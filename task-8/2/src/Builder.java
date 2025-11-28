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
        mainItems.put(4, new MenuItem("Seed the data",
                DIContainer.resolve(SeedAction.class), null));

        rootMenu = new Menu("Main menu of autoservice", mainItems);
    }

    private Menu buildOrdersMenu() {
        Menu sortingOrdersAfterOrdersMenuMenu = buildSortingOrdersAfterOrdersMenuMenu();
        Menu sortingCurrentlyRunningOrdersAfterOrdersMenuMenu = buildSortingCurrentlyRunningOrdersAfterOrdersMenuMenu();

        Map<Integer, MenuItem> orderItems = new HashMap<>();
        orderItems.put(0, new MenuItem("Exit", null, null));
        orderItems.put(1, new MenuItem("Create order",
                DIContainer.resolve(CreateOrderAction.class), null));
        orderItems.put(2, new MenuItem("Remove order",
                DIContainer.resolve(RemoveOrderAction.class), null));
        orderItems.put(3, new MenuItem("Complete order",
                DIContainer.resolve(CompleteOrderAction.class), null));
        orderItems.put(4, new MenuItem("Cancel order",
                DIContainer.resolve(CancelOrderAction.class), null));
        orderItems.put(5, new MenuItem("Shift the order time",
                DIContainer.resolve(ShiftOrderTimeAction.class), null));
        orderItems.put(6, new MenuItem("Show list of orders", null, sortingOrdersAfterOrdersMenuMenu));
        orderItems.put(7, new MenuItem("Show list of currently running orders", null, sortingCurrentlyRunningOrdersAfterOrdersMenuMenu));
        orderItems.put(8, new MenuItem("Show the order by technician",
                DIContainer.resolve(ShowOrderByTechnicianAction.class), null));
        orderItems.put(9, new MenuItem("Show orders over period time",
                DIContainer.resolve(ShowOrdersOverPeriodTimeStrictEqualityAction.class), null));
        orderItems.put(10, new MenuItem("Show nearest available date",
                DIContainer.resolve(ShowNearestAvailableDateAction.class), null));
        orderItems.put(11, new MenuItem("Upload orders data from an imported CSV file",
                DIContainer.resolve(ImportOrdersAction.class), null));
        orderItems.put(12, new MenuItem("Download orders data to an exportable CSV file",
                DIContainer.resolve(ExportOrdersAction.class), null));

        return new Menu("Order management", orderItems);
    }

    private Menu buildSortingOrdersAfterOrdersMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show orders sorting by start date",
                DIContainer.resolve(ShowSortingByStartDateOrdersAction.class), null));
        sortingOptions.put(2, new MenuItem("Show orders sorting by end date",
                DIContainer.resolve(ShowSortingByEndDateOrdersAction.class), null));
        sortingOptions.put(3, new MenuItem("Show orders sorting by loading date",
                DIContainer.resolve(ShowSortingByLoadingDateOrdersAction.class), null));
        sortingOptions.put(4, new MenuItem("Show orders sorting by cost",
                DIContainer.resolve(ShowSortingByCostOrdersAction.class), null));

        return new Menu("Order sorting options", sortingOptions);
    }

    private Menu buildSortingCurrentlyRunningOrdersAfterOrdersMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show orders sorting by start date",
                DIContainer.resolve(ShowSortingByStartDateCurrentlyRunningOrdersAction.class), null));
        sortingOptions.put(2, new MenuItem("Show orders sorting by end date",
                DIContainer.resolve(ShowSortingByEndDateCurrentlyRunningOrdersAction.class), null));
        sortingOptions.put(3, new MenuItem("Show orders sorting by cost",
                DIContainer.resolve(ShowSortingByCostCurrentlyRunningOrdersAction.class), null));

        return new Menu("Options for sorting currently running orders", sortingOptions);
    }

    private Menu buildTechniciansMenu() {
        Menu sortingTechniciansAfterTechniciansMenuMenu = buildSortingTechniciansAfterTechniciansMenuMenu();

        Map<Integer, MenuItem> technicianItems = new HashMap<>();
        technicianItems.put(0, new MenuItem("Exit", null, null));
        technicianItems.put(1, new MenuItem("Create technician",
                DIContainer.resolve(CreateTechnicianAction.class), null));
        technicianItems.put(2, new MenuItem("Remove technician",
                DIContainer.resolve(RemoveTechnicianAction.class), null));
        technicianItems.put(3, new MenuItem("Show all technicians", null, sortingTechniciansAfterTechniciansMenuMenu));
        technicianItems.put(4, new MenuItem("Show technician(-s) performing the specified order",
                DIContainer.resolve(ShowTechniciansByOrderAction.class), null));
        technicianItems.put(5, new MenuItem("Upload technicians data from an imported CSV file",
                DIContainer.resolve(ImportTechniciansAction.class), null));
        technicianItems.put(6, new MenuItem("Download technicians data to an exportable CSV file",
                DIContainer.resolve(ExportTechniciansAction.class), null));

        return new Menu("Technician management", technicianItems);
    }

    private Menu buildSortingTechniciansAfterTechniciansMenuMenu() {
        Map<Integer, MenuItem> sortingOptions = new HashMap<>();
        sortingOptions.put(0, new MenuItem("Exit", null, null));
        sortingOptions.put(1, new MenuItem("Show technicians sorting alphabetically",
                DIContainer.resolve(ShowSortingTechniciansAlphabeticallyAction.class), null));
        sortingOptions.put(2, new MenuItem("Show technicians sorting by occupancy",
                DIContainer.resolve(ShowSortingTechniciansByOccupancyAction.class), null));

        return new Menu("Technician sorting options", sortingOptions);
    }

    private Menu buildCarPlacesMenu() {
        Map<Integer, MenuItem> spotItems = new HashMap<>();
        spotItems.put(0, new MenuItem("Exit", null, null));
        spotItems.put(1, new MenuItem("Create car place",
                DIContainer.resolve(CreateCarPlaceAction.class), null));
        spotItems.put(2, new MenuItem("Remove car place",
                DIContainer.resolve(RemoveCarPlaceAction.class), null));
        spotItems.put(3, new MenuItem("Show list of available car places",
                DIContainer.resolve(ShowAvailableCarPlacesAction.class), null));
        spotItems.put(4, new MenuItem("Show the number of available car places on a specified date in the future",
                DIContainer.resolve(ShowAvailableCarPlacesInFutureAction.class), null));
        spotItems.put(5, new MenuItem("Upload car places data from an imported CSV file",
                DIContainer.resolve(ImportCarPlacesAction.class), null));
        spotItems.put(6, new MenuItem("Download car places data to an exportable CSV file",
                DIContainer.resolve(ExportCarPlacesAction.class), null));

        return new Menu("Car places management", spotItems);
    }
}