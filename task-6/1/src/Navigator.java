import java.io.IOException;
import java.util.Scanner;

public class Navigator {
    private Menu currentMenu;
    private final Scanner scanner;

    public Navigator(Menu initialMenu, Scanner scanner) {
        this.currentMenu = initialMenu;
        this.scanner = scanner;
    }

    public void printMenu() {
        if (currentMenu != null) {
            currentMenu.displayMenu();
        }
    }

    public void navigate(Integer choice) throws IOException {
        if (currentMenu.containsKey(choice)) {
            MenuItem selectedItem = currentMenu.getMenuItem(choice);
            if (selectedItem != null) { // переход на новое меню
                Menu nextMenu = selectedItem.getNextMenu();
                if (nextMenu != null) {
                    currentMenu = nextMenu;
                    System.out.println("Transition to: " + currentMenu.getName());
                } else if (selectedItem.getAction() != null) {  // выполнение действия
                    selectedItem.doAction();
                } else {    // если нет нового меню или действия, значит ошибка
                    System.out.println("This menu item is not configured!");
                }
            }
        } else {
            System.out.println("Invalid menu item! Available options: " + currentMenu.getMenuItems().keySet());
        }
    }

    public void setCurrentMenu(Menu menu) { this.currentMenu = menu; }
    public Menu getCurrentMenu() { return currentMenu; }

    public Scanner getScanner() {  return scanner; }
}