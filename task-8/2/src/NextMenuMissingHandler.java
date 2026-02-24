import java.util.HashMap;

public class NextMenuMissingHandler implements INextMenuResponseHandler{
    @Override
    public Menu handleResponse(Menu currentMenu, Integer choice) {
        var strategyHandlers = new HashMap<IAction, ISelectedItemHandler>();
        strategyHandlers.put(null, new SelectedItemMissingHandler());

        var responseHandler = strategyHandlers.getOrDefault(currentMenu.getMenuItem(choice).getAction(),
                new SelectedItemAvailabilityHandler());
        responseHandler.handleResponse(currentMenu.getMenuItem(choice));

        return currentMenu;
    }
}
