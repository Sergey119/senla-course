import java.util.List;

public interface IServiceAdvisorResponseHandler {
    ServiceAdvisor handleResponse(List<String> fields);
}
