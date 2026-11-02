import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class ConfigLoader {

    private static final String NULL_LITERAL = "null";
    private static final int QUOTES_LENGTH = 2;
    private static final int QUOTE_TRIM_INDEX = 1;
    private static final int PAIR_SPLIT_LIMIT = 2;

    private ConfigLoader() {
    }

    public static AppConfig load(String path) throws IOException {
        Path file = Paths.get(path);
        checkFile(file);

        String content = Files.readString(file, StandardCharsets.UTF_8);
        Map<String, String> raw = parseFlatJson(content);

        return buildConfig(raw);
    }

    private static void checkFile(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new IOException("Файл конфигурации не найден: " + file);
        }
        if (!Files.isRegularFile(file)) {
            throw new IOException("Путь не является файлом: " + file);
        }
        if (!Files.isReadable(file)) {
            throw new IOException("Файл недоступен для чтения: " + file);
        }
    }

    private static AppConfig buildConfig(Map<String, String> raw) {
        return new AppConfig(
                raw.get("packageName"),
                raw.get("repositoryUrl"),
                raw.get("testRepositoryPath"),
                Boolean.parseBoolean(raw.getOrDefault("testMode", "false")),
                raw.get("packageVersion"),
                raw.get("filterSubstring")
        );
    }

    private static Map<String, String> parseFlatJson(String json) {
        Map<String, String> result = new HashMap<>();
        String trimmed = json.trim();
        checkBraces(trimmed);

        String body = trimmed.substring(QUOTE_TRIM_INDEX, trimmed.length() - QUOTE_TRIM_INDEX).trim();
        if (body.isEmpty()) {
            return result;
        }

        for (String pair : body.split(",")) {
            parsePair(pair, result);
        }
        return result;
    }

    private static void checkBraces(String text) {
        if (!text.startsWith("{") || !text.endsWith("}")) {
            throw new IllegalArgumentException(
                    "JSON должен начинаться с '{' и заканчиваться '}'");
        }
    }

    private static void parsePair(String pair, Map<String, String> target) {
        String[] kv = pair.split(":", PAIR_SPLIT_LIMIT);
        if (kv.length != PAIR_SPLIT_LIMIT) {
            throw new IllegalArgumentException("Некорректная пара ключ-значение: " + pair);
        }

        String key = stripQuotes(kv[0]);
        String value = stripQuotes(kv[1]);

        if (NULL_LITERAL.equals(value)) {
            value = null;
        }
        target.put(key, value);
    }

    private static String stripQuotes(String value) {
        String cleaned = value.trim();
        if (cleaned.length() >= QUOTES_LENGTH && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(QUOTE_TRIM_INDEX, cleaned.length() - QUOTE_TRIM_INDEX);
        }
        return cleaned;
    }
}