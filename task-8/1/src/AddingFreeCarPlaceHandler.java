public class AddingFreeCarPlaceHandler implements IPropertyAccessResponseHandler<CarPlace> {
    private final Autoservice autoservice = Autoservice.getInstance();

    @Override
    public void handleResponse(CarPlace carPlace) {
        autoservice.addCarPlace(carPlace);
    }
}
