public class NextMenuAvailabilityHandler implements INextMenuResponseHandler{
    @Override
    public Menu handleResponse(Menu currentMenu, Integer choice) {
        currentMenu = currentMenu.getMenuItem(choice).getNextMenu();
        System.out.println("Transition to: " + currentMenu.getName());
        return currentMenu;
    }
}
