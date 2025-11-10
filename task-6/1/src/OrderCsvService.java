import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderCsvService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public List<String> readAllLinesFromFile(String filePath) throws IOException {
        var allLines = new ArrayList<String>();

        var reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            allLines.add(line);
        }
        System.out.println("The number of lines extracted from the imported file: " + allLines.size());

        return allLines;
    }

    public List<Order> parseValidOrders(List<String> allLines) {
        var validOrders = new ArrayList<Order>();

        for (var line : allLines) {
            var fields = parseCsvLine(line);
            var strategyHandlers = new HashMap<Integer, IOrderResponseHandler>();
            strategyHandlers.put(11, new ConversionOrdersFromLineToObjectHandler());

            var responseHandler = strategyHandlers.getOrDefault(fields.size(),
                    new NotEnoughFieldsExtractedFromImportedOrderCsv());
            validOrders.add(responseHandler.handleResponse(fields));
        }

        System.out.println("Number of successfully created records of orders: " + validOrders.size());
        return validOrders;
    }

    public List<Order> processOrders(List<Order> validOrders, Map<Integer, Order> existingOrders) {
        var processedOrders = new ArrayList<Order>();
        var newRecords = 0;
        var updatedRecords = 0;

        for (var orderFromCsv : validOrders) {
            if (orderFromCsv != null) {
                processedOrders.add(orderFromCsv);
                if (existingOrders.containsKey(orderFromCsv.getId())) {
                    updatedRecords++;
                } else {
                    newRecords++;
                }
            }
        }

        System.out.println("Orders processing completed:");
        System.out.println("- new records: " + newRecords);
        System.out.println("- updated records: " + updatedRecords);
        System.out.println("- total processed: " + processedOrders.size());

        return processedOrders;
    }

    // Основной метод импорта
    public List<Order> importFromCsv(String filePath, Map<Integer, Order> existingOrders) throws IOException {
        var allLines = readAllLinesFromFile(filePath);
        if ((allLines.isEmpty()) || (!validateHeader(allLines.getFirst()))) {
            return new ArrayList<>();
        }
        allLines.removeFirst(); // Удаление строки с заголовком
        var validOrders = parseValidOrders(allLines);
        return processOrders(validOrders, existingOrders);
    }

    private boolean validateHeader(String headerLine) {
        var expectedHeader = "id,serviceAdvisorId,technicianIds,customerId,carPlaceId,status,cost,createdDate,startDate,loadingDate,endDate";
        if (headerLine == null || !headerLine.equals(expectedHeader)) {
            System.out.println("Invalid CSV file header format");
            System.out.println("Expected: " + expectedHeader);
            System.out.println("Received: " + headerLine);
            return false;
        }
        return true;
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }

    private List<String> parseCsvLine(String line) {
        var fields = new ArrayList<String>();
        var currentField = new StringBuilder();
        var inQuotes = false;

        for (var i = 0; i < line.length(); i++) {
            var c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        fields.add(currentField.toString());
        return fields;
    }

    public void exportToCsv(List<Order> orders, String filePath) {
        try (var writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,serviceAdvisorId,technicianIds,customerId,carPlaceId,status,cost,createdDate,startDate,loadingDate,endDate");
            for (var order : orders) {
                writer.println(order.getId() + "," + order.getServiceAdvisor().getId() + ","
                        + escapeCsvField(getTechnicianIdsString(order.getTechnicians())) + ","
                        + order.getCustomer().getId() + "," + order.getCarPlace().getId() + ","
                        + order.getStatus().name() + "," + order.getCost() + ","
                        + formatDate(order.getCreatedDate()) + "," + formatDate(order.getStartDate()) + ","
                        + formatDate(order.getLoadingDate()) + "," + formatDate(order.getEndDate())
                );
            }
            System.out.println("Orders data has been exported to file: " + filePath);
            System.out.println("Number of records exported: " + orders.size());
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    private String getTechnicianIdsString(Map<Integer, Technician> technicians) {
        if (technicians == null || technicians.isEmpty()) {
            return "";
        }
        var ids = new ArrayList<String>();
        for (var id : technicians.keySet()) {
            ids.add(String.valueOf(id));
        }
        return String.join(";", ids);
    }

    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}