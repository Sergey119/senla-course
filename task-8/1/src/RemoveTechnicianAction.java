import java.util.Scanner;

public class RemoveTechnicianAction extends ConsoleUserAccessAction implements IAction {
	
    @Override
    public void execute() {
        System.out.println("--- Removing a technician. ---");
        System.out.println(autoservice.getTechnicians());
        System.out.print("ID of technician: ");
        Integer id = new Scanner(System.in).nextInt();
        autoservice.removeTechnician(id);
        System.out.println(autoservice.getTechnicians());
        System.out.println("Technician has been successfully removed!");

    }
}