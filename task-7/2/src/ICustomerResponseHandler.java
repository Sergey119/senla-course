import java.util.List;

public interface ICustomerResponseHandler {
    Customer handleResponse(List<String> fields);
}
