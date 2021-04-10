package user11681.mirror.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class MirrorLogger {
    private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(":")
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(":")
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    public void info(final Object format, final Object... arguments) {
        this.print(Level.INFO, format, arguments);
    }

    public void warn(final Object format, final Object... arguments) {
        this.print(Level.WARN, format, arguments);
    }

    public void error(final Object format, final Object... arguments) {
        this.print(Level.ERROR, format, arguments);
    }

    private void print(final Level level, final Object format, final Object... arguments) {
        System.out.printf(String.format("%s[%s] [%s] (mirror) ", (char) 27 + level.color, formatTime(), level.string) + format + '\n', arguments);
    }

    private static String formatTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    private enum Level {
        INFO("[94m"),
        WARN("[33m"),
        ERROR("[31m");

        private final String string;
        private final String color;

        Level(final String color) {
            this.string = this.name();
            this.color = color;
        }
    }
}
