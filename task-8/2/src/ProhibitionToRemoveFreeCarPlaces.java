import java.util.HashMap;

public class ProhibitionToRemoveFreeCarPlaces implements IPropertyAccessResponseHandler<Integer> {
    @Inject
    private Autoservice autoservice;

    @Override
    public void handleResponse(Integer integer) {
        var strategyHandlers = new HashMap<Boolean, IPropertyAccessResponseHandler<Integer>>();
        strategyHandlers.put(false, new RemovingFreeCarPlaceHandler());
        var responseHandler = strategyHandlers.getOrDefault(autoservice.getCarPlace(integer).isOccupied(),
                new RefusalRemovingOccupiedCarPlaceHandler());
        responseHandler.handleResponse(integer);
    }
}
