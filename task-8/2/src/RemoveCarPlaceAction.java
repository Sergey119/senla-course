import java.util.HashMap;
import java.util.Scanner;

public class RemoveCarPlaceAction extends ConfirmedByConfigurationAction {

    @Override
    public void execute() {
        System.out.println("--- Removing a car place. ---");
        System.out.println(autoservice.getCarPlaces());
        System.out.print("ID of car place: ");
        var id = new Scanner(System.in).nextInt();

        var strategyHandlers = new HashMap<String, IPropertyAccessResponseHandler<Integer>>();
        strategyHandlers.put("true", new PermissionToRemoveFreeCarPlaces());
        var responseHandler = strategyHandlers.getOrDefault(
                String.valueOf(config.isAddRemoveFreeCarPlacesPermission()),
                new ProhibitionToRemoveFreeCarPlaces());
        responseHandler.handleResponse(id);

        System.out.println(autoservice.getCarPlaces());
        System.out.println("Car place has been successfully removed!");
    }
}