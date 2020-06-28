package user11681.mirror;

import java.lang.reflect.Modifier;

public class Modifiers {
    public static final int ENUM = 0x4000;
    public static final int ENUM_FIELD = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL | ENUM;

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
