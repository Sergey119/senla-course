import java.util.Scanner;

public class ShowTechniciansByOrderAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show technicians by order. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        var id = new Scanner(System.in).nextInt();
        System.out.print(autoservice.findTechniciansByOrder(id));
    }
}