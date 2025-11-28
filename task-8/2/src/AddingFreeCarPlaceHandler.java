public class AddingFreeCarPlaceHandler implements IPropertyAccessResponseHandler<CarPlace> {
    @Inject
    private Autoservice autoservice;

    public AddingFreeCarPlaceHandler() {
        DIContainer.injectDependencies(this);
    }

    @Override
    public void handleResponse(CarPlace carPlace) {
        autoservice.addCarPlace(carPlace);
    }
}
