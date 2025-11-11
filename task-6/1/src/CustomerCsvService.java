import java.io.*;
import java.util.*;

public class CustomerCsvService {
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

    public List<Customer> parseValidCustomers(List<String> allLines) {
        var validCustomers = new ArrayList<Customer>();

        for (var line : allLines) {
            var fields = parseCsvLine(line);
            var strategyHandlers = new HashMap<Integer, ICustomerResponseHandler>();
            strategyHandlers.put(2, new ConversionCustomersFromLineToObjectHandler());

            var responseHandler = strategyHandlers.getOrDefault(fields.size(),
                    new NotEnoughFieldsExtractedFromImportedCustomerCsv());
            var customer = responseHandler.handleResponse(fields);
            if (customer != null) {
                validCustomers.add(customer);
            }
        }

        System.out.println("Number of successfully created records of customers: "
                + validCustomers.size());
        return validCustomers;
    }

    public List<Customer> processCustomers(List<Customer> validCustomers, Map<Integer, Customer> existingCustomers) {
        var processedCustomers = new ArrayList<Customer>();
        var newRecords = 0;
        var updatedRecords = 0;

        for (var customerFromCsv : validCustomers) {
            if (customerFromCsv != null) {
                processedCustomers.add(customerFromCsv);
                if (existingCustomers.containsKey(customerFromCsv.getId())) {
                    updatedRecords++;
                } else {
                    newRecords++;
                }
            }
        }

        System.out.println("Customers processing completed:");
        System.out.println("- new records: " + newRecords);
        System.out.println("- updated records: " + updatedRecords);
        System.out.println("- total processed: " + processedCustomers.size());

        return processedCustomers;
    }

    // Основной метод импорта
    public List<Customer> importFromCsv(String filePath, Map<Integer, Customer> existingCustomers) throws IOException {
        var allLines = readAllLinesFromFile(filePath);
        if ((allLines.isEmpty()) || (!validateHeader(allLines.getFirst()))) {
            return new ArrayList<>();
        }
        allLines.removeFirst(); // Удаление строки с заголовком
        var validCustomers = parseValidCustomers(allLines);
        return processCustomers(validCustomers, existingCustomers);
    }

    private boolean validateHeader(String headerLine) {
        var expectedHeader = "id,name";
        if (headerLine == null || !headerLine.equals(expectedHeader)) {
            System.out.println("Invalid CSV file header format");
            System.out.println("Expected: " + expectedHeader);
            System.out.println("Received: " + headerLine);
            return false;
        }
        return true;
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
}