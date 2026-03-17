import java.util.Scanner;

public class ImportCarPlacesAction extends ConsoleUserAccessAction implements IAction {
    private final String basePath = "C:\\IdeaProjects\\senla-course\\task-6\\2\\stream\\";

    @Override
    public void execute() {
        System.out.println("--- Importing car places from CSV. ---");
        System.out.println(autoservice.getCarPlaces());

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

        // Подтверждение импорта
        System.out.println("Note: Importing will update existing records with matching IDs.");
        System.out.print("Continue import? (yes/no):");
        var confirmation = new Scanner(System.in).nextLine();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Import cancelled.");
            return;
        }

        var importedCarPlaces = csvService.importFromCsv(filePath, autoservice.getCarPlaces());

        // Если есть запись с таким id, то она обновляется,
        // если нет, то добавляется новая
        for (var carPlace : importedCarPlaces) {
            autoservice.getCarPlaces().put(carPlace.getId(), carPlace);
        }

        System.out.println("Imported data on car places has been added to the system.");
        System.out.println(autoservice.getCarPlaces());
    }
}