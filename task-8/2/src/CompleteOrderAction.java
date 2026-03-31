import java.util.Scanner;

public class CompleteOrderAction extends ConsoleUserAccessAction implements IAction {
	
    @Override
    public void execute() {
        System.out.println("--- Completing a order. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        var id = new Scanner(System.in).nextInt();
		autoservice.completeOrder(id);
        System.out.println(autoservice.getOrders());
        System.out.println("Order has been successfully completed!");

    }
}