package user11681.mirror;

class TestSubclass extends TestSuperclass {
    public int test;

    public TestSubclass() {
        this(0, null, null);
    }

    public TestSubclass(final int param0, final String param1, final TestEnum param2) {
        super(param0, param1, param2);
    }
}
