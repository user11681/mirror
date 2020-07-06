package user11681.mirror.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class FieldWrapper<F> {
    protected final Field field;
    protected final Object owner;

    public FieldWrapper(final String field, final Object owner) {
        this(owner.getClass(), owner, field);
    }

    public FieldWrapper(final Class<?> clazz, final String name) {
        this(Fields.getLowestField(clazz, name));
    }

    public FieldWrapper(final Class<?> clazz, final Object owner, final String field) {
        this(Fields.getLowestField(clazz, field), owner);
    }

    public FieldWrapper(final Field field) {
        this(field, null);
    }

    public FieldWrapper(final Field field, final Object owner) {
        this.field = field;
        this.owner = owner;

        this.setAccessible();
    }

    public final F get() {
        try {
            return (F) this.field.get(this.owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public final F get(final Object owner) {
        try {
            return (F) this.field.get(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public final void set(final Object value) {
        try {
            this.field.set(this.owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public final void set(final Object owner, final Object value) {
        try {
            this.field.set(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    private void setAccessible() {
        if (this.owner == null) {
            Fields.setLowestField(this.field, "modifiers", this.field.getModifiers() & ~Modifier.FINAL);
        }

        this.field.setAccessible(true);
    }
}
