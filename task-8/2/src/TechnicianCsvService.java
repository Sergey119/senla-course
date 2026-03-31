import java.io.*;
import java.util.*;

public class TechnicianCsvService {
    public List<String> readAllLinesFromFile(String filePath) {
        var allLines = new ArrayList<String>();

        try (var reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            System.out.println("The number of lines extracted from the imported file: " + allLines.size());

        } catch (FileNotFoundException e) {
            System.out.println("File " + filePath + " not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return allLines;
    }

    public List<Technician> parseValidTechnicians(List<String> allLines) {
        var validTechnicians = new ArrayList<Technician>();

        for (var line : allLines) {
            var fields = parseCsvLine(line);
            var strategyHandlers = new HashMap<Integer, ITechnicianResponseHandler>();
            strategyHandlers.put(4, new ConversionTechniciansFromLineToObjectHandler());

            var responseHandler = strategyHandlers.getOrDefault(fields.size(),
                    new NotEnoughFieldsExtractedFromImportedTechnicianCsv());
            var technician = responseHandler.handleResponse(fields);
            if (technician != null) {
                validTechnicians.add(technician);
            }
        }

        System.out.println("Number of successfully created records of technicians: " + validTechnicians.size());
        return validTechnicians;
    }

    public List<Technician> processTechnicians(List<Technician> validTechnicians, Map<Integer, Technician> existingTechnicians) {
        var processedTechnicians = new ArrayList<Technician>();
        var newRecords = 0;
        var updatedRecords = 0;

        for (var technicianFromCsv : validTechnicians) {
            if (technicianFromCsv != null) {
                processedTechnicians.add(technicianFromCsv);
                if (existingTechnicians.containsKey(technicianFromCsv.getId())) {
                    updatedRecords++;
                } else {
                    newRecords++;
                }
            }
        }

        System.out.println("Technicians processing completed:");
        System.out.println("- new records: " + newRecords);
        System.out.println("- updated records: " + updatedRecords);
        System.out.println("- total processed: " + processedTechnicians.size());

        return processedTechnicians;
    }

    // Основной метод импорта
    public List<Technician> importFromCsv(String filePath, Map<Integer, Technician> existingTechnicians) {
        var allLines = readAllLinesFromFile(filePath);
        if ((allLines.isEmpty()) || (!validateHeader(allLines.getFirst()))) {
            return new ArrayList<>();
        }
        allLines.removeFirst(); // Удаление строки с заголовком
        var validTechnicians = parseValidTechnicians(allLines);
        return processTechnicians(validTechnicians, existingTechnicians);
    }

    private boolean validateHeader(String headerLine) {
        var expectedHeader = "id,name,specialization,isAvailable";
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

    public void exportToCsv(List<Technician> technicians, String filePath) {
        try (var writer = new PrintWriter(new FileWriter(filePath))) {
            // Записываем заголовок
            writer.println("id,name,specialization,isAvailable");

            for (var technician : technicians) {
                writer.println(technician.getId() + "," + escapeCsvField(technician.getName()) + ","
                    + escapeCsvField(technician.getSpecialization()) + "," + technician.isAvailable());
            }

            System.out.println("Technicians data has been exported to file: " + filePath);
            System.out.println("Number of records exported: " + technicians.size());

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}