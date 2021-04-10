package user11681.mirror.asm.builder;

public class ConstructorBuilder extends AbstractMethodBuilder<ConstructorBuilder> {
    public ConstructorBuilder(final TypeBuilder builder) {
        super(builder);

        this.name = "<init>";
        this.returnType = "V";
    }

    @Override
    public ConstructorBuilder name(final String name) {
        throw new UnsupportedOperationException("constructor's name is automatically \"<init>\"");
    }
}
