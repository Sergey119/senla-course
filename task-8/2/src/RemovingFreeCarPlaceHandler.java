public class RemovingFreeCarPlaceHandler implements IPropertyAccessResponseHandler<Integer> {
    @Inject
    private Autoservice autoservice;

    public RemovingFreeCarPlaceHandler() {
        DIContainer.injectDependencies(this);
    }

    @Override
    public void handleResponse(Integer integer) {
        autoservice.removeCarPlace(integer);
    }
}
