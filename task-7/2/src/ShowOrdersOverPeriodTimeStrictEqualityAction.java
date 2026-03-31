import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ShowOrdersOverPeriodTimeStrictEqualityAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show orders for a certain period of time. ---");
        System.out.println(autoservice.getOrders());
        Date nowDate = new Date();
        List<Date> timePeriod = new ArrayList<>();
        System.out.print("Enter the start date. (yyyy-MM-dd-HH-mm): ");
        timePeriod.add(dateSetting(nowDate));
        System.out.print("Enter the end date. (yyyy-MM-dd-HH-mm): ");
        timePeriod.add(dateSetting(nowDate));
        System.out.print("What is the order status? (pen/com/can) ");
        String s = new Scanner(System.in).nextLine();
        OrderStatus status = OrderStatus.PENDING;
        if (s.equals("completed") || s.equals("complete") || s.equals("com") || s.equals("co")) {
            status = OrderStatus.COMPLETED;
        } else if (s.equals("cancelled") || s.equals("cancel") || s.equals("can") || s.equals("ca") ) {
            status = OrderStatus.CANCELLED;
        } else if (!(s.equals("pending") || s.equals("pen") || s.equals("p"))) {
            System.out.println("Incorrectly entered data. The value of status is pending.");
        }
        System.out.print("What comparator is used? (start/end/cost) ");
        s = new Scanner(System.in).nextLine();
        OrderStartDateEndDateCostOnlyComparator comparator = new OrderStartDateComparator();
        if (s.equals("end date") || s.equals("end") || s.equals("e")) {
            comparator = new OrderEndDateComparator();
        } else if (s.equals("cost") || s.equals("c")) {
            comparator = new OrderCostComparator();
        } else if (!(s.equals("start date") || s.equals("start") || s.equals("s"))) {
            System.out.println("Incorrectly entered data. A comparator based on the start date is used.");
        }
        System.out.println(autoservice.ordersOverPeriodTimeStrictEquality(timePeriod, status, comparator));
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