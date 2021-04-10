package user11681.mirror;

import sun.misc.Unsafe;
import user11681.mirror.asm.builder.TypeBuilder;
import user11681.mirror.logger.MirrorLogger;
import user11681.mirror.reflect.accessor.FieldAccessor;

public class Mirror {
    public static final Unsafe theUnsafe = FieldAccessor.get(Unsafe.class, "theUnsafe");
    public static final MirrorLogger LOGGER = new MirrorLogger();

    public static void main(final String[] args) {
        final Class<? extends Mirror> testClass = new TypeBuilder()
                .publicAccess().superFlag()
                .name("user11681.mirror.SyntheticClass")
                .extend(Mirror.class)
                .constructor().publicAccess().descriptor("(ILjava/lang/String;Luser11681/mirror/TestEnum;)V").then()
                .build();
    }
}
