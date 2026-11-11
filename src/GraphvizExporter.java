import java.util.Map;
import java.util.Set;

public class GraphvizExporter {

    public static String generateDot(DependencyGraph graph, Set<String> nodesToRender) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Dependencies {\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=box, style=filled, color=lightblue];\n");

        for (String node : nodesToRender) {
            sb.append(String.format("    \"%s\";\n", node));
        }

        for (String node : nodesToRender) {
            Set<String> deps = graph.getDependencies(node);
            for (String dep : deps) {

                if (nodesToRender.contains(dep) && !node.equals(dep)) {
                    sb.append(String.format("    \"%s\" -> \"%s\";\n", node, dep));
                }
            }
        }

        sb.append("}\n");
        return sb.toString();
    }
}