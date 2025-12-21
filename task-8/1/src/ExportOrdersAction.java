import java.util.ArrayList;
import java.util.Scanner;

public class ExportOrdersAction extends ConsoleUserAccessAction implements IAction {
    private final String basePath = ConfigPropertiesDirectory.getInstance().getPath();

    @Override
    public void execute() {
        System.out.println("--- Exporting orders to CSV. ---");

        var orders = autoservice.getOrders();

        if (orders.isEmpty()) {
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

        var csvService = new OrderCsvService();
        var orderList = new ArrayList<>(orders.values());
        csvService.exportToCsv(orderList, filePath);
    }
}