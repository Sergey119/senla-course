import java.util.HashMap;
import java.util.Map;

public class Builder {
    private Menu rootMenu;

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {
        Menu carPlacesMenu = buildCarPlacesMenu();
        Menu techniciansMenu = buildTechniciansMenu();
        Menu ordersMenu = buildOrdersMenu();

        Map<Integer, MenuItem> mainItems = new HashMap<>();
        mainItems.put(0, new MenuItem("Exit", null, null));
        mainItems.put(1, new MenuItem("Order management", null, ordersMenu));
        mainItems.put(2, new MenuItem("Technician management", null, techniciansMenu));
        mainItems.put(3, new MenuItem("Car places management", null, carPlacesMenu));

        rootMenu = new Menu("Main menu of autoservice", mainItems);
    }

    private Menu buildOrdersMenu() {
        Menu sortingOrdersAfterOrdersMenuMenu = buildSortingOrdersAfterOrdersMenuMenu();
        Menu sortingCurrentlyRunningOrdersAfterOrdersMenuMenu = buildSortingCurrentlyRunningOrdersAfterOrdersMenuMenu();

        Map<Integer, MenuItem> orderItems = new HashMap<>();
        orderItems.put(0, new MenuItem("Exit", null, null));
        orderItems.put(1, new MenuItem("Create order", new CreateOrderAction(), null));
        orderItems.put(2, new MenuItem("Remove order", new RemoveOrderAction(), null));
        orderItems.put(3, new MenuItem("Complete order", new CompleteOrderAction(), null));
        orderItems.put(4, new MenuItem("Cancel order", new CancelOrderAction(), null));
        orderItems.put(5, new MenuItem("Shift the order time", new ShiftOrderTimeAction(), null));
        orderItems.put(6, new MenuItem("Show list of orders", null, sortingOrdersAfterOrdersMenuMenu));
        orderItems.put(7, new MenuItem("Show list of currently running orders", null, sortingCurrentlyRunningOrdersAfterOrdersMenuMenu));
        orderItems.put(8, new MenuItem("Show the order by technician", new ShowOrderByTechnicianAction(), null));
        orderItems.put(9, new MenuItem("Show orders over period time", new ShowOrdersOverPeriodTimeStrictEqualityAction(), null));
        orderItems.put(10, new MenuItem("Show nearest available date", new ShowNearestAvailableDateAction(), null));

        return new Menu("Order management", orderItems);
    }

    private Menu buildSortingOrdersAfterOrdersMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show orders sorting by start date", new ShowSortingByStartDateOrdersAction(), null));
        sortingOptions.put(2, new MenuItem("Show orders sorting by end date", new ShowSortingByEndDateOrdersAction(), null));
        sortingOptions.put(3, new MenuItem("Show orders sorting by loading date", new ShowSortingByLoadingDateOrdersAction(), null));
        sortingOptions.put(4, new MenuItem("Show orders sorting by cost", new ShowSortingByCostOrdersAction(), null));

        return new Menu("Order sorting options", sortingOptions);
    }

    private Menu buildSortingCurrentlyRunningOrdersAfterOrdersMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show orders sorting by start date", new ShowSortingByStartDateCurrentlyRunningOrdersAction(), null));
        sortingOptions.put(2, new MenuItem("Show orders sorting by end date", new ShowSortingByEndDateCurrentlyRunningOrdersAction(), null));
        sortingOptions.put(3, new MenuItem("Show orders sorting by cost", new ShowSortingByCostCurrentlyRunningOrdersAction(), null));

        return new Menu("Options for sorting currently running orders", sortingOptions);
    }

    private Menu buildTechniciansMenu() {
        Menu sortingTechniciansAfterTechniciansMenuMenu = buildSortingTechniciansAfterTechniciansMenuMenu();

        Map<Integer, MenuItem> technicianItems = new HashMap<>();
        technicianItems.put(0, new MenuItem("Exit", null, null));
        technicianItems.put(1, new MenuItem("Create technician", new CreateTechnicianAction(), null));
        technicianItems.put(2, new MenuItem("Remove technician", new RemoveTechnicianAction(), null));
        technicianItems.put(3, new MenuItem("Show all technicians", null, sortingTechniciansAfterTechniciansMenuMenu));
        technicianItems.put(4, new MenuItem("Show technician(-s) performing the specified order", new ShowTechniciansByOrderAction(), null));

        return new Menu("Technician management", technicianItems);
    }

    private Menu buildSortingTechniciansAfterTechniciansMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show technicians sorting alphabetically", new ShowSortingTechniciansAlphabeticallyAction(), null));
        sortingOptions.put(2, new MenuItem("Show technicians sorting by occupancy", new ShowSortingTechniciansByOccupancyAction(), null));

        return new Menu("Technician sorting options", sortingOptions);
    }

    private Menu buildCarPlacesMenu() {
        Map<Integer, MenuItem> spotItems = new HashMap<>();
        spotItems.put(0, new MenuItem("Exit", null, null));
        spotItems.put(1, new MenuItem("Create car place", new CreateCarPlaceAction(), null));
        spotItems.put(2, new MenuItem("Remove car place", new RemoveCarPlaceAction(), null));
        spotItems.put(3, new MenuItem("Show list of available car places", new ShowAvailableCarPlacesAction(), null));
        spotItems.put(4, new MenuItem("Show the number of available car places on a specified date in the future", new ShowAvailableCarPlacesInFutureAction(), null));

        return new Menu("Car places management", spotItems);
    }
}