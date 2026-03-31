public class ConfigPropertiesDirectory {
    private static ConfigPropertiesDirectory instance;
    private static final String RESOURCES_DIR_PATH = "C:\\Users\\admin\\IdeaProjects\\senla-course\\task-8\\2\\stream\\";

    private ConfigPropertiesDirectory() {}
    public static synchronized ConfigPropertiesDirectory getInstance() {
        if (instance == null) {
            instance = new ConfigPropertiesDirectory();
        }
        return instance;
    }
    public String getPath() {
        return RESOURCES_DIR_PATH;
    }
}
