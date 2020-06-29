package user11681.mirror;

public class Throwables {
    private static final FieldWrapper<String> detailMessage = new FieldWrapper<>(Throwable.class, "detailMessage");

    /**
     * format the detail message of a {@linkplain Throwable throwable}
     *
     * @param throwable an instance of {@link Throwable}
     * @param arguments the arguments to pass to {@link String#format} on the message of {@code throwable}
     * @param <T>       the type of {@code throwable}
     * @return {@code throwable}
     */
    public static <T extends Throwable> T format(final T throwable, final Object... arguments) {
        detailMessage.set(throwable, String.format(detailMessage.get(throwable), arguments));

        return throwable;
    }
}
