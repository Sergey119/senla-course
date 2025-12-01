import java.util.List;

public class TechnicianNotFoundHandler implements ITechnicianExistenceResponseHandler{
    @Override
    public List<Order> handleResponse(int key) {
        throw new UnsupportedOperationException("Technician not found.");
    }
}
