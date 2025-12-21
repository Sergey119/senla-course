import java.util.List;

public class ConversionCustomersFromLineToObjectHandler implements ICustomerResponseHandler{
    @Override
    public Customer handleResponse(List<String> fields) {
        var id = Integer.parseInt(fields.get(0).trim());
        var name = fields.get(1).trim();

        return new Customer(id, name);
    }
}
