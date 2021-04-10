package user11681.usersmanual.type;

import java.util.HashMap;
import java.util.Map;

public class TypeUtil {
    private static final Map<Class<?>, Class<?>> OBJECT_TO_PRIMITIVE = new HashMap<>();

    static {
        OBJECT_TO_PRIMITIVE.put(Boolean.class, boolean.class);
        OBJECT_TO_PRIMITIVE.put(Character.class, char.class);
        OBJECT_TO_PRIMITIVE.put(Byte.class, byte.class);
        OBJECT_TO_PRIMITIVE.put(Short.class, short.class);
        OBJECT_TO_PRIMITIVE.put(Integer.class, int.class);
        OBJECT_TO_PRIMITIVE.put(Long.class, long.class);
        OBJECT_TO_PRIMITIVE.put(Float.class, float.class);
        OBJECT_TO_PRIMITIVE.put(Double.class, double.class);
    }

    public static boolean areEquivalent(final Class<?>[] first, final Class<?>[] second) {
        if (first.length == second.length) {
            for (int i = 0, length = first.length; i < length; i++) {
                final Class<?> clazz = first[i];
                final Class<?> other = second[i];

                if (clazz.isPrimitive() && !other.isPrimitive() && getPrimitiveEquivalent(other) != clazz
                        || !clazz.isPrimitive() && other.isPrimitive() && getPrimitiveEquivalent(clazz) != other
                        || clazz != other) {
                    return false;
                }
            }
        }

        return false;
    }

    public static Class<?> getPrimitiveEquivalent(final Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        }

        return OBJECT_TO_PRIMITIVE.getOrDefault(clazz, clazz);
    }
}
