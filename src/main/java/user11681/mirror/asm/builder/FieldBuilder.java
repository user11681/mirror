package user11681.mirror.asm.builder;

import org.objectweb.asm.FieldVisitor;

public class FieldBuilder extends MemberBuilder<FieldBuilder> {
    protected final TypeBuilder builder;

    protected Object value;

    public FieldBuilder(final TypeBuilder builder) {
        this.builder = builder;
    }

    public FieldBuilder value(final Object value) {
        this.value = value;

        return self;
    }

    public TypeBuilder then() {
        return this.builder;
    }

    protected void build(final FieldVisitor visitor) {
        visitor.visitEnd();
    }
}
