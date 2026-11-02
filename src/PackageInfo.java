import java.util.ArrayList;
import java.util.List;

public class PackageInfo {
    private final String name;
    private final String version;
    private final List<String> dependencies = new ArrayList<>();

    public PackageInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public void addDependency(String depName) {
        this.dependencies.add(depName);
    }

    public String getName() { return name; }
    public String getVersion() { return version; }
    public List<String> getDependencies() { return dependencies; }
}