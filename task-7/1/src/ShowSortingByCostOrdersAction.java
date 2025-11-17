public class ShowSortingByCostOrdersAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show orders sorted by cost. ---");
        System.out.println(autoservice.sortingByCostOrders());
    }
}
