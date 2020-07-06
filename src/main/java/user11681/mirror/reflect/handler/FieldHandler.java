package user11681.mirror.reflect.handler;

import java.lang.reflect.Field;
import user11681.mirror.reflect.ReflectionException;

public class FieldHandler {
    public static Field getDeclaredField(final Class<?> clazz, final String name) throws ReflectionException {
        try {
            return clazz.getDeclaredField(name);
        } catch (final NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(final Field field, final Object owner) throws ReflectionException {
        try {
            return (T) field.get(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static boolean getBoolean(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getBoolean(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static char getChar(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getChar(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static byte getByte(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getByte(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static short getShort(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getShort(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static int getInt(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getInt(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static long getLong(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getLong(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static float getFloat(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getFloat(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static double getDouble(final Field field, final Object owner) throws ReflectionException {
        try {
            return field.getDouble(owner);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void set(final Field field, final Object owner, final Object value) throws ReflectionException {
        try {
            field.set(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setBoolean(final Field field, final Object owner, final boolean value) throws ReflectionException {
        try {
            field.setBoolean(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setChar(final Field field, final Object owner, final char value) throws ReflectionException {
        try {
            field.setChar(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setByte(final Field field, final Object owner, final byte value) throws ReflectionException {
        try {
            field.setByte(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setShort(final Field field, final Object owner, final short value) throws ReflectionException {
        try {
            field.setShort(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setInt(final Field field, final Object owner, final int value) throws ReflectionException {
        try {
            field.setInt(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setLong(final Field field, final Object owner, final long value) throws ReflectionException {
        try {
            field.setLong(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setFloat(final Field field, final Object owner, final float value) throws ReflectionException {
        try {
            field.setFloat(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setDouble(final Field field, final Object owner, final double value) throws ReflectionException {
        try {
            field.setDouble(owner, value);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }
}
