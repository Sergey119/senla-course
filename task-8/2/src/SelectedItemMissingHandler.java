public class SelectedItemMissingHandler implements ISelectedItemHandler {
    @Override
    public void handleResponse(MenuItem selectedItem) {
        System.out.println("This menu item is not configured!");
    }
}
