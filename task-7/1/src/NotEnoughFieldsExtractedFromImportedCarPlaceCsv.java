import java.util.List;

public class NotEnoughFieldsExtractedFromImportedCarPlaceCsv implements ICarPlaceResponseHandler {
    @Override
    public CarPlace handleResponse(List<String> fields) {
        throw new UnsupportedOperationException("One or more records " +
                "in the imported CSV file have problems with fields (should be 4).");
    }
}