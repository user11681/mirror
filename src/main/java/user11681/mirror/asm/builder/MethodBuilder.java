package user11681.mirror.asm.builder;

import org.objectweb.asm.Type;

public class MethodBuilder extends AbstractMethodBuilder<MethodBuilder> {
    public MethodBuilder(final TypeBuilder builder) {
        super(builder);
    }

    public MethodBuilder returns(final Class<?> returnType) {
        return this.returns(Type.getDescriptor(returnType));
    }

    public MethodBuilder returns(final String returnType) {
        this.returnType = returnType;

        return this;
    }

    @Override
    public MethodBuilder name(final String name) {
        if (name.equals("<init>")) {
            throw new UnsupportedOperationException("TypeBuilder::method used for <init>; use TypeBuilder::constructor for constructors");
        }

        return super.name(name);
    }
}
