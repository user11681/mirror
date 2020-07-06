package user11681.mirror.asm;

import javax.annotation.Nonnull;

public interface EnumGenerator {
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    static <T extends Enum<T>> T construct() {
        return null;
    }
}
