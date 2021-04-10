package user11681.mirror.asm.builder;

import org.objectweb.asm.Opcodes;

public abstract class NodeBuilder<T extends NodeBuilder<T>> {
    @SuppressWarnings("unchecked")
    protected final T self = (T) this;

    protected int access;
    protected String name;

    public T publicAccess() {
        this.access |= Opcodes.ACC_PUBLIC;

        return self;
    }

    public T protectedAccess() {
        this.access |= Opcodes.ACC_PROTECTED;

        return self;
    }

    public T privateAccess() {
        this.access |= Opcodes.ACC_PRIVATE;

        return self;
    }

    public T superFlag() {
        this.access |= Opcodes.ACC_SUPER;

        return self;
    }

    public T abstractFlag() {
        this.access |= Opcodes.ACC_ABSTRACT;

        return self;
    }

    public T enumFlag() {
        this.access |= Opcodes.ACC_ENUM;

        return self;
    }

    public T staticFlag() {
        this.access |= Opcodes.ACC_STATIC;

        return self;
    }

    public T finalFlag() {
        this.access |= Opcodes.ACC_FINAL;

        return self;
    }

    public T syntheticFlag() {
        this.access |= Opcodes.ACC_SYNTHETIC;

        return self;
    }

    public T name(final String name) {
        this.name = name;

        return self;
    }
}
