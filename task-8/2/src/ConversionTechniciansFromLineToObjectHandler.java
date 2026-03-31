import java.util.List;

public class ConversionTechniciansFromLineToObjectHandler implements ITechnicianResponseHandler {

    @Override
    public Technician handleResponse(List<String> fields) {
        var id = Integer.parseInt(fields.get(0).trim());
        var name = unescapeCsvField(fields.get(1).trim());
        var specialization = unescapeCsvField(fields.get(2).trim());
        var isAvailable = Boolean.parseBoolean(fields.get(3).trim());

        var technician = new Technician(id, name, specialization);
        technician.setAvailable(isAvailable);

        return technician;
    }

    private String unescapeCsvField(String field) {
        if (field == null) return "";
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
            field = field.replace("\"\"", "\"");
        }
        return field;
    }
}