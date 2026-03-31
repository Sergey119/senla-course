public class ShowSortingByStartDateOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show orders sorted by start date. ---");
        System.out.println(autoservice.sortingByStartDateOrders());
    }
}
