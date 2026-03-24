public class PermissionToAddFreeCarPlaces implements IPropertyAccessResponseHandler<CarPlace> {

    @Override
    public void handleResponse(CarPlace carPlace) {
        var handler = new AddingFreeCarPlaceHandler();
        handler.handleResponse(carPlace);
    }
}
