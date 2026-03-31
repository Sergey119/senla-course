import java.util.ArrayList;
import java.util.Scanner;

public class ExportTechniciansAction extends ConsoleUserAccessAction implements IAction {
    private final String basePath = "C:\\IdeaProjects\\senla-course\\task-6\\1\\stream\\";

    @Override
    public void execute() {
        System.out.println("--- Exporting technicians to CSV. ---");

        var technicians = autoservice.getTechnicians();

        if (technicians.isEmpty()) {
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

        var csvService = new TechnicianCsvService();
        var technicianList = new ArrayList<>(technicians.values());
        csvService.exportToCsv(technicianList, filePath);
    }
}
