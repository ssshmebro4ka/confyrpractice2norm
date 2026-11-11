import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyGraph {
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    public void addEdge(String from, String to) {
        adjacencyList.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        adjacencyList.computeIfAbsent(to, k -> new HashSet<>());
    }

    public Set<String> getDependencies(String packageName) {
        return adjacencyList.getOrDefault(packageName, new HashSet<>());
    }

    public boolean containsNode(String packageName) {
        return adjacencyList.containsKey(packageName);
    }

    public Set<String> getReverseDependencies(String targetPackage) {
        Set<String> reverseDeps = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
            if (entry.getValue().contains(targetPackage)) {
                reverseDeps.add(entry.getKey());
            }
        }
        return reverseDeps;
    }
}