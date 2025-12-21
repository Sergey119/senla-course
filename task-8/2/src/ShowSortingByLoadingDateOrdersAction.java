public class ShowSortingByLoadingDateOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show orders sorted by loading date. ---");
        System.out.println(autoservice.sortingByLoadingDateOrders());
    }
}
