import java.util.*;

public class CreateOrderAction extends ConsoleUserAccessAction implements IAction {
	
    @Override
    public void execute() {
        System.out.println("--- Creating a order. ---");
        System.out.print("ID of order: ");
        int serviceAdvisorId = new Scanner(System.in).nextInt();
        System.out.print("How many technicians will be required to complete the order? ");
        int techniciansNumber = new Scanner(System.in).nextInt();
        Map<Integer, Technician> technicians = new HashMap<>();
        System.out.print("ID of technician(-s): ");
        for (int i = 0; i < techniciansNumber; i++) {
            int technician = new Scanner(System.in).nextInt();
            technicians.put(technician, autoservice.getTechnician(technician));
        }
        System.out.print("ID of customer: ");
        int customerId = new Scanner(System.in).nextInt();
        System.out.print("ID of car place: ");
        int carPlaceId = new Scanner(System.in).nextInt();
        Date createdDate = new Date();
        System.out.print("Enter when the order will begin. (yyyy-MM-dd-HH-mm): ");
        Date startDate = dateSetting(createdDate);
        Date loadingDate = startDate;
        loadingDate.setMinutes(loadingDate.getMinutes() + 10);
        System.out.print("Enter when the order will be completed. (yyyy-MM-dd-HH-mm): ");
        Date endDate = dateSetting(createdDate);
        Order order = new Order(4, autoservice.getServiceAdvisor(serviceAdvisorId), technicians,
                autoservice.getCustomer(customerId), autoservice.getCarPlace(carPlaceId),
                createdDate, startDate, loadingDate, endDate);
        autoservice.addOrder(order);
    }

    public Date dateSetting(Date nowDate) {
        int dateYear = new Scanner(System.in).nextInt();
        if (dateYear < 2024 || dateYear > 5000) {
            dateYear = nowDate.getYear();
            System.out.println("Incorrectly entered year. The current year value is taken.");
        }
        int dateMonth = new Scanner(System.in).nextInt();
        if (dateMonth < 0 || dateMonth > 11) {
            dateMonth = nowDate.getMonth();
            System.out.println("Incorrectly entered month. The current month value is taken.");
        }
        int dateDay = new Scanner(System.in).nextInt();
        if (dateDay < 1 || dateDay > 31) {
            dateDay = nowDate.getDate();
            System.out.println("Incorrectly entered day. The current day value is taken.");
        }
        int dateHour = new Scanner(System.in).nextInt();
        if (dateHour < 0 || dateHour > 23) {
            dateHour = nowDate.getHours();
            System.out.println("Incorrectly entered hours. The current hour value is taken.");
        }
        int dateMinute = new Scanner(System.in).nextInt();
        if (dateMinute < 0 || dateMinute > 59) {
            dateMinute = nowDate.getMinutes();
            System.out.println("Incorrectly entered minutes. The current minute value is taken.");
        }

        return new Date(dateYear, dateMonth, dateDay, dateHour, dateMinute);
    }
}
