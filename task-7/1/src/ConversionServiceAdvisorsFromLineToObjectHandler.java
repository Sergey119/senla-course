import java.util.List;

public class ConversionServiceAdvisorsFromLineToObjectHandler implements IServiceAdvisorResponseHandler{
    @Override
    public ServiceAdvisor handleResponse(List<String> fields) {
        var id = Integer.parseInt(fields.get(0).trim());
        var name = fields.get(1).trim();

        return new ServiceAdvisor(id, name);
    }
}
