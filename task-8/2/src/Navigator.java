import java.util.HashMap;
import java.util.Scanner;
public class Navigator {
    @Inject
    private Menu currentMenu;

    private Scanner scanner;

    public Navigator() { }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMenu() {
        if (currentMenu != null) {
            currentMenu.displayMenu();
        } else {
            System.out.println("No menu set!");
        }
    }

    public void navigate(Integer choice) {
        var strategyHandlers = new HashMap<Boolean, IMenuItemResponseHandler>();
        strategyHandlers.put(false, new MenuItemMissingHandler());
        strategyHandlers.put(true, new MenuItemAvailabilityHandler());

        var responseHandler = strategyHandlers.get(currentMenu.containsKey(choice));
        currentMenu = responseHandler.handleResponse(currentMenu, choice);
    }

    public void setCurrentMenu(Menu menu) { this.currentMenu = menu; }
    public Menu getCurrentMenu() { return currentMenu; }

    public Scanner getScanner() { return scanner; }
}