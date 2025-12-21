import java.util.List;

public class TechnicianExistsHandler implements ITechnicianExistenceResponseHandler{
    @Inject
    private Autoservice autoservice;

    @Override
    public List<Order> handleResponse(int key) {
        return autoservice.getOrders().values().stream()
                .filter(order -> order.getTechnicians().containsKey(key)).toList();
    }
}
