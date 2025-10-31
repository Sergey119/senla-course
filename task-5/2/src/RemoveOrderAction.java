import java.util.Scanner;

public class RemoveOrderAction extends ConsoleUserAccessAction implements IAction {
	
    @Override
    public void execute() {
        System.out.println("--- Removing a order. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        Integer id = new Scanner(System.in).nextInt();
        autoservice.removeOrder(id);
        System.out.println(autoservice.getOrders());
        System.out.println("Order has been successfully removed!");

    }
}