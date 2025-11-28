import java.util.HashMap;

public class MenuItemAvailabilityHandler implements IMenuItemResponseHandler {
    @Override
    public Menu handleResponse(Menu currentMenu, Integer choice) {
        var selectedItem = currentMenu.getMenuItem(choice);

        var strategyHandlers = new HashMap<Menu, INextMenuResponseHandler>();
        strategyHandlers.put(null, new NextMenuMissingHandler());

        var responseHandler = strategyHandlers.getOrDefault(selectedItem.getNextMenu(),
                new NextMenuAvailabilityHandler());
        return responseHandler.handleResponse(currentMenu, choice);
    }
}
