package user11681.mirror.reflect;

import java.lang.reflect.Constructor;

public class ConstructorWrapper<T> {
    protected final Constructor<T> constructor;

    public ConstructorWrapper(final Class<T> clazz, final Class<?>... parameterTypes) {
        this(Constructors.getConstructor(clazz, parameterTypes));
    }

    public ConstructorWrapper(final Constructor<T> constructor) {
        this.constructor = constructor;
        this.constructor.setAccessible(true);
    }
}
