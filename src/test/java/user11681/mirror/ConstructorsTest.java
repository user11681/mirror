package user11681.mirror;

import junit.framework.TestCase;

public class ConstructorsTest extends TestCase {
    public static final TestEnum FOUR = Constructors.newEnumInstance(TestEnum.values(), "FOUR", 4);

    public void test() {
        System.out.println(FOUR.getNumber());
        System.out.println(FOUR.getSquared());
    }
}