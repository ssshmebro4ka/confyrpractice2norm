import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void loadValidConfig() throws IOException {
        Path file = tempDir.resolve("config.json");
        Files.writeString(file, "{\n"
                + "  \"packageName\": \"express\",\n"
                + "  \"repositoryUrl\": \"https://registry.npmjs.org\",\n"
                + "  \"testMode\": false,\n"
                + "  \"packageVersion\": \"4.18.2\",\n"
                + "  \"filterSubstring\": \"types\"\n"
                + "}");

        AppConfig config = ConfigLoader.load(file.toString());

        assertEquals("express", config.getPackageName());
        assertEquals("https://registry.npmjs.org", config.getRepositoryUrl());
        assertEquals("4.18.2", config.getPackageVersion());
    }

    @Test
    void loadMissingFileThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigLoader.load("no-such-file.json"));
    }

    @Test
    void loadBrokenJsonThrows() throws IOException {
        Path file = tempDir.resolve("broken.json");
        Files.writeString(file, "not a json at all");

        assertThrows(IllegalArgumentException.class,
                () -> ConfigLoader.load(file.toString()));
    }

    @Test
    void validConfigPassesValidation() {
        AppConfig cfg = new AppConfig("express", "https://registry.npmjs.org",
                null, false, "4.18.2", "types");

        assertDoesNotThrow(() -> ConfigValidator.validate(cfg));
    }

    @Test
    void emptyPackageNameFails() {
        AppConfig cfg = new AppConfig("", "https://x", null, false, "1.0.0", null);
        assertThrows(IllegalArgumentException.class, () -> ConfigValidator.validate(cfg));
    }

    @Test
    void badVersionFails() {
        AppConfig cfg = new AppConfig("express", "https://x", null, false, "abc", null);
        assertThrows(IllegalArgumentException.class, () -> ConfigValidator.validate(cfg));
    }

    @Test
    void noRepositoryFails() {
        AppConfig cfg = new AppConfig("express", null, null, false, "1.0.0", null);
        assertThrows(IllegalArgumentException.class, () -> ConfigValidator.validate(cfg));
    }

    @Test
    void testModeWithoutPathFails() {
        AppConfig cfg = new AppConfig("express", "https://x", null, true, "1.0.0", null);
        assertThrows(IllegalArgumentException.class, () -> ConfigValidator.validate(cfg));
    }
}