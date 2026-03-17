import java.io.*;
import java.util.*;

public class ServiceAdvisorCsvService {
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

    public List<ServiceAdvisor> parseValidServiceAdvisors(List<String> allLines) {
        var validServiceAdvisors = new ArrayList<ServiceAdvisor>();

        for (var line : allLines) {
            var fields = parseCsvLine(line);
            var strategyHandlers = new HashMap<Integer, IServiceAdvisorResponseHandler>();
            strategyHandlers.put(2, new ConversionServiceAdvisorsFromLineToObjectHandler());

            var responseHandler = strategyHandlers.getOrDefault(fields.size(),
                    new NotEnoughFieldsExtractedFromImportedServiceAdvisorCsv());
            var serviceAdvisor = responseHandler.handleResponse(fields);
            if (serviceAdvisor != null) {
                validServiceAdvisors.add(serviceAdvisor);
            }
        }

        System.out.println("Number of successfully created records of service advisors: "
                + validServiceAdvisors.size());
        return validServiceAdvisors;
    }

    public List<ServiceAdvisor> processServiceAdvisors(List<ServiceAdvisor> validServiceAdvisors, Map<Integer, ServiceAdvisor> existingServiceAdvisors) {
        var processedServiceAdvisors = new ArrayList<ServiceAdvisor>();
        var newRecords = 0;
        var updatedRecords = 0;

        for (var serviceAdvisorFromCsv : validServiceAdvisors) {
            if (serviceAdvisorFromCsv != null) {
                processedServiceAdvisors.add(serviceAdvisorFromCsv);
                if (existingServiceAdvisors.containsKey(serviceAdvisorFromCsv.getId())) {
                    updatedRecords++;
                } else {
                    newRecords++;
                }
            }
        }

        System.out.println("Service advisors processing completed:");
        System.out.println("- new records: " + newRecords);
        System.out.println("- updated records: " + updatedRecords);
        System.out.println("- total processed: " + processedServiceAdvisors.size());

        return processedServiceAdvisors;
    }

    // Основной метод импорта
    public List<ServiceAdvisor> importFromCsv(String filePath, Map<Integer, ServiceAdvisor> existingServiceAdvisors) throws IOException {
        var allLines = readAllLinesFromFile(filePath);
        if ((allLines.isEmpty()) || (!validateHeader(allLines.getFirst()))) {
            return new ArrayList<>();
        }
        allLines.removeFirst(); // Удаление строки с заголовком
        var validServiceAdvisors = parseValidServiceAdvisors(allLines);
        return processServiceAdvisors(validServiceAdvisors, existingServiceAdvisors);
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