public final class ConfigValidator {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 214;
    private static final int MAX_FILTER_LENGTH = 100;

    private ConfigValidator() {

    }

    public static void validate(AppConfig config) {
        validatePackageName(config.getPackageName());
        validateRepository(config);
        validateVersion(config.getPackageVersion());
        validateFilter(config.getFilterSubstring());
    }

    private static void validatePackageName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Параметр 'packageName' не задан или пуст");
        }
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Длина 'packageName' должна быть от "
                    + MIN_NAME_LENGTH + " до " + MAX_NAME_LENGTH + " символов");
        }
        if (!name.matches("[a-zA-Z0-9._-]+")) {
            throw new IllegalArgumentException("'packageName' содержит недопустимые символы");
        }
    }

    private static void validateRepository(AppConfig config) {
        String url = config.getRepositoryUrl();
        String path = config.getTestRepositoryPath();

        if (isBlank(url) && isBlank(path)) {
            throw new IllegalArgumentException(
                    "Должен быть указан либо 'repositoryUrl', либо 'testRepositoryPath'");
        }
        if (!isBlank(url) && !(url.startsWith("http://") || url.startsWith("https://"))) {
            throw new IllegalArgumentException(
                    "'repositoryUrl' должен начинаться с http/https");
        }
        if (config.isTestMode() && isBlank(path)) {
            throw new IllegalArgumentException(
                    "В режиме testMode=true обязателен параметр 'testRepositoryPath'");
        }
    }

    private static void validateVersion(String version) {
        if (isBlank(version)) {
            throw new IllegalArgumentException("Параметр 'packageVersion' не задан");
        }
        if (!version.matches("\\d+(\\.\\d+)*")) {
            throw new IllegalArgumentException("'packageVersion' должен иметь вид X.Y.Z");
        }
    }

    private static void validateFilter(String filter) {
        if (filter != null && filter.length() > MAX_FILTER_LENGTH) {
            throw new IllegalArgumentException("'filterSubstring' слишком длинный (макс. "
                    + MAX_FILTER_LENGTH + ")");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}