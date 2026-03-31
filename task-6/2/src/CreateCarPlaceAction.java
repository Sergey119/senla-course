import java.util.Scanner;

public class CreateCarPlaceAction extends ConsoleUserAccessAction implements IAction {
	private CarPlace carPlace;
	
    @Override
    public void execute() {
        System.out.println("--- Creating a car place. ---");
        System.out.println(autoservice.getCarPlaces());
        System.out.print("The car place square: ");
        int square = new Scanner(System.in).nextInt();
        System.out.print("Is there a car lift at the car place? (y/n) ");
        String s = new Scanner(System.in).nextLine();
        boolean carLift = false;
        if (s.equals("y") || s.equals("Y") || s.equals("yes") || s.equals("Yes")) {
            carLift = true;
        } else if (!(s.equals("n") || s.equals("N") || s.equals("no") || s.equals("No"))) {
            System.out.println("Incorrectly entered data. The value of car lift is false.");
        }
        carPlace = new CarPlace(3, square, carLift);
        autoservice.addCarPlace(carPlace);
        System.out.println(autoservice.getCarPlaces());
        System.out.println("Car place has been successfully created!");

    }
}