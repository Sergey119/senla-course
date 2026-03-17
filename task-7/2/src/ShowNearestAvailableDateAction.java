import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class ShowNearestAvailableDateAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show the nearest available date. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("Enter the date when you want to see the nearest available date. (yyyy-MM-dd-HH-mm): ");
        Date nowDate = new Date();
        Date definiteDate = dateSetting(nowDate);
        autoservice.getDailyManagement().groupOrdersByDate(autoservice.getOrders());
        Map<Integer, Integer> map = autoservice.getDailyManagement().getAvailableDate(definiteDate);
        System.out.println("Available slots for car repairs [" + definiteDate + "]:");
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("- from " + entry.getKey() + " to " + (entry.getValue()+1) + " hours");
        }
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
