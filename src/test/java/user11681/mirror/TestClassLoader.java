package user11681.mirror;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import sun.misc.Resource;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;
import sun.security.util.Debug;

import static user11681.mirror.Mirror.theUnsafe;

public class TestClassLoader extends URLClassLoader {
    private static final Debug debug = getField("debug", null);

    private final URLClassPath ucp;
    private final AccessControlContext acc;
    private final HashMap<CodeSource, ProtectionDomain> pdcache;

    public TestClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);

        this.ucp = this.getField("ucp");
        this.acc = this.getField("acc");
        this.pdcache = this.getField("pdcache");
    }

    public TestClassLoader(final URL[] urls) {
        super(urls);

        this.ucp = this.getField("ucp");
        this.acc = this.getField("acc");
        this.pdcache = this.getField("pdcache");
    }

    public TestClassLoader(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);

        this.ucp = this.getField("ucp");
        this.acc = this.getField("acc");
        this.pdcache = this.getField("pdcache");
    }

    public static Class<?> defineClass(final String name, final byte[] klass) {
        return theUnsafe.defineClass(name, klass, 0, klass.length, null, null);
    }

    private static <T> T getField(final String name, final Object owner) {
        try {
            Class<?> klass = URLClassLoader.class;

            while (klass != Object.class) {
                try {
                    final Field field = klass.getDeclaredField(name);

                    field.setAccessible(true);

                    //noinspection unchecked
                    return (T) field.get(owner);
                } catch (final NoSuchFieldException exception) {
                    klass = klass.getSuperclass();
                }
            }

            throw new RuntimeException("error");
        } catch (final IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private <T> T getField(final String name) {
        return getField(name, this);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final Class<?> result;
        try {
            result = AccessController.doPrivileged(
                    (PrivilegedExceptionAction<Class<?>>) () -> {
                        String path = name.replace('.', '/').concat(".class");
                        Resource res = TestClassLoader.this.ucp.getResource(path, false);
                        if (res != null) {
                            try {
                                return defineClass(name, res);
                            } catch (IOException e) {
                                throw new ClassNotFoundException(name, e);
                            }
                        } else {
                            return null;
                        }
                    }, acc);
        } catch (PrivilegedActionException pae) {
            throw (ClassNotFoundException) pae.getException();
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }

        return result;
    }

    private Class<?> defineClass(final String name, final Resource resource) throws IOException {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        URL url = resource.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            Manifest man = resource.getManifest();
            definePackageInternal(pkgname, man, url);
        }
        // Now read the class bytes and define the class
        ByteBuffer bb = resource.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            CodeSigner[] signers = resource.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineKlass(name, bb, cs);
        } else {
            byte[] b = resource.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = resource.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, b, 0, b.length, cs);
        }
    }

    private void definePackageInternal(String pkgname, Manifest man, URL url) {
        if (getAndVerifyPackage(pkgname, man, url) == null) {
            try {
                if (man != null) {
                    definePackage(pkgname, man, url);
                } else {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            } catch (IllegalArgumentException iae) {
                // parallel-capable class loaders: re-verify in case of a
                // race condition
                if (getAndVerifyPackage(pkgname, man, url) == null) {
                    // Should never happen
                    throw new AssertionError("Cannot find package " +
                            pkgname);
                }
            }
        }
    }

    private Package getAndVerifyPackage(String pkgname, Manifest man, URL url) {
        Package pkg = getPackage(pkgname);
        if (pkg != null) {
            // Package found, so check package sealing.
            if (pkg.isSealed()) {
                // Verify that code source URL is the same.
                if (!pkg.isSealed(url)) {
                    throw new SecurityException(
                            "sealing violation: package " + pkgname + " is sealed");
                }
            } else {
                // Make sure we are not attempting to seal the package
                // at this code source URL.
                if ((man != null) && isSealed(pkgname, man)) {
                    throw new SecurityException(
                            "sealing violation: can't seal package " + pkgname +
                                    ": already loaded");
                }
            }
        }
        return pkg;
    }

    private boolean isSealed(String name, Manifest man) {
        Attributes attr = SharedSecrets.javaUtilJarAccess()
                .getTrustedAttributes(man, name.replace('.', '/').concat("/"));
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }

    protected final Class<?> defineKlass(String name, ByteBuffer b, CodeSource cs) {
        return defineClass(name, b, getProtectionDomain(cs));
    }

    private ProtectionDomain getProtectionDomain(CodeSource cs) {
        if (cs == null) {
            return null;
        }

        ProtectionDomain pd;
        synchronized (pdcache) {
            pd = pdcache.get(cs);
            if (pd == null) {
                PermissionCollection perms = getPermissions(cs);
                pd = new ProtectionDomain(cs, perms, this, null);
                pdcache.put(cs, pd);
                if (debug != null) {
                    debug.println(" getPermissions " + pd);
                    debug.println("");
                }
            }
        }
        return pd;
    }
}
