public class ShowSortingByCostCurrentlyRunningOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show currently running orders sorted by cost. ---");
        System.out.println(autoservice.sortingByCostCurrentlyRunningOrders());
    }
}
