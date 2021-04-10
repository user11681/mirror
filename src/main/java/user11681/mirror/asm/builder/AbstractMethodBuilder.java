package user11681.mirror.asm.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class AbstractMethodBuilder<T extends AbstractMethodBuilder<T>> extends MemberBuilder<T> {
    protected final TypeBuilder parent;
    protected final Map<String, Class<?>> parameters;
    protected final Set<String> exceptions;

    protected String returnType;

    public AbstractMethodBuilder(final TypeBuilder parent) {
        this.parent = parent;
        this.parameters = new HashMap<>();
        this.exceptions = new HashSet<>();
    }

    public T parameter(final Class<?> clazz, final String name) {
        this.parameters.put(name, clazz);

        return self;
    }

    public T exception(final String exception) {
        this.exceptions.add(exception);

        return self;
    }

/*
    public MethodBuilder body() {
        return this;
    }
*/

    public TypeBuilder then() {
        return this.parent;
    }

    protected void build(final MethodVisitor visitor) {
        visitor.visitCode();

        final Label label0 = new Label();
        visitor.visitLabel(label0);

        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, this.parent.superclass, "<init>", "()V", false);

        visitor.visitInsn(this.getReturnOpcode());

        final Label label1 = new Label();
        visitor.visitLabel(label1);

        visitor.visitLocalVariable("this", this.parent.type, null, label0, label1, 0);
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    protected int getReturnOpcode() {
        switch (this.returnType) {
            case "V":
                return Opcodes.RETURN;
            case "Z":
            case "C":
            case "B":
            case "S":
            case "I":
                return Opcodes.IRETURN;
            case "J":
                return Opcodes.LRETURN;
            case "F":
                return Opcodes.FRETURN;
            case "D":
                return Opcodes.DRETURN;
            default:
                return Opcodes.ARETURN;
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%s)%s in %s", this.name, this.descriptor, this.returnType, this.parent);
    }
}
