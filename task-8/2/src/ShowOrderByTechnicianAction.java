import java.util.Scanner;

public class ShowOrderByTechnicianAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show order by technician. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of technician: ");
        var id = new Scanner(System.in).nextInt();
        System.out.print(autoservice.findOrderByTechnician(id));
    }
}