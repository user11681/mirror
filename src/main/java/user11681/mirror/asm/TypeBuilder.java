package user11681.mirror.asm;

import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import user11681.mirror.reflect.Mirror;

public class TypeBuilder extends SignatureNodeBuilder<TypeBuilder> {
    protected final ClassWriter writer;
    protected final Set<String> interfaces;
    protected final Set<FieldBuilder> fields;
    protected final Set<MethodBuilder> methods;
    protected final Set<InnerTypeBuilder> innerTypes;

    protected String superclass;

    public TypeBuilder() {
        this.writer = new ClassWriter(0);
        this.interfaces = new HashSet<>();
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();
        this.innerTypes = new HashSet<>();
    }

    public TypeBuilder extend(final Class<?> superclass) {
        return this.extend(Type.getDescriptor(superclass));
    }

    public TypeBuilder extend(final String superclass) {
        this.superclass = superclass;

        return self;
    }

    public TypeBuilder implement(final Class<?> interfaze) {
        return this.implement(Type.getDescriptor(interfaze));
    }

    public TypeBuilder implement(final String interfaze) {
        this.interfaces.add(interfaze);

        return self;
    }

    public InnerTypeBuilder inner() {
        final InnerTypeBuilder innerType = new InnerTypeBuilder(this);

        this.innerTypes.add(innerType);

        return innerType;
    }

    public FieldBuilder field() {
        final FieldBuilder field = new FieldBuilder(this);

        this.fields.add(field);

        return field;
    }

    public MethodBuilder method() {
        final MethodBuilder method = new MethodBuilder(this);

        this.methods.add(method);

        return method;
    }

    public <U> Class<U> build() {
        final ClassWriter writer = this.writer;

        writer.visit(Opcodes.V1_8, this.access, this.name, this.signature, this.superclass, (String[]) this.interfaces.toArray());

        for (final MethodBuilder method : this.methods) {
            writer.visitMethod(method.access, method.name, method.descriptor, method.signature, (String[]) method.exceptions.toArray());
        }

        for (final FieldBuilder field : this.fields) {
            writer.visitField(field.access, field.name, field.descriptor, field.signature, field.value);
        }

        for (final InnerTypeBuilder innerType : this.innerTypes) {
            writer.visitInnerClass(innerType.name, innerType.outerName, innerType.innerName, innerType.access);
        }

        writer.visitEnd();

        //noinspection unchecked
        return (Class<U>) Mirror.CLASS_LOADER.defineClass(this.name, writer.toByteArray());
    }
}
