package de.muenchen.stadtbezirksbudget.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public final class LogUtils {
    private static final int MAX_LEN = 4096;
    private static final Pattern CONTROL = Pattern.compile("[\\r\\n\\t\\x00-\\x1F\\x7F]");
    private static final Pattern NON_ASCII_PRINTABLE = Pattern.compile("[^\\x20-\\x7E]");

    private LogUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String sanitizeForLog(final String input) {
        if (input == null) {
            return "";
        }
        String s = input;
        s = CONTROL.matcher(s).replaceAll("_");
        s = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'");
        s = NON_ASCII_PRINTABLE.matcher(s).replaceAll("?");
        if (s.length() > MAX_LEN) {
            s = s.substring(0, MAX_LEN) + "...";
        }
        return s;
    }

    public static String sanitizeObjectForLog(final Object object) {
        try {
            return sanitizeForLog(Objects.toString(object, "<null>"));
        } catch (Exception ex) {
            return "<unprintable>";
        }
    }
}
