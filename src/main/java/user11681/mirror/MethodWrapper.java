package user11681.mirror;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper<R> {
    protected final Method method;
    protected final Object owner;

    public MethodWrapper(final String name, final Object owner, final Class<?>... parameterTypes) {
        this(owner.getClass(), owner, name, parameterTypes);
    }

    public MethodWrapper(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
        this(Methods.getLowestMethod(clazz, name, parameterTypes));
    }

    public MethodWrapper(final Class<?> clazz, final Object owner, final String name, final Class<?>... parameterTypes) {
        this(Methods.getLowestMethod(clazz, name, parameterTypes), owner);
    }

    public MethodWrapper(final Method method) {
        this(method, null);
    }

    public MethodWrapper(final Method method, final Object owner) {
        this.method = method;
        this.method.setAccessible(true);
        this.owner = owner;
    }

    public R invoke(final Object... args) {
        return this.invokeOn(this.owner, args);
    }

    @SuppressWarnings("unchecked")
    public R invokeOn(final Object object, final Object... args) {
        try {
            return (R) this.method.invoke(object, args);
        } catch (final IllegalAccessException | InvocationTargetException exception) {
            throw new InternalError(exception);
        }
    }
}
