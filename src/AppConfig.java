public final class AppConfig {

    private final String packageName;
    private final String repositoryUrl;
    private final String testRepositoryPath;
    private final boolean testMode;
    private final String packageVersion;
    private final String filterSubstring;

    public AppConfig(String packageName, String repositoryUrl, String testRepositoryPath,
                     boolean testMode, String packageVersion, String filterSubstring) {
        this.packageName = packageName;
        this.repositoryUrl = repositoryUrl;
        this.testRepositoryPath = testRepositoryPath;
        this.testMode = testMode;
        this.packageVersion = packageVersion;
        this.filterSubstring = filterSubstring;
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
}