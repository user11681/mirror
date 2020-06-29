package user11681.mirror;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Modifiers extends Modifier {
    public static final int ENUM = 0x4000;
    public static final int ENUM_FIELD = PUBLIC | STATIC | FINAL | ENUM;

    public static boolean isEnumArrayField(final Field field, final Class<?> enumClass) {
        final int modifiers = field.getModifiers();

        return field.isSynthetic() && field.getType().getComponentType() == enumClass && isPrivate(modifiers) && isStatic(modifiers) && isFinal(modifiers);
    }

    public static boolean isEnum(final int modifiers) {
        return (modifiers & ENUM) != 0;
    }

    public static boolean isPublicStaticConstant(final int modifiers) {
        return Modifier.isPublic(modifiers) && isStaticConstant(modifiers);
    }

    public static boolean isStaticConstant(final int modifiers) {
        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }
}
