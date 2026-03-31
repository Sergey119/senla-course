public class ShowSortingTechniciansAlphabeticallyAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show technicians sorted alphabetically. ---");
        System.out.println(autoservice.sortingTechniciansAlphabetically());
    }
}
