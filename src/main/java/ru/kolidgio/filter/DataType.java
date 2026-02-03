package ru.kolidgio.filter;

public enum DataType {
    INTEGERS("integers.txt"),
    FLOATS("floats.txt"),
    STRINGS("strings.txt");

    public final String defaultName;

    DataType(String defaultName) {
        this.defaultName = defaultName;
    }
}