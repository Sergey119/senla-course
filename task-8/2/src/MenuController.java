import java.util.Scanner;

@Singleton
public class MenuController {
    @Inject
    private Builder builder;

    @Inject
    private Navigator navigator;

    public void initialize() {
        Scanner scanner = new Scanner(System.in);

        this.builder.buildMenu();

        this.navigator.setCurrentMenu(this.builder.getRootMenu());
        this.navigator.setScanner(scanner);
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
                    System.out.println("Invalid input. Please enter a number.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }
}