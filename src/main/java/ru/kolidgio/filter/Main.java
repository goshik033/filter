package ru.kolidgio.filter;

public final class Main {
    public static void main(String[] args) {
        try {
            CliArgs cli = CliArgs.parse(args);

            System.out.println("outDir=" + cli.outDir);
            System.out.println("prefix=" + cli.prefix);
            System.out.println("append=" + cli.append);
            System.out.println("statsMode=" + cli.statsMode);
            System.out.println("inputFiles=" + cli.inputFiles);

        } catch (Exception e) {
            System.err.println("Ошибка аргументов: " + e.getMessage());
            System.err.println("Пример: java -jar target/filter-1.0-SNAPSHOT.jar -s -a -p sample- in1.txt in2.txt");
            System.exit(2);
        }
    }
}