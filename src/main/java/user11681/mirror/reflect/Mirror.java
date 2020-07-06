package user11681.mirror.reflect;

import sun.misc.Unsafe;
import user11681.mirror.asm.MirrorClassLoader;
import user11681.mirror.reflect.accessor.FieldAccessor;

public class Mirror {
    public static final Unsafe theUnsafe = FieldAccessor.get(Unsafe.class, "theUnsafe");
    public static final MirrorClassLoader CLASS_LOADER = new MirrorClassLoader();
}
