public class SelectedItemAvailabilityHandler implements ISelectedItemHandler {
    @Override
    public void handleResponse(MenuItem selectedItem) {
        selectedItem.doAction();
    }
}