package user11681.mirror;

import java.util.Arrays;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassPrinter extends ClassVisitor {
    public ClassPrinter() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        System.out.printf("Visiting %s extends %s.\n", name, superName);
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        System.out.printf("Visiting field %s %s %s = %s.\n", signature, descriptor, name, value);

        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        System.out.printf("Visiting method %s %s%s throws %s.\n", signature, name, descriptor, Arrays.toString(exceptions));

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
