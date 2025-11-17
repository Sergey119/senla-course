import java.io.*;
import java.util.*;

public class CarPlaceCsvService {
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

    public List<CarPlace> parseValidCarPlaces(List<String> allLines) {
        var validCarPlaces = new ArrayList<CarPlace>();

        for (var line : allLines) {
            var fields = parseCsvLine(line);
            var strategyHandlers = new HashMap<Integer, ICarPlaceResponseHandler>();
            strategyHandlers.put(4, new ConversionCarPlacesFromLineToObjectHandler());

            var responseHandler = strategyHandlers.getOrDefault(fields.size(),
                    new NotEnoughFieldsExtractedFromImportedCarPlaceCsv());
            var carPlace = responseHandler.handleResponse(fields);
            if (carPlace != null) {
                validCarPlaces.add(carPlace);
            }
        }

        System.out.println("Number of successfully created records of car places: " + validCarPlaces.size());
        return validCarPlaces;
    }

    public List<CarPlace> processCarPlaces(List<CarPlace> validCarPlaces, Map<Integer, CarPlace> existingCarPlaces) {
        var processedCarPlaces = new ArrayList<CarPlace>();
        var newRecords = 0;
        var updatedRecords = 0;

        for (var carPlaceFromCsv : validCarPlaces) {
            if (carPlaceFromCsv != null) {
                processedCarPlaces.add(carPlaceFromCsv);
                if (existingCarPlaces.containsKey(carPlaceFromCsv.getId())) {
                    updatedRecords++;
                } else {
                    newRecords++;
                }
            }
        }

        System.out.println("Car places processing completed:");
        System.out.println("- new records: " + newRecords);
        System.out.println("- updated records: " + updatedRecords);
        System.out.println("- total processed: " + processedCarPlaces.size());

        return processedCarPlaces;
    }

    // Основной метод импорта
    public List<CarPlace> importFromCsv(String filePath, Map<Integer, CarPlace> existingCarPlaces) throws IOException {
        var allLines = readAllLinesFromFile(filePath);
        if ((allLines.isEmpty()) || (!validateHeader(allLines.getFirst()))) {
            return new ArrayList<>();
        }
        allLines.removeFirst(); // Удаление строки с заголовком
        var validCarPlaces = parseValidCarPlaces(allLines);
        return processCarPlaces(validCarPlaces, existingCarPlaces);
    }

    private boolean validateHeader(String headerLine) {
        var expectedHeader = "id,square,carLift,isOccupied";
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

    public void exportToCsv(List<CarPlace> carPlaces, String filePath) {
        try (var writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,square,carLift,isOccupied");

            for (var carPlace : carPlaces) {
                writer.println(carPlace.getId() + "," + carPlace.getSquare() + ","
                    + carPlace.isCarLift() + "," + carPlace.isOccupied());
            }

            System.out.println("Данные экспортированы в файл: " + filePath);
            System.out.println("Записей экспортировано: " + carPlaces.size());

        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }
}