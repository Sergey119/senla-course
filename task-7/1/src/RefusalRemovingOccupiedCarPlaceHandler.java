public class RefusalRemovingOccupiedCarPlaceHandler implements IPropertyAccessResponseHandler<Integer> {
    @Override
    public void handleResponse(Integer arg) {
        throw new UnsupportedOperationException("Access to removing " +
                "free car places in autoservice is prohibited.");
    }
}
