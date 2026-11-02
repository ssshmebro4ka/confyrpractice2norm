import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphBuilder {

    private final Set<String> visited = new LinkedHashSet<>();
    private final DependencyGraph graph;
    private final String filterSubstring;
    private final int maxDepth;

    public GraphBuilder(DependencyGraph graph, String filterSubstring, int maxDepth) {
        this.graph = graph;
        this.filterSubstring = filterSubstring;
        this.maxDepth = maxDepth;
    }

    public Set<String> buildGraph(String startPackage) {
        visited.clear();
        bfsRecursive(startPackage, 0);
        return visited;
    }

    private void bfsRecursive(String currentPackage, int currentDepth) {
        if (currentDepth > maxDepth) {
            return;
        }

        if (filterSubstring != null && !filterSubstring.isBlank()
                && currentPackage.contains(filterSubstring)) {
            return;
        }

        if (visited.contains(currentPackage)) {
            return;
        }

        visited.add(currentPackage);

        for (String dependency : graph.getDependencies(currentPackage)) {
            bfsRecursive(dependency, currentDepth + 1);
        }
    }

    public static DependencyGraph loadTestGraph(String filePath) throws IOException {
        DependencyGraph testGraph = new DependencyGraph();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            if (line.isBlank() || !line.contains(":")) continue;

            String[] parts = line.split(":", 2);
            String mainPackage = parts[0].trim();
            String[] deps = parts[1].split(",");

            testGraph.addEdge(mainPackage, null); // Добавляем сам пакет
            for (String dep : deps) {
                if (!dep.trim().isEmpty()) {
                    testGraph.addEdge(mainPackage, dep.trim());
                }
            }
        }
        return testGraph;
    }
}