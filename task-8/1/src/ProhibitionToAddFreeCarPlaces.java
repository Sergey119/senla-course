import java.util.HashMap;

public class ProhibitionToAddFreeCarPlaces implements IPropertyAccessResponseHandler<CarPlace> {
    @Override
    public void handleResponse(CarPlace carPlace) {
        var strategyHandlers = new HashMap<Boolean, IPropertyAccessResponseHandler<CarPlace>>();
        strategyHandlers.put(false, new AddingFreeCarPlaceHandler());
        var responseHandler = strategyHandlers.getOrDefault(carPlace.isOccupied(),
                new RefusalAddingOccupiedCarPlaceHandler());
        responseHandler.handleResponse(carPlace);
    }
}
