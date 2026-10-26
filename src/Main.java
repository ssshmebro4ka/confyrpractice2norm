import java.io.IOException;

/**
 * Точка входа приложения.
 * Загружает конфигурацию, валидирует её и выводит параметры.
 */
public final class Main {

    private static final String DEFAULT_CONFIG_PATH = "config.json";
    private static final String NULL_PLACEHOLDER = "(не задано)";
    private static final String HEADER = "=== Параметры конфигурации ===";
    private static final String FOOTER = "==============================";
    private static final String FORMAT = "%-20s = %s%n";
    private static final int EXIT_ERROR = 1;

    private Main() {
    }


    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : DEFAULT_CONFIG_PATH;

        try {
            AppConfig config = ConfigLoader.load(configPath);
            ConfigValidator.validate(config);
            printConfig(config);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(EXIT_ERROR);
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
        System.out.println(FOOTER);
    }

    private static void printPair(String key, String value) {
        String display = (value == null || value.isBlank()) ? NULL_PLACEHOLDER : value;
        System.out.printf(FORMAT, key, display);
    }
}