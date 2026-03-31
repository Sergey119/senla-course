import java.util.Date;
import java.util.Scanner;

public class ShowAvailableCarPlacesInFutureAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- The number of available car places on a specific date. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("Enter the date when you want to see the number of car places that are available. (yyyy-MM-dd): ");
        Date nowDate = new Date();
        Date definiteDate = dateSetting(nowDate);
        autoservice.getDailyManagement().groupOrdersByDate(autoservice.getOrders());
        System.out.println("Number of available car places for the selected date [" + definiteDate +
                "]: " + autoservice.getDailyManagement().getAvailableCarPlaces(definiteDate));
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

        return new Date(dateYear, dateMonth, dateDay);
    }
}
