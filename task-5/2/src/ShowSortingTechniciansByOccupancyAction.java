public class ShowSortingTechniciansByOccupancyAction extends ConsoleUserAccessAction implements IAction {

    @Override
    public void execute() {
        System.out.println("--- Show technicians sorted by occupancy. ---");
        System.out.println(autoservice.sortingTechniciansByOccupancy());
    }
}
