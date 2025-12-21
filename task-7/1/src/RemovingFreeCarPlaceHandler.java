public class RemovingFreeCarPlaceHandler implements IPropertyAccessResponseHandler<Integer> {
    private final Autoservice autoservice = Autoservice.getInstance();

    @Override
    public void handleResponse(Integer integer) {
        autoservice.removeCarPlace(integer);
    }
}
