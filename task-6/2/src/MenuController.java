import java.util.Scanner;

public class MenuController {
    private Builder builder;
    private Navigator navigator;
    private static MenuController instance;

    private MenuController() {}

    public static synchronized MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void initialize() {
        Scanner scanner = new Scanner(System.in);
        this.builder = new Builder();
        this.builder.buildMenu();
        this.navigator = new Navigator(builder.getRootMenu(), scanner);
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                navigator.printMenu();
                String input = navigator.getScanner().nextLine().trim();

                if ("0".equals(input)) {
                    System.out.println("Exiting the program...");
                    running = false;
                    continue;
                }

                try {
                    int choice = Integer.parseInt(input);
                    navigator.navigate(choice);
                } catch (NumberFormatException e) {
                    System.out.println("Exception was thrown with number in MenuController: " + e.getMessage());
                }

            } catch (Exception e) {
                System.out.println("Exception was thrown in MenuController: " + e.getMessage());
            }
        }

        if (navigator.getScanner() != null) {
            navigator.getScanner().close();
        }
    }

}