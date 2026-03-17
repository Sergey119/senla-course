import java.util.Scanner;

public class ImportTechniciansAction extends ConsoleUserAccessAction implements IAction {
    private final String basePath = "C:\\Users\\admin\\IdeaProjects\\senla-course\\task-7\\2\\stream\\";

    @Override
    public void execute() {
        System.out.println("--- Importing technicians from CSV. ---");
        System.out.println(autoservice.getTechnicians());

        // Запрос имени файла у пользователя
        System.out.print("Enter the CSV file name (without extension): ");
        var scanner = new Scanner(System.in);
        var fileName = scanner.nextLine();

        if (fileName.isEmpty()) {
            System.out.println("File name cannot be empty!");
            return;
        }

        var filePath = basePath + fileName + ".csv";
        System.out.println("Full path to the file: " + filePath);

        var csvService = new TechnicianCsvService();

        // Подтверждение импорта
        System.out.println("Note: Importing will update existing records with matching IDs.");
        System.out.print("Continue import? (yes/no):");
        var confirmation = new Scanner(System.in).nextLine();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Import cancelled.");
            return;
        }

        var importedTechnicians = csvService.importFromCsv(filePath, autoservice.getTechnicians());

        // Если есть запись с таким id, то она обновляется,
        // если нет, то добавляется новая
        for (var technician : importedTechnicians) {
            autoservice.getTechnicians().put(technician.getId(), technician);
        }

        System.out.println("Imported data on technicians has been added to the system.");
        System.out.println(autoservice.getTechnicians());
    }
}