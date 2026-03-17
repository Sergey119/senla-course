import java.util.List;
import java.util.stream.Collectors;

public class TechnicianExistsHandler implements ITechnicianExistenceResponseHandler{
    private final Autoservice autoservice = Autoservice.getInstance();

    @Override
    public List<Order> handleResponse(int key) {
        return autoservice.getOrders().values().stream()
                .filter(order -> order.getTechnicians().containsKey(key)).toList();
    }
}
