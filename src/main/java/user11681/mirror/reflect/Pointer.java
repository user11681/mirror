package user11681.mirror.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;

public final class Pointer<T> {
    private final Object owner;
    private final Field field;

    private Pointer(final Class<?> clazz, final Object owner, final String name) {
        final Field field1 = Fields.getLowestField(clazz, name);

        this.field = field1;
        this.owner = owner;

        field1.setAccessible(true);

        try {
            final Field modifiers = Field.class.getDeclaredField("modifiers");

            modifiers.setAccessible(true);
            modifiers.setInt(field1, field1.getModifiers() & ~Modifier.FINAL);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static <T> Pointer<T> of(@Nonnull final Object owner, final String name) {
        return new Pointer<>(owner.getClass(), owner, name);
    }

    public static <T> Pointer<T> of(final Class<?> clazz, final String name) {
        return new Pointer<>(clazz, null, name);
    }

    public final T get() {
        try {
            //noinspection unchecked
            return (T) this.field.get(this.owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public final void set(final T value) {
        try {
            this.field.set(this.owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }
}
