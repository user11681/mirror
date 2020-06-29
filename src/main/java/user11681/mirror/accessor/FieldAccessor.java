package user11681.mirror.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import user11681.mirror.ReflectionException;
import user11681.mirror.handler.FieldHandler;

public class FieldAccessor {
    public static <T> T get(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.get(field, owner);
    }

    public static boolean getBoolean(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getBoolean(field, owner);
    }

    public static char getChar(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getChar(field, owner);
    }

    public static byte getByte(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getByte(field, owner);
    }

    public static short getShort(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getShort(field, owner);
    }

    public static int getInt(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getInt(field, owner);
    }

    public static long getLong(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getLong(field, owner);
    }

    public static float getFloat(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getFloat(field, owner);
    }

    public static double getDouble(final Field field, final Object owner) throws ReflectionException {
        makeAccessible(field);

        return FieldHandler.getDouble(field, owner);
    }

    public static void set(final Field field, final Object owner, final Object value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.set(field, owner, value);
    }

    public static void setBoolean(final Field field, final Object owner, final boolean value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setBoolean(field, owner, value);
    }

    public static void setChar(final Field field, final Object owner, final char value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setChar(field, owner, value);
    }

    public static void setByte(final Field field, final Object owner, final byte value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setByte(field, owner, value);
    }

    public static void setShort(final Field field, final Object owner, final short value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setShort(field, owner, value);
    }

    public static void setInt(final Field field, final Object owner, final int value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setInt(field, owner, value);
    }

    public static void setLong(final Field field, final Object owner, final long value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setLong(field, owner, value);
    }

    public static void setFloat(final Field field, final Object owner, final float value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setFloat(field, owner, value);
    }

    public static void setDouble(final Field field, final Object owner, final double value) throws ReflectionException {
        makeAccessible(field);

        FieldHandler.setDouble(field, owner, value);
    }

    public static void makeAccessible(final Field field) throws ReflectionException {
        field.setAccessible(true);

        if (Modifier.isStatic(field.getModifiers())) {
            removeFinal(field);
        }
    }

    public static void removeFinal(final Field field) throws ReflectionException {
        try {
            final Field modifiers = Field.class.getDeclaredField("modifiers");

            modifiers.setAccessible(true);

            setInt(modifiers, field, field.getModifiers() & ~Modifier.FINAL);
        } catch (final NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }
}
