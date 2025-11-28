import java.util.Scanner;

public class CreateTechnicianAction extends ConsoleUserAccessAction implements IAction {
	private Technician technician;
	
    @Override
    public void execute() {
        System.out.println("--- Creating a technician. ---");
        System.out.println(autoservice.getTechnicians());
        System.out.print("Name of technician: ");
        String technicianName = new Scanner(System.in).nextLine();
        System.out.print("Name of specialization: ");
        String specialization = new Scanner(System.in).nextLine();
		technician = new Technician(4, technicianName, specialization);
        autoservice.addTechnician(technician);
        System.out.println(autoservice.getTechnicians());
        System.out.println("Technician has been successfully created!");

    }
}