public class Main {
    public static void main(String[] args) {
        System.out.println("Launch of the car service system...");

        try {
            Autoservice autoservice = Autoservice.getInstance();
            autoservice.initializeDefaultData();

            MenuController menuController = MenuController.getInstance();
            menuController.initialize();

            menuController.run();
        } catch (Exception e) {
            System.err.println("Exception was thrown in Main: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("The auto service system has been completed.");
        }
    }
}