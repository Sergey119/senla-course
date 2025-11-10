import java.util.Scanner;

public class ShiftOrderTimeAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Shifting order time. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        Integer id = new Scanner(System.in).nextInt();
        System.out.println(autoservice.getOrder(id));
        System.out.println("Time required to shift the order (measured in hours).");
        Integer time = new Scanner(System.in).nextInt();
		autoservice.shiftOrderTime(id, time);
        System.out.println(autoservice.getOrder(id));
        System.out.println("The order was successfully shifted in time!");

    }
}