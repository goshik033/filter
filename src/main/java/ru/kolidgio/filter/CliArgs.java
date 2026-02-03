package ru.kolidgio.filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CliArgs {
    public final Path outDir;
    public final String prefix;
    public final boolean append;
    public final StatsMode statsMode;
    public final List<String> inputFiles;

    private CliArgs(Path outDir, String prefix, boolean append, StatsMode statsMode, List<String> inputFiles) {
        this.outDir = outDir;
        this.prefix = prefix;
        this.append = append;
        this.statsMode = statsMode;
        this.inputFiles = inputFiles;
    }

    public static CliArgs parse(String[] args) {
        Path outDir = Path.of(".");
        String prefix = "";
        boolean append = false;
        StatsMode statsMode = StatsMode.NONE;
        List<String> inputFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String a = args[i];

            switch (a) {
                case "-o" -> {
                    String value = requireValue(args, i, "-o");
                    i++;
                    Path candidate = parseDir(value, "-o");
                    outDir = candidate;
                }
                case "-p" -> {
                    String value = requireValue(args, i, "-p");
                    i++;
                    prefix = value;
                }
                case "-a" -> append = true;
                case "-s" -> {
                    if (statsMode != StatsMode.FULL) statsMode = StatsMode.SHORT;
                }
                case "-f" -> statsMode = StatsMode.FULL;
                default -> {
                    if (a.startsWith("-")) {
                        throw new IllegalArgumentException("Неизвестная опция: " + a);
                    }
                    inputFiles.add(a);
                }
            }
        }

        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Не указаны входные файлы");
        }

        return new CliArgs(outDir, prefix, append, statsMode, List.copyOf(inputFiles));
    }

    private static String requireValue(String[] args, int i, String optName) {
        if (i + 1 >= args.length) {
            throw new IllegalArgumentException("Опция " + optName + " требует значение");
        }
        String value = args[i + 1];
        if (isKnownOption(value)) {
            throw new IllegalArgumentException("Опция " + optName + " требует значение, но получена опция: " + value);
        }
        return value;
    }

    private static boolean isKnownOption(String s) {
        return s.equals("-o") || s.equals("-p") || s.equals("-a") || s.equals("-s") || s.equals("-f");
    }

    private static Path parseDir(String raw, String optName) {
        final Path p;
        try {
            p = Path.of(raw).toAbsolutePath().normalize();
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный путь для " + optName + ": " + raw);
        }

        if (Files.exists(p) && !Files.isDirectory(p)) {
            throw new IllegalArgumentException("Путь для " + optName + " не является директорией: " + p);
        }

        try {
            Files.createDirectories(p);
        } catch (Exception e) {
            throw new IllegalArgumentException("Невозможно использовать директорию " + p + ": " + e.getMessage());
        }

        return p;
    }
}