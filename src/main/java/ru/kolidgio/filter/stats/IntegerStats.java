package ru.kolidgio.filter.stats;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class IntegerStats {
    private long count = 0;
    private BigDecimal min = null;
    private BigDecimal max = null;
    private BigDecimal sum = BigDecimal.ZERO;

    public void add(String s) {
        BigDecimal v = new BigDecimal(s);
        count++;
        if (min == null || v.compareTo(min) < 0) min = v;
        if (max == null || v.compareTo(max) > 0) max = v;
        sum = sum.add(v);
    }

    public String render(StatsMode mode) {
        if (mode == StatsMode.NONE) return "";
        if (count == 0) return "integers: 0";
        if (mode == StatsMode.SHORT) return "integers: " + count;

        BigDecimal avg = sum.divide(BigDecimal.valueOf(count), 16, RoundingMode.HALF_UP).stripTrailingZeros();
        return "integers: count=" + count +
                ", min=" + min.toPlainString() +
                ", max=" + max.toPlainString() +
                ", sum=" + sum.toPlainString() +
                ", avg=" + avg.toPlainString();
    }
}