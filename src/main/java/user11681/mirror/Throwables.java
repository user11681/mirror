package user11681.mirror;

import java.lang.reflect.Field;

public class Throwables {
    private static final Field detailMessage = Fields.getDeclaredField(Throwable.class, "detailMessage");

    static {
        detailMessage.setAccessible(true);
    }

    public static <T extends Throwable> T format(final T throwable, final Object... arguments) {
        try {
            detailMessage.set(throwable, String.format((String) detailMessage.get(throwable), arguments));
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }

        return throwable;
    }
}
