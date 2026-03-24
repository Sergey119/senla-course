import java.util.Scanner;

public class RemoveCarPlaceAction extends ConsoleUserAccessAction implements IAction {
	private CarPlace carPlace;
	
    @Override
    public void execute() {
        System.out.println("--- Removing a car place. ---");
        System.out.println(autoservice.getCarPlaces());
        System.out.print("ID of car place: ");
        Integer id = new Scanner(System.in).nextInt();
		autoservice.removeCarPlace(id);
        System.out.println(autoservice.getCarPlaces());
        System.out.println("Car place has been successfully removed!");
    }
}