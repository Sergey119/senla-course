import java.util.List;

public class NotEnoughFieldsExtractedFromImportedTechnicianCsv implements ITechnicianResponseHandler{
    @Override
    public Technician handleResponse(List<String> fields) {
        throw new UnsupportedOperationException("One or more records " +
                "in the imported CSV file have problems with fields (should be 4).");
    }
}
