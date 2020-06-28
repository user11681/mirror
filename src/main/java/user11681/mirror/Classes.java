package user11681.mirror;

public class Classes {
    public static Class<?> forNameNullable(final String name) {
        try {
            return Class.forName(name);
        } catch (final ClassNotFoundException exception) {
            return null;
        }
    }

    public static Class<?> forName(final String name) throws NotFoundException {
        try {
            return Class.forName(name);
        } catch (final ClassNotFoundException exception) {
            throw new NotFoundException(exception);
        }
    }
}
