import java.util.List;

public class ConversionCarPlacesFromLineToObjectHandler implements ICarPlaceResponseHandler {

    @Override
    public CarPlace handleResponse(List<String> fields) {
        var id = Integer.parseInt(fields.get(0).trim());
        var square = Integer.parseInt(fields.get(1).trim());
        var carLift = Boolean.parseBoolean(fields.get(2).trim());
        var isOccupied = Boolean.parseBoolean(fields.get(3).trim());

        var carPlace = new CarPlace(id, square, carLift);
        carPlace.setOccupied(isOccupied);

        return carPlace;
    }
}
