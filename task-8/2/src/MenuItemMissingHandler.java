public class MenuItemMissingHandler implements IMenuItemResponseHandler {
    @Override
    public Menu handleResponse(Menu currentMenu, Integer choice) {
        System.out.println("\"" + choice + "\" is invalid menu item! Available options: " +
                (currentMenu != null ? currentMenu.getMenuItems().keySet() : "none"));
        return currentMenu;
    }
}
