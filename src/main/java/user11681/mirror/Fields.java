package user11681.mirror;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import sun.reflect.ReflectionFactory;

@SuppressWarnings("unchecked")
public class Fields {
    public static void addToArray(final String name, final Object owner, final Object newElement) {
        addToArray(owner.getClass(), owner, name, newElement);
    }

    public static void addToArray(final Class<?> clazz, final String name, final Object newElement) {
        addToArray(clazz, null, name, newElement);
    }

    public static void addToArray(final Class<?> clazz, final Object owner, final String name, final Object newElement) {
        addToArray(getLowestField(clazz, name), owner, newElement);
    }

    public static void addToArray(final Field field, final Object newElement) {
        addToArray(field, null, newElement);
    }

    public static void addToArray(final Field field, final Object owner, Object newElement) {
        try {
            final Field modifiers = Field.class.getDeclaredField("modifiers");

            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);

            final Object[] original = (Object[]) field.get(owner);
            final int length = original.length;
            final Object[] newArray = Arrays.copyOf(original, length + 1);

            newArray[length] = newElement;

            field.set(owner, newArray);
        } catch (final IllegalAccessException | NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static <U> U getFieldValue(final Object object, final String name) {
        return getFieldValue(object.getClass(), object, name);
    }

    public static <U> U getFieldValue(final Class<?> clazz, final String name) {
        return getFieldValue(clazz, null, name);
    }

    @Nonnull
    public static <U> U getFieldValue(final Class<?> clazz, final Object object, final String name) {
        return getFieldValue(object, getLowestField(clazz, name));
    }

    public static <T> T getFieldValue(final Field field) {
        return getFieldValue(null, field);
    }

    @Nonnull
    public static <T> T getFieldValue(final Object object, final Field field) {
        try {
            final T value;

            field.setAccessible(true);
            value = (T) field.get(object);
            field.setAccessible(false);

            return value;
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setField(final Object object, final String name, final Object value) {
        setField(object.getClass(), object, name, value);
    }

    public static void setField(final Class<?> clazz, final String name, final Object value) {
        setField(clazz, null, name, value);
    }

    public static void setField(final Class<?> clazz, final Object object, final String name, final Object value) {
        try {
            final Field field = getLowestField(clazz, name);

            if (object == null) {
                setField(field, "modifiers", field.getModifiers() & ~Modifier.FINAL);
            }

            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static List<Field> getAllFields(final Class<?> clazz) {
        final List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        final Class<?> superclass = clazz.getSuperclass();

        if (superclass != null) {
            fields.addAll(getAllFields(superclass));
        }

        return fields;
    }

    public static Field makeAccessible(final Field field) {
        final Field modifiers = getDeclaredField(Field.class, "modifiers");

        field.setAccessible(true);
        modifiers.setAccessible(true);
        setInt(modifiers, field, field.getModifiers() & ~Modifier.FINAL);

        return field;
    }

    public static Field getLowestField(final Object object, final String name) {
        return getLowestField(object.getClass(), name);
    }

    public static Field getLowestField(final Class<?> clazz, final String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (final NoSuchFieldException exception) {
            final Class<?> superclass = clazz.getSuperclass();

            if (superclass == null) {
                throw new ReflectionException(exception);
            }

            return getLowestField(superclass, name);
        }
    }

    public static Field getDeclaredField(final Class<?> clazz, final String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (final NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    /**
     * get the {@code private static final synthetic} enum array field in {@code enumClass}.
     *
     * @param enumClass the {@link Class} object of an enumeration from which to get its synthetic enum array field.
     * @return the synthetic enum array field contained by {@code enumClass}.
     */
    public static Field getEnumArrayField(final Class<?> enumClass) {
        for (final Field field : enumClass.getDeclaredFields()) {
            final int modifiers = field.getModifiers();

            if (field.isSynthetic() && field.getType().getComponentType() == enumClass && Modifier.isPrivate(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                return field;
            }
        }

        throw new IllegalArgumentException(String.format("%s is not an enum class.", enumClass.getName()));
    }

    /**
     * add a {@linkplain Field field} holding an enum {@code instance} to the class of its type.
     *
     * @param instance an enum instance.
     */
    public static void addEnumField(final Enum<?> instance) {
        //noinspection ClassGetClass
        addDeclaredField(instance.getClass().getClass(), newDeclaredEnumField(instance));
    }

    /**
     * construct a {@linkplain Field field} belonging to the type of and holding {@code instance}.
     *
     * @param instance an enum instance.
     * @return a new {@linkplain Field field} belonging to the type of and holding {@code instance}.
     */
    public static Field newDeclaredEnumField(final Enum<?> instance) {
        //noinspection ClassGetClass
        final Class<?> clazz = instance.getClass().getClass();

        return ReflectionFactory.getReflectionFactory().newField(clazz, instance.name(), clazz, Modifiers.ENUM_FIELD, clazz.getDeclaredFields().length, null, null);
    }

    /**
     * add {@linkplain Field field} to {@linkplain Class.ReflectionData#declaredFields declaredFields}.
     *
     * @param field the {@linkplain Field field} to add.
     */
    @SuppressWarnings("JavadocReference")
    public static void addDeclaredField(Class<?> clazz, final Field field) {
        try {
            final Method privateGetDeclaredFields = clazz.getDeclaredMethod("privateGetDeclaredFields", boolean.class);
            final Method reflectionData = clazz.getDeclaredMethod("reflectionData");

            privateGetDeclaredFields.setAccessible(true);
            reflectionData.setAccessible(true);

            final Field[] oldDeclaredFields = (Field[]) privateGetDeclaredFields.invoke(clazz, false);
            final int length = oldDeclaredFields.length;
            final Field[] newDeclaredFields = Arrays.copyOf(oldDeclaredFields, length + 1);
            final Class<?> ReflectionData = Classes.forName("java.lang.Class$ReflectionData");
            final Field declaredFields = ReflectionData.getDeclaredField("declaredFields");

            declaredFields.setAccessible(true);
            reflectionData.setAccessible(true);

            newDeclaredFields[length] = field;
            declaredFields.set(reflectionData.invoke(clazz), newDeclaredFields);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void set(final Field field, final Object object, final Object value) throws ReflectionException {
        try {
            field.set(object, value);
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
