public class Main {
    public static void main(String[] args) {
        System.out.println("Launch of the car service system...");

        try {
            loadApplicationConfiguration();

            AutoserviceManager autoserviceManager = DIContainer.resolve(AutoserviceManager.class);
            autoserviceManager.loadData();

            MenuController menuController = DIContainer.resolve(MenuController.class);
            menuController.initialize();
            menuController.run();

            autoserviceManager.saveData();

        } catch (Exception e) {
            System.err.println("Error in Main: " + e.getMessage());
        } finally {
            System.out.println("The auto service system has been completed.");
        }
    }

    private static void loadApplicationConfiguration() {
        try {
            AutoserviceConfig config = DIContainer.resolve(AutoserviceConfig.class);
            ConfigurationInjector.inject(config);
            System.out.println("Application configuration loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading application configuration: " + e.getMessage());
            System.out.println("Using default configuration settings");
        }
    }
}