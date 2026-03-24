import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionOrdersFromLineToObjectHandler implements IOrderResponseHandler {
    private final Autoservice autoservice = Autoservice.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public Order handleResponse(List<String> fields) {
        var id = Integer.parseInt(fields.get(0).trim());
        var serviceAdvisorId = Integer.parseInt(fields.get(1).trim());
        var technicianIdsStr = unescapeCsvField(fields.get(2).trim());
        var customerId = Integer.parseInt(fields.get(3).trim());
        var carPlaceId = Integer.parseInt(fields.get(4).trim());
        var status = OrderStatus.valueOf(fields.get(5).trim());
        var cost = Integer.parseInt(fields.get(6).trim());
        var createdDate = parseDate(fields.get(7).trim());
        var startDate = parseDate(fields.get(8).trim());
        var loadingDate = parseDate(fields.get(9).trim());
        var endDate = parseDate(fields.get(10).trim());

        var serviceAdvisor = autoservice.getServiceAdvisor(serviceAdvisorId);
        var orderTechnicians = parseTechnicianIds(technicianIdsStr);
        var customer = autoservice.getCustomer(customerId);
        var carPlace = autoservice.getCarPlace(carPlaceId);

        if (serviceAdvisor == null || customer == null || carPlace == null || orderTechnicians.isEmpty()) {
            System.out.println("Missing links for related records with the order");
            return null;
        }

        var order = new Order(id, serviceAdvisor, orderTechnicians, customer,
                carPlace, createdDate, startDate, loadingDate, endDate);
        order.setStatus(status);
        order.setCost(cost);

        return order;
    }

    private String unescapeCsvField(String field) {
        if (field == null) return "";
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
            field = field.replace("\"\"", "\"");
        }
        return field;
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Error occurred when parsing the date: " + dateStr);
            return null;
        }
    }

    private Map<Integer, Technician> parseTechnicianIds(String technicianIdsStr) {
        var result = new HashMap<Integer, Technician>();
        if (technicianIdsStr == null || technicianIdsStr.isEmpty()) {
            return result;
        }

        var ids = technicianIdsStr.split(";");
        for (var idStr : ids) {
            var technicianId = Integer.parseInt(idStr.trim());
            var technician = autoservice.getTechnician(technicianId);
            if (technician != null) {
                result.put(technicianId, technician);
            }

        }
        return result;
    }
}

