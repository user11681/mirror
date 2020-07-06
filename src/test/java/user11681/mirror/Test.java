package user11681.mirror;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import junit.framework.TestCase;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import sun.misc.Unsafe;
import user11681.mirror.asm.TypeBuilder;
import user11681.mirror.reflect.Constructors;
import user11681.mirror.reflect.Mirror;

public class Test extends TestCase {
    public static final Unsafe theUnsafe = Mirror.theUnsafe;

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

    public void test() throws Throwable {
        final ClassReader reader = new ClassReader("user11681.mirror.TestEnum");
        final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        final OutputStream output = new FileOutputStream(new File(this.getRoot(), "user11681/mirror/Test1.class"));

        reader.accept(writer, 0);
        reader.accept(new ClassPrinter(), 0);

        output.write(writer.toByteArray());
        output.close();

        for (int i = 0; i < 100; i++) {
            final TestEnum newValue = Constructors.addEnumInstance(TestEnum.class, String.valueOf(i).toUpperCase());

            printfln("Added a new enum constant %s with ordinal %d.", newValue, newValue.ordinal());
        }
    }

    public void newClass() throws Throwable {
        final Class<?> ExtendedTestEnum = new TypeBuilder()
                .publicAccess().superFlag().enumFlag().abstractFlag()
                .name("user11681/mirror/ExtendedTestEnum")
                .signature("Ljava/lang/Enum<Luser11681/mirror/ExtendedTestEnum;>;")
                .extend(TestEnum.class)
                .inner().name("user11681/mirror/ExtendedTestEnum$1").staticFlag().enumFlag().build()
                .field().publicAccess().finalFlag().staticFlag().enumFlag().name("SIX").descriptor("Luser11681/mirror/ExtendedTestEnum;").build()
                .field().privateAccess().finalFlag().staticFlag().syntheticFlag().name("$VALUES").descriptor("L[user11681/mirror/ExtendedTestEnum;").build()
                .build();

/*
        final Class<?> ExtendedTestEnum$1 = new TypeBuilder()
                .finalFlag().superFlag().enumFlag()
                .name("user11681/mirror/ExtendedTestEnum$1")
                .extend(ExtendedTestEnum)
                .
*/

//        final ClassWriter writer = new ClassWriter(0);
//
//        writer.visitMethod(0, null, null, null, null);
    }

    public File getRoot() throws Throwable {
        return new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
    }
}
