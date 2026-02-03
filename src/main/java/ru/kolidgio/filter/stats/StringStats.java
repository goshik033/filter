package ru.kolidgio.filter.stats;

public final class StringStats {
    private long count = 0;
    private Integer minLen = null;
    private Integer maxLen = null;

    public void add(String s) {
        count++;
        int len = s.length();
        if (minLen == null || len < minLen) minLen = len;
        if (maxLen == null || len > maxLen) maxLen = len;
    }

    public String render(StatsMode mode) {
        if (mode == StatsMode.NONE) return "";
        if (count == 0) return "strings: 0";
        if (mode == StatsMode.SHORT) return "strings: " + count;

        return "strings: count=" + count +
                ", minLen=" + minLen +
                ", maxLen=" + maxLen;
    }
}