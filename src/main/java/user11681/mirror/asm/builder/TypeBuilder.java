package user11681.mirror.asm.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import user11681.mirror.Mirror;
import user11681.mirror.reflect.Throwables;

public class TypeBuilder extends SignatureNodeBuilder<TypeBuilder> {
    protected final ClassWriter writer;
    protected final Set<String> interfaces;
    protected final Set<FieldBuilder> fields;
    protected final Set<AbstractMethodBuilder<?>> methods;
    protected final Set<InnerTypeBuilder> innerTypes;

    protected int version;

    protected String superclass;
    protected String path;
    protected String type;

    public TypeBuilder() {
        this.writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        this.interfaces = new HashSet<>();
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();
        this.innerTypes = new HashSet<>();

        this.version = Opcodes.V1_8;
    }

    public TypeBuilder version(final int version) {
        this.version = version;

        return this;
    }

    @Override
    public TypeBuilder name(final String path) {
        this.path = path;
        this.type = String.format("L%s;", path);

        return super.name(String.format("%s", path.replace('/', '.')));
    }

    public TypeBuilder extend(final Class<?> superclass) {
        return this.extend(Type.getInternalName(superclass));
    }

    public TypeBuilder extend(final String superclass) {
        this.superclass = superclass;

        return this;
    }

    public TypeBuilder implement(final Class<?> interfaze) {
        return this.implement(Type.getDescriptor(interfaze));
    }

    public TypeBuilder implement(final String interfaze) {
        this.interfaces.add(interfaze);

        return this;
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

    public ConstructorBuilder constructor() {
        final ConstructorBuilder constructor = new ConstructorBuilder(this);

        this.methods.add(constructor);

        return constructor;
    }

    public TypeBuilder defaultConstructor() {
        this.methods.add(new ConstructorBuilder(this).publicAccess().descriptor("()V"));

        return this;
    }

    public MethodBuilder method() {
        final MethodBuilder method = new MethodBuilder(this);

        this.methods.add(method);

        return method;
    }

    public <U> Class<U> build() {
        this.writer.visit(this.version, this.access, this.path, this.signature, this.superclass, this.interfaces.toArray(new String[0]));

        for (final AbstractMethodBuilder<?> method : this.methods) {
            if (method.descriptor == null) {
                throw Throwables.format(new IllegalArgumentException("descriptor of method %s is null"), method);
            }

            method.build(this.writer.visitMethod(method.access, method.name, method.descriptor, method.signature, method.exceptions.toArray(new String[0])));
        }

        for (final FieldBuilder field : this.fields) {
            field.build(this.writer.visitField(field.access, field.name, field.descriptor, field.signature, field.value));
        }

        for (final InnerTypeBuilder innerType : this.innerTypes) {
            this.writer.visitInnerClass(innerType.name, innerType.outerName, innerType.innerName, innerType.access);
        }

        this.writer.visitEnd();

        return this.defineClass();
    }

    protected <T> Class<T> defineClass() {
        try {
            final File classFile = new File(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),
                    this.path + ".class"
            );

            //noinspection ResultOfMethodCallIgnored
            classFile.getParentFile().mkdirs();

            if (!classFile.createNewFile()) {
                Mirror.LOGGER.warn(String.format("File %s already exists; overwriting it.", classFile));
            }

            new FileOutputStream(classFile).write(this.writer.toByteArray());

            //noinspection unchecked
            return (Class<T>) Class.forName(this.name);
        } catch (final IOException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.name);

        if (this.superclass != null) {
            builder.append(" extends ").append(this.superclass);
        }

        if (!this.interfaces.isEmpty()) {
            builder.append(" implements ");

            final Iterator<String> iterator = this.interfaces.iterator();

            builder.append(iterator.next());

            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next());
            }
        }

        return builder.toString();
    }
}
