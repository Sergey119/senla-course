public class RefusalAddingOccupiedCarPlaceHandler implements IPropertyAccessResponseHandler<CarPlace>{
    @Override
    public void handleResponse(CarPlace carPlace) {
        throw new UnsupportedOperationException("Access to adding " +
                "free car places in autoservice is prohibited.");
    }
}
