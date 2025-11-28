public class ShowSortingByEndDateOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show orders sorted by end date. ---");
        System.out.println(autoservice.sortingByEndDateOrders());
    }
}
