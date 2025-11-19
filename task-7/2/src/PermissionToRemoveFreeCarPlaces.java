public class PermissionToRemoveFreeCarPlaces implements IPropertyAccessResponseHandler<Integer> {
    @Override
    public void handleResponse(Integer integer) {
        var handler = new RemovingFreeCarPlaceHandler();
        handler.handleResponse(integer);
    }
}
