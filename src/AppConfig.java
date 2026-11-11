public final class AppConfig {

    private final String packageName;
    private final String repositoryUrl;
    private final String testRepositoryPath;
    private final boolean testMode;
    private final String packageVersion;
    private final String filterSubstring;
    private final boolean reverseMode;
    private final boolean visualizeMode;

    public AppConfig(String packageName, String repositoryUrl, String testRepositoryPath,
                     boolean testMode, String packageVersion, String filterSubstring,
                     boolean reverseMode, boolean visualizeMode) {
        this.packageName = packageName;
        this.repositoryUrl = repositoryUrl;
        this.testRepositoryPath = testRepositoryPath;
        this.testMode = testMode;
        this.packageVersion = packageVersion;
        this.filterSubstring = filterSubstring;
        this.reverseMode = reverseMode;
        this.visualizeMode = visualizeMode;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getTestRepositoryPath() {
        return testRepositoryPath;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public String getFilterSubstring() {
        return filterSubstring;
    }

    public boolean isReverseMode() {
        return reverseMode;
    }
    public boolean isVisualizeMode() {
        return visualizeMode;
    }
}