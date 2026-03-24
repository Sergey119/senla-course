import java.util.List;

public class NotEnoughFieldsExtractedFromImportedCustomerCsv implements ICustomerResponseHandler{
    @Override
    public Customer handleResponse(List<String> fields) {
        throw new UnsupportedOperationException("One or more records " +
                "in the imported CSV file have problems with fields (should be 4).");
    }
}
