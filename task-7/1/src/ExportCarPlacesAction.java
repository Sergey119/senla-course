import java.util.ArrayList;
import java.util.Scanner;

public class ExportCarPlacesAction extends ConsoleUserAccessAction implements IAction {
    private final String basePath = "C:\\Users\\admin\\IdeaProjects\\senla-course\\task-7\\1\\stream\\";

    @Override
    public void execute() {
        System.out.println("--- Exporting car places to CSV. ---");

        var carPlaces = autoservice.getCarPlaces();

        if (carPlaces.isEmpty()) {
            System.out.println("There is no data available for export");
            return;
        }

        // Запрос имени файла у пользователя
        System.out.print("Enter the CSV file name (without extension): ");
        var scanner = new Scanner(System.in);
        var fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            System.out.println("File name cannot be empty!");
            return;
        }

        var filePath = basePath + fileName + ".csv";
        System.out.println("Full path to the file: " + filePath);

        var csvService = new CarPlaceCsvService();
        var carPlaceList = new ArrayList<>(carPlaces.values());
        csvService.exportToCsv(carPlaceList, filePath);
    }
}
