import java.io.IOException;
import java.util.Set;

public final class Main {
    private static final String DEFAULT_CONFIG_PATH = "config.json";
    private static final String NULL_PLACEHOLDER = "(не задано)";
    private static final String HEADER = "=== Параметры конфигурации ===";
    private static final String FOOTER = "==============================";
    private static final String FORMAT = "%-20s = %s%n";
    private static final int EXIT_ERROR = 1;

    private static final int DEFAULT_MAX_DEPTH = 5;
    private static final String TEST_START_PACKAGE = "A";

    private Main() {
    }

    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : DEFAULT_CONFIG_PATH;

        try {
            AppConfig config = ConfigLoader.load(configPath);
            ConfigValidator.validate(config);
            printConfig(config);

            runStages(config);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(EXIT_ERROR);
        }
    }

    private static void runStages(AppConfig config) {
        System.out.println("\n--- Построение графа зависимостей ---");

        try {

            DependencyGraph graph = buildGraph(config);

            String startPackage = config.isTestMode() ? TEST_START_PACKAGE : config.getPackageName();

            GraphBuilder builder = new GraphBuilder(graph, config.getFilterSubstring(), DEFAULT_MAX_DEPTH);
            Set<String> result;

            if (config.isReverseMode()) {
                System.out.println("Направление: Обратные зависимости (кто зависит от пакета)");
                result = builder.buildReverseGraph(startPackage);
            } else {
                System.out.println("Направление: Прямые зависимости (BFS)");
                result = builder.buildGraph(startPackage);
            }

            printResult(result, config.isReverseMode());

            if (config.isVisualizeMode()) {
                System.out.println("\n--- Текстовое представление графа (Graphviz/DOT) ---");
                String dotCode = GraphvizExporter.generateDot(graph, result);
                System.out.println(dotCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при получении зависимостей: " + e.getMessage());
        }
    }

    private static DependencyGraph buildGraph(AppConfig config) throws IOException, InterruptedException {
        DependencyGraph graph = new DependencyGraph();

        if (config.isTestMode()) {
            System.out.println("Режим: Тестирование (чтение из файла)");
            if (config.getTestRepositoryPath() == null || config.getTestRepositoryPath().isBlank()) {
                throw new IllegalArgumentException("Для тестового режима укажите testRepositoryPath");
            }
            return GraphBuilder.loadTestGraph(config.getTestRepositoryPath());
        } else {
            System.out.println("Режим: Реальный запрос к crates.io");
            PackageInfo info = DependencyFetcher.fetchDependencies(
                    config.getPackageName(), config.getPackageVersion()
            );
            for (String dep : info.getDependencies()) {
                graph.addEdge(config.getPackageName(), dep);
            }
            return graph;
        }
    }

    private static void printResult(Set<String> result, boolean isReverse) {
        if (isReverse) {
            System.out.println("\nНайденные обратные зависимости (кто зависит от пакета):");
        } else {
            System.out.println("\nНайденные прямые зависимости (BFS с рекурсией):");
        }

        if (result.isEmpty()) {
            System.out.println("Зависимости не найдены.");
        } else {
            for (String pkg : result) {
                System.out.println("- " + pkg);
            }
        }
    }

    private static void printConfig(AppConfig config) {
        System.out.println(HEADER);
        printPair("packageName", config.getPackageName());
        printPair("repositoryUrl", config.getRepositoryUrl());
        printPair("testRepositoryPath", config.getTestRepositoryPath());
        printPair("testMode", String.valueOf(config.isTestMode()));
        printPair("packageVersion", config.getPackageVersion());
        printPair("filterSubstring", config.getFilterSubstring());
        printPair("reverseMode", String.valueOf(config.isReverseMode()));
        printPair("visualizeMode", String.valueOf(config.isVisualizeMode()));
        System.out.println(FOOTER);
    }

    private static void printPair(String key, String value) {
        String display = (value == null || value.isBlank()) ? NULL_PLACEHOLDER : value;
        System.out.printf(FORMAT, key, display);
    }
}