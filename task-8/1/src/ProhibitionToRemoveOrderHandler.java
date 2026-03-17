public class ProhibitionToRemoveOrderHandler implements IPropertyAccessResponseHandler<Integer>{
    @Override
    public void handleResponse(Integer arg) {
        throw new UnsupportedOperationException("Access to remove order is prohibited.");
    }
}
