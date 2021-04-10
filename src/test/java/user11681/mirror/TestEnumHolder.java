package user11681.mirror;

import user11681.mirror.asm.enumeration.EnumGenerator;
import user11681.mirror.asm.enumeration.EnumHolder;

@EnumHolder(TestEnum.class)
public abstract class TestEnumHolder {
    public static final TestEnum SYNTHETIC_CONSTANT = EnumGenerator.construct();

    public TestEnumHolder(final int thing) {

    }
}
