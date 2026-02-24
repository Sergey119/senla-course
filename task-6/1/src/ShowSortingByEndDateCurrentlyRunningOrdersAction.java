public class ShowSortingByEndDateCurrentlyRunningOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show currently running orders sorted by end date. ---");
        System.out.println(autoservice.sortingByEndDateCurrentlyRunningOrders());
    }
}
