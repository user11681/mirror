package user11681.mirror.asm.builder;

import org.objectweb.asm.Type;

public abstract class MemberBuilder<T extends MemberBuilder<T>> extends SignatureNodeBuilder<T> {
    protected String descriptor;

    public T descriptor(final Class<?> clazz) {
        return this.descriptor(Type.getDescriptor(clazz));
    }

    public T descriptor(final String descriptor) {
        this.descriptor = descriptor;

        return self;
    }
}
