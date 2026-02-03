package ru.kolidgio.filter;

import ru.kolidgio.filter.stats.FloatStats;
import ru.kolidgio.filter.stats.IntegerStats;
import ru.kolidgio.filter.stats.StatsMode;
import ru.kolidgio.filter.stats.StringStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
    public static void main(String[] args) {
        int code = run(args);
        if (code != 0) System.exit(code);
    }

    private static int run(String[] args) {
        final CliArgs cli;
        try {
            cli = CliArgs.parse(args);
        } catch (Exception e) {
            System.err.println("Ошибка аргументов: " + e.getMessage());
            System.err.println("Пример: java -jar target/filter-1.0-SNAPSHOT.jar -s -a -p sample- in1.txt in2.txt");
            return 2;
        }

        LineClassifier classifier = new LineClassifier();

        IntegerStats intStats = new IntegerStats();
        FloatStats floatStats = new FloatStats();
        StringStats stringStats = new StringStats();

        try (OutputManager out = new OutputManager(cli.outDir, cli.prefix, cli.append)) {

            for (String fileName : cli.inputFiles) {
                Path p = Path.of(fileName);

                if (!Files.exists(p)) {
                    System.err.println("Файл не найден: " + fileName);
                    continue;
                }
                if (!Files.isRegularFile(p)) {
                    System.err.println("Не файл: " + fileName);
                    continue;
                }

                try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        DataType type = classifier.classify(line);
                        String trimmed = line.trim();
                        String toWrite = (type == DataType.STRINGS) ? line : trimmed;

                        try {
                            out.write(type, toWrite);
                        } catch (IOException e) {
                            System.err.println("Ошибка записи результата: " + e.getMessage());
                            return 1;
                        }

                        switch (type) {
                            case INTEGERS -> intStats.add(trimmed);
                            case FLOATS -> floatStats.add(trimmed);
                            case STRINGS -> stringStats.add(line);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла " + fileName + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Фатальная ошибка: " + e.getMessage());
            return 1;
        }

        if (cli.statsMode != StatsMode.NONE) {
            System.out.println(intStats.render(cli.statsMode));
            System.out.println(floatStats.render(cli.statsMode));
            System.out.println(stringStats.render(cli.statsMode));
        }

        return 0;
    }
}