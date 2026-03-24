public class ShowAvailableCarPlacesAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show available car places. ---");
        System.out.print(autoservice.availableCarPlacesList());
    }
}