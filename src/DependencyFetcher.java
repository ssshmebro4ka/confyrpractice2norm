import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyFetcher {
    private static final Pattern CRATE_ID_PATTERN = Pattern.compile("\"crate_id\"\\s*:\\s*\"([^\"]+)\"");

    public static PackageInfo fetchDependencies(String packageName, String version) throws IOException, InterruptedException {
        String url = String.format("https://crates.io/api/v1/crates/%s/%s/dependencies", packageName, version);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "KonfypPractice-App")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Не удалось получить данные из репозитория. Код ответа: " + response.statusCode());
        }

        return parseDependencies(response.body(), packageName, version);
    }

    private static PackageInfo parseDependencies(String jsonResponse, String packageName, String version) {
        PackageInfo packageInfo = new PackageInfo(packageName, version);
        java.util.Set<String> uniqueDeps = new java.util.HashSet<>();

        Matcher matcher = CRATE_ID_PATTERN.matcher(jsonResponse);
        while (matcher.find()) {
            uniqueDeps.add(matcher.group(1));
        }

        for (String dep : uniqueDeps) {
            packageInfo.addDependency(dep);
        }

        return packageInfo;
    }
}