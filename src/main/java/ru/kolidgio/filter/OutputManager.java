package ru.kolidgio.filter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

public final class OutputManager implements AutoCloseable {
    private final Path outDir;
    private final String prefix;
    private final boolean append;
    private final Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);

    public OutputManager(Path outDir, String prefix, boolean append) {
        this.outDir = (outDir == null) ? Path.of(".") : outDir;
        this.prefix = (prefix == null) ? "" : prefix;
        this.append = append;
    }

    public void write(DataType type, String line) throws IOException {
        BufferedWriter w = writers.get(type);
        if (w == null) {
            Files.createDirectories(outDir);
            Path file = outDir.resolve(prefix + type.defaultName);

            OpenOption[] opts = append
                    ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND}
                    : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

            w = Files.newBufferedWriter(file, StandardCharsets.UTF_8, opts);
            writers.put(type, w);
        }

        w.write(line);
        w.newLine();
    }

    @Override
    public void close() {
        for (BufferedWriter w : writers.values()) {
            try {
                w.close();
            } catch (IOException e) {
                System.err.println("Не удалось закрыть файл: " + e.getMessage());
            }
        }
        writers.clear();
    }
}