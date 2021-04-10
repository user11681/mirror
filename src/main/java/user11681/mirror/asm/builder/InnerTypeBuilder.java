package user11681.mirror.asm.builder;

public class InnerTypeBuilder extends NodeBuilder<InnerTypeBuilder> {
    protected final TypeBuilder parent;

    protected String outerName;
    protected String innerName;

    public InnerTypeBuilder(final TypeBuilder parent) {
        this.parent = parent;
    }

    public InnerTypeBuilder outerName(final String name) {
        this.outerName = outerName;

        return this;
    }

    public InnerTypeBuilder innerName(final String name) {
        this.innerName = innerName;

        return this;
    }

    public TypeBuilder build() {
        return this.parent;
    }
}
