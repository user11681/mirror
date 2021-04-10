package user11681.mirror;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import junit.framework.TestCase;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import user11681.mirror.asm.builder.TypeBuilder;
import user11681.mirror.reflect.Constructors;

import static user11681.mirror.Mirror.LOGGER;
import static user11681.mirror.Mirror.theUnsafe;

public class Test extends TestCase implements Opcodes {
    public static final int COMPUTE_ALL = ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;

    public static void register(final Class<?> holder) {
    }

    public static long normalize(final int integer) {
        if (integer >= 0) {
            return integer;
        }

        return ~0L >>> 32 & integer;
    }

    public static void println(final Object... values) {
        System.out.println(Arrays.deepToString(values));
    }

    public static void printfln(final String format, final Object... arguments) {
        System.out.printf(format + "\n", arguments);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> defineClass(final String name, final byte[] klass) {
        return (Class<T>) theUnsafe.defineClass(name, klass, 0, klass.length, null, null);
    }

    public static <T> Class<T> implement(String subclassPath, String... superPaths) {
        final String binaryName = subclassPath.replace('/', '.');
        subclassPath = subclassPath.replace('.', '/');

        for (int i = 0; i < superPaths.length; i++) {
            superPaths[i] = superPaths[i].replace('.', '/');
        }

        final ClassWriter writer = new ClassWriter(COMPUTE_ALL);

        writer.visit(V1_8, ACC_PUBLIC, subclassPath, null, "java/lang/Object", superPaths);

        final MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);

        final Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);

        final Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLocalVariable("this", "L" + subclassPath + ";", null, label0, label1, 0);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        writer.visitEnd();

        return defineClass(binaryName, writer.toByteArray());
    }

    public void test() throws Throwable {
        final String classPath = "user11681/mirror/StatefulInterface";
        final Class<?> interfase = this.getInterface(classPath);
        final Field field = interfase.getDeclaredField("interfaceField");
        final Class<?> subclass = implement("user11681/mirror/StatefulImplementation", classPath);
        final Object instance = theUnsafe.allocateInstance(subclass);
        final Object other = theUnsafe.allocateInstance(subclass);

        LOGGER.warn(field.get(instance));
        LOGGER.warn(field.get(other));
        field.setInt(instance, 11223344);
        field.setInt(other, 44332211);
        LOGGER.warn(field.get(instance));
        LOGGER.warn(field.get(other));
    }

    public Class<?> getInterface(final String classPath) throws Throwable {
        final String binaryName = classPath.replace('/', '.');
        final ClassWriter writer = new ClassWriter(COMPUTE_ALL);
        final OutputStream output = new FileOutputStream(new File(this.getRoot(), classPath + ".class"));

        writer.visit(V1_8, ACC_PUBLIC, classPath, null, "java/lang/Object", null);

        final FieldVisitor fieldVisitor = writer.visitField(ACC_PUBLIC | ACC_ABSTRACT, "interfaceField", "I", null, null);
        fieldVisitor.visitEnd();

        final MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();

        final Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);

        final Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLocalVariable("this", "L" + classPath + ";", null, label0, label1, 0);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        writer.visitEnd();

        output.close();

        return defineClass(binaryName, writer.toByteArray());
    }

    public void oldTest() throws Throwable {
        final ClassReader reader = new ClassReader("user11681.mirror.TestEnum");
        final ClassWriter writer = new ClassWriter(reader, COMPUTE_ALL);
        final OutputStream output = new FileOutputStream(new File(this.getRoot(), "user11681/mirror/Test1.class"));

        reader.accept(writer, 0);
        reader.accept(new ClassPrinter(), 0);

        output.write(writer.toByteArray());
        output.close();

        for (int i = 0; i < 100; i++) {
            final TestEnum newValue = Constructors.addEnumInstance(TestEnum.class, String.valueOf(i).toUpperCase());

//            printfln("Added a new enum constant %s with ordinal %d.", newValue, newValue.ordinal());
        }

        final Class<? extends TestSubclass> testClass = new TypeBuilder()
                .publicAccess().superFlag()
                .name("user11681/mirror/SyntheticClass")
                .defaultConstructor()
//                .constructor().publicAccess().descriptor("(ILjava/lang/String;Luser11681/mirror/TestEnum;)V").then()
                .build();

//        final TestSubclass testInstance = (TestSubclass) testClass.getDeclaredConstructors()[0].newInstance(0, "nothing", TestEnum.ONE);
//        final TestSubclass testInstance = (TestSubclass) testClass.getDeclaredConstructors()[0].newInstance();
        final Object testInstance = theUnsafe.allocateInstance(testClass);

        LOGGER.error(testInstance.getClass());
    }

    public void newClass() throws Throwable {
        final Class<?> ExtendedTestEnum = new TypeBuilder()
                .publicAccess().superFlag().enumFlag().abstractFlag()
                .name("user11681/mirror/ExtendedTestEnum")
                .signature("Ljava/lang/Enum<Luser11681/mirror/ExtendedTestEnum;>;")
                .extend(TestEnum.class)
                .inner().name("user11681/mirror/ExtendedTestEnum$1").staticFlag().enumFlag().build()
                .field().publicAccess().finalFlag().staticFlag().enumFlag().name("SIX").descriptor("Luser11681/mirror/ExtendedTestEnum;").then()
                .field().privateAccess().finalFlag().staticFlag().syntheticFlag().name("$VALUES").descriptor("[Luser11681/mirror/ExtendedTestEnum;").then()
                .build();

/*
        final Class<?> ExtendedTestEnum$1 = new TypeBuilder()
                .finalFlag().superFlag().enumFlag()
                .name("user11681/mirror/ExtendedTestEnum$1")
                .extend(ExtendedTestEnum)
                .
*/

        final ClassWriter writer = new ClassWriter(0);

        writer.visit(0, 0, "", "", null, null);
        writer.visitMethod(0, "", "", null, null);
    }

    public File getRoot() throws Throwable {
        return new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
    }
}
