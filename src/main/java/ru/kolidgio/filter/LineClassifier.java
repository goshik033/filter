package ru.kolidgio.filter;

import java.math.BigDecimal;

public final class LineClassifier {

    public DataType classify(String line) {
        String t = line.trim();
        if (t.isEmpty()) return DataType.STRINGS;

        if (isIntegerFormat(t)) return DataType.INTEGERS;

        try {
            new BigDecimal(t);
            return DataType.FLOATS;
        } catch (NumberFormatException ignored) {
            return DataType.STRINGS;
        }
    }

    private static boolean isIntegerFormat(String t) {
        int i = 0;
        if (t.startsWith("+") || t.startsWith("-")) i = 1;
        if (i == t.length()) return false;

        for (; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}