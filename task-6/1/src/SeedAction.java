import java.io.IOException;

public class SeedAction extends ConsoleUserAccessAction implements IAction {
    private final String filePath = "C:\\IdeaProjects\\senla-course\\task-6\\1\\stream\\";

    @Override
    public void execute() throws IOException {
        System.out.println("--- Seeding. ---");

        // Порядок важен
        loadServiceAdvisors();
        loadCustomers();
        loadTechnicians();
        loadCarPlaces();
        loadOrders();

        System.out.println("Dummy data was loaded");
        System.out.println("- service advisors: " + autoservice.getServiceAdvisors().size());
        System.out.println("- customers: " + autoservice.getCustomers().size());
        System.out.println("- technicians: " + autoservice.getTechnicians().size());
        System.out.println("- car places: " + autoservice.getCarPlaces().size());
        System.out.println("- orders: " + autoservice.getOrders().size());

    }

    private void loadServiceAdvisors() throws IOException {
        var csvService = new ServiceAdvisorCsvService();

        var importedAdvisors = csvService.importFromCsv(filePath
                + "default_service_advisors.csv", autoservice.getServiceAdvisors());
        for (var advisor : importedAdvisors) {
            autoservice.getServiceAdvisors().put(advisor.getId(), advisor);
        }
    }

    private void loadCustomers() throws IOException {
        var csvService = new CustomerCsvService();

        var importedCustomers = csvService.importFromCsv(filePath
                + "default_customers.csv", autoservice.getCustomers());
        for (var customer : importedCustomers) {
            autoservice.getCustomers().put(customer.getId(), customer);
        }
    }

    private void loadTechnicians() throws IOException {
        var csvService = new TechnicianCsvService();

        var importedTechnicians = csvService.importFromCsv(filePath
                + "default_technicians.csv", autoservice.getTechnicians());
        for (var technician : importedTechnicians) {
            autoservice.getTechnicians().put(technician.getId(), technician);
        }
    }

    private void loadCarPlaces() throws IOException {
        var csvService = new CarPlaceCsvService();

        var importedCarPlaces = csvService.importFromCsv(filePath
                + "default_car_places.csv", autoservice.getCarPlaces());
        for (var carPlace : importedCarPlaces) {
            autoservice.getCarPlaces().put(carPlace.getId(), carPlace);
        }
    }

    private void loadOrders() throws IOException {
        var csvService = new OrderCsvService();

        var importedOrders = csvService.importFromCsv(filePath
                + "default_orders.csv", autoservice.getOrders());
        for (var order : importedOrders) {
            autoservice.getOrders().put(order.getId(), order);
        }
    }
}
