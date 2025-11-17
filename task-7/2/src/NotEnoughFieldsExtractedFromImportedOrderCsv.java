import java.util.List;

public class NotEnoughFieldsExtractedFromImportedOrderCsv implements IOrderResponseHandler{
    @Override
    public Order handleResponse(List<String> fields) {
        throw new UnsupportedOperationException("One or more records " +
                "in the imported CSV file have problems with fields (should be 11).");
    }
}
