package user11681.mirror;

public enum TestEnum {
    ONE(1, 1),
    TWO(1, 2),
    THREE(1);

    private final int number;
    private final int squared;

    TestEnum(final int number) {
        this(number, number * number);
    }

    TestEnum(final int number, final int squared) {
        this.number = number;
        this.squared = squared;
    }

    public final int getNumber() {
        return this.number;
    }

    public final int getSquared() {
        return this.squared;
    }
}
