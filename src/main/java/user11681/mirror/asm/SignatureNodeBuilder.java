package user11681.mirror.asm;

import org.objectweb.asm.Type;

public abstract class SignatureNodeBuilder<T extends SignatureNodeBuilder<T>> extends NodeBuilder<T> {
    protected String signature;

    public T signature(final Class<?> clazz) {
        return this.signature(Type.getDescriptor(clazz));
    }

    public T signature(final String signature) {
        this.signature = signature;

        return self;
    }
}
