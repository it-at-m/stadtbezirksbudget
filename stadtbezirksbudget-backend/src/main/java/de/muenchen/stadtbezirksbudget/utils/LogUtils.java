package de.muenchen.stadtbezirksbudget.utils;

public final class LogUtils {
    private LogUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String sanitizeForLog(final String input) {
        if (input == null) {
            return "";
        }
        return input
                .replaceAll("[\\r\\n\\t]", "_").replaceAll("[\\x00-\\x1F\\x7F]", "_") // Replace control characters (unprintable, line breaks, tabs, etc.)
                .replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'") // Escape quotes and backslashes, which can disrupt log format
                .replace("=", "_").replace(":", "_") // Replace other log delimiter characters (e.g., = or : )
                .replaceAll("[^\\x20-\\x7E]", "?"); // Replace non-ASCII printable with '?'
    }

    public static String sanitizeObjectForLog(final Object object) {
        if (object == null) {
            return "";
        }
        return sanitizeForLog(object.toString());
    }
}
