public class ShowSortingByStartDateCurrentlyRunningOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show currently running orders sorted by start date. ---");
        System.out.println(autoservice.sortingByStartDateCurrentlyRunningOrders());
    }
}
