import java.util.List;

public interface IOrderResponseHandler {
    Order handleResponse(List<String> fields);
}
