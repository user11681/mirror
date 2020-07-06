package user11681.mirror.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sun.reflect.ReflectionFactory;
import user11681.mirror.reflect.accessor.FieldAccessor;
import user11681.mirror.reflect.handler.FieldHandler;

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

    public static void addToArray(final Field array, final Object owner, Object newElement) {
        try {
            FieldAccessor.makeAccessible(array);

            final Object[] original = (Object[]) array.get(owner);
            final int length = original.length;
            final Object[] newArray = Arrays.copyOf(original, length + 1);

            newArray[length] = newElement;

            array.set(owner, newArray);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static <U> U getFieldValue(final Object object, final String name) {
        return getFieldValue(object.getClass(), object, name);
    }

    public static <U> U getFieldValue(final Class<?> clazz, final String name) {
        return getFieldValue(clazz, null, name);
    }

    public static <U> U getFieldValue(final Class<?> clazz, final Object object, final String name) {
        return getFieldValue(object, getLowestField(clazz, name));
    }

    public static <T> T getFieldValue(final Field field) {
        return getFieldValue(null, field);
    }

    public static <T> T getFieldValue(final Object object, final Field field) {
        try {
            field.setAccessible(true);

            return (T) field.get(object);
        } catch (final IllegalAccessException exception) {
            throw new ReflectionException(exception);
        }
    }

    public static void setLowestField(final Object owner, final String name, final Object value) {
        setLowestField(owner.getClass(), owner, name, value);
    }

    public static void setLowestField(final Class<?> clazz, final String name, final Object value) {
        setLowestField(clazz, null, name, value);
    }

    public static void setLowestField(final Class<?> clazz, final Object owner, final String name, final Object value) {
        setField(clazz, owner, getLowestField(clazz, name), value);
    }

    public static void setField(final Object owner, final String name, final Object value) {
        setField(owner.getClass(), owner, name, value);
    }

    public static void setField(final Class<?> clazz, final String name, final Object value) {
        setField(clazz, null, name, value);
    }

    public static void setField(final Class<?> clazz, final Object owner, final String name, final Object value) {
        setField(clazz, owner, FieldHandler.getDeclaredField(clazz, name), value);
    }

    public static void setField(final Class<?> clazz, final Object owner, final Field field, final Object value) {
        try {
            FieldAccessor.makeAccessible(field);

            field.set(owner, value);
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

    public static Field getLowestField(final Object object, final String name) {
        return getLowestField(object.getClass(), name);
    }

    public static Field getLowestField(final Class<?> original, final String name) {
        Class<?> clazz = original;

        while (clazz != null) {
            try {
                return clazz.getDeclaredField(name);
            } catch (final NoSuchFieldException exception) {
                clazz = clazz.getSuperclass();
            }
        }

        throw Throwables.format(new ReflectionException("field %s was not found in %s or its superclasses."), original, name);
    }

    /**
     * get the {@code private static final synthetic} enum array field in {@code enumClass}.
     *
     * @param enumClass the {@link Class} object of an enumeration from which to get its synthetic enum array field.
     * @return the synthetic enum array field contained by {@code enumClass}.
     */
    public static Field getEnumArrayField(final Class<?> enumClass) {
        for (final Field field : enumClass.getDeclaredFields()) {
            if (Modifiers.isEnumArrayField(field, enumClass)) {
                return field;
            }
        }

        throw new IllegalArgumentException(String.format("%s is not an enum class.", enumClass.getName()));
    }

    public static List<Field> getEnumArrayFields(final Class<?> enumClass) {
        final List<Field> fields = new ArrayList<>();

        for (final Field field : enumClass.getDeclaredFields()) {
            if (Modifiers.isEnumArrayField(field, enumClass)) {
                fields.add(field);
            }
        }

        if (fields.size() > 0) {
            return fields;
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
        addDeclaredField(instance.getClass().getClass(), newEnumField(instance));
    }

    /**
     * construct a {@linkplain Field field} belonging to the type of and holding {@code instance}.
     *
     * @param instance an enum instance.
     * @return a new {@linkplain Field field} belonging to the type of and holding {@code instance}.
     */
    public static Field newEnumField(final Enum<?> instance) {
        //noinspection ClassGetClass
        final Class<?> clazz = instance.getClass().getClass();

        return ReflectionFactory.getReflectionFactory().newField(clazz, instance.name(), clazz, Modifiers.ENUM_FIELD, clazz.getDeclaredFields().length, null, null);
    }

    /**
     * add {@code field} to {@linkplain Class.ReflectionData#declaredFields declaredFields}.
     *
     * @param field the {@linkplain Field field} to add.
     */
    @SuppressWarnings({"JavadocReference", "ResultOfMethodCallIgnored"})
    public static void addDeclaredField(Class<?> clazz, final Field field) {
        final Class<?> ReflectionData = Classes.forName("java.lang.Class$ReflectionData");
        final Object reflectionData = new MethodWrapper<>("reflectionData", clazz).invoke();

        if (getFieldValue(reflectionData, "declaredFields") == null) {
            clazz.getDeclaredFields();
        }

        if (getFieldValue(reflectionData, "publicFields") == null) {
            clazz.getFields();
        }

        addToArray("declaredFields", reflectionData, field);

        if (Modifier.isPublic(field.getModifiers())) {
            addToArray("publicFields", reflectionData, field);
        }
    }

}
