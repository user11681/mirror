package user11681.mirror;

import java.lang.reflect.Method;

public class Methods {
    public static Method getLowestMethod(final Object object, final String methodName, final Class<?>... parameterTypes) {
        return getLowestMethod(object.getClass(), methodName, parameterTypes);
    }

    public static Method getLowestMethod(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (final NoSuchMethodException exception) {
            final Class<?> superclass = clazz.getSuperclass();

            if (superclass == null) {
                throw new ReflectionException(exception);
            }

            return getLowestMethod(superclass, name, parameterTypes);
        }
    }
}
