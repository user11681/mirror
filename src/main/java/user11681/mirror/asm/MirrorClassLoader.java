package user11681.mirror.asm;

public class MirrorClassLoader extends ClassLoader {
    public Class<?> defineClass(final String name, final byte[] definition) {
        return this.defineClass(name, definition, 0, definition.length);
    }
}
