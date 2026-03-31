import java.util.Scanner;

public class CancelOrderAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Canceling a order. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        var id = new Scanner(System.in).nextInt();
        autoservice.cancelOrder(id);
        System.out.println(autoservice.getOrders());
        System.out.println("Order has been successfully canceled!");

    }
}