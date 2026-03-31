import java.util.*;

public class Main {
    public static void main(String[] args) {
        Autoservice autoservice = new Autoservice();
        autoservice.initializeDefaultData();

        // Запускать 1 из 4 реализованных функций

        // addRemoveTechnicianScript(autoservice);
        // addRemoveCarPlaceScript(autoservice);
        // addRemoveCloseCancelOrderScript(autoservice);
        // shiftInTimeOrderScript(autoservice);

        // Новые

        // availableCarPlacesList(autoservice);
        // sortingOrdersVariousTypes(autoservice);
        // sortingTechniciansVariousTypes(autoservice);
        // sortingCurrentlyRunningOrdersVariousTypes(autoservice);
        // findOrderByTechnician(autoservice);
        // findTechniciansByOrder(autoservice);
        // filterOrdersByTimePeriod(autoservice);
        // availableCarPlacesNumberInFuture(autoservice);
        availableDatesNumberInFuture(autoservice);
    }

    public static void addRemoveTechnicianScript(Autoservice a){
        System.out.println("--- Technician is being added to the autoservice. ---");
        List<Technician> technicianList = a.getTechnicians();
        System.out.println(technicianList);
        Technician newTechnician = new Technician(4, "Anthony Mitchell", "probationer, continuing student");
        a.addTechnician(newTechnician);
        technicianList = a.getTechnicians();
        System.out.println(technicianList);
        System.out.println("--- Technician is being removed from the autoservice. ---");
        Technician removeTechnician = technicianList.get(0);
        a.removeTechnician(removeTechnician);
        technicianList = a.getTechnicians();
        System.out.println(technicianList);
    }

    public static void addRemoveCarPlaceScript(Autoservice a){
        System.out.println("--- Place is being added to the autoservice. ---");
        List<CarPlace> placeList = a.getCarPlaces();
        System.out.println(placeList);
        Random r = new Random();
        CarPlace newPlace = new CarPlace(3, 20 + r.nextInt(0, 10), r.nextBoolean());
        a.addCarPlace(newPlace);
        placeList = a.getCarPlaces();
        System.out.println(placeList);
        System.out.println("--- Place is being removed from the autoservice. ---");
        CarPlace removePlace = placeList.get(0);
        a.removeCarPlace(removePlace);
        placeList = a.getCarPlaces();
        System.out.println(placeList);
    }

    public static void addRemoveCloseCancelOrderScript(Autoservice a){
        System.out.println("--- Order is being added in the system. ---");
        List<Order> orderList = a.getOrders();
        System.out.println(orderList);
        Random r = new Random();
        int d   = 21+ r.nextInt(0, 4);
        int h1  = 9 + r.nextInt(0, 8);
        int h2  = 9 + r.nextInt(0, 4);
        int h3  = 13+ r.nextInt(0, 4);
        List<Technician> tech = new ArrayList<>();
        tech.add(a.getTechnicians().getFirst());
        Order newOrder = new Order(orderList.size(), a.getServiceAdvisors().getFirst(),
                tech,a.getCustomers().getFirst(), a.getCarPlaces().getFirst(),
                new Date(2025, 9, d-1, h1, 0),
                new Date(2025, 9, d, h2, 0),
                new Date(2025, 9, d, h2, 10),
                new Date(2025, 9, d, h3, 0));
        a.addOrder(newOrder);
        orderList = a.getOrders();
        System.out.println(orderList);
        System.out.println("--- Order is being removed from the system. ---");
        Order removeOrder = orderList.get(0);
        a.removeOrder(removeOrder);
        orderList = a.getOrders();
        System.out.println(orderList);
        System.out.println("--- Closing an order at a autoservice. ---");
        System.out.println(orderList.get(1));
        orderList.get(1).setStatus(OrderStatus.COMPLETED);
        System.out.println(orderList.get(1));
        System.out.println("--- Cancelling an order at a autoservice. ---");
        System.out.println(orderList.get(2));
        orderList.get(2).setStatus(OrderStatus.CANCELLED);
        System.out.println(orderList.get(2));
    }

    public static void shiftInTimeOrderScript(Autoservice a){
        System.out.println("--- Order fulfillment time shift. ---");
        System.out.println("Create order following the current one, which will conflict with it.");
        Order delayedOrder = a.getOrders().get(0);
        delayedOrder.setStatus(OrderStatus.IN_PROGRESS);
        System.out.println("Current order:");
        System.out.println(delayedOrder);
        Random r = new Random();
        int d   = delayedOrder.getStartDate().getDay();
        int h1  = 9 + r.nextInt(0, 8);
        int h2  = delayedOrder.getEndDate().getHours();
        int h3  = h2 + r.nextInt(0, 4);
        List<Technician> tech = new ArrayList<>();
        tech.add(a.getTechnicians().getFirst());
        Order newOrder = new Order(a.getOrders().size(), a.getServiceAdvisors().getFirst(),
                tech, a.getCustomers().getFirst(), a.getCarPlaces().getFirst(),
                new Date(2025, 9, d-1, h1, 0),
                new Date(2025, 9, d, h2, 0),
                new Date(2025, 9, d, h2, 10),
                new Date(2025, 9, d, h3, 0));
        a.addOrder(newOrder);
        System.out.println("Next order:");
        System.out.println(a.getOrders().get(a.getOrders().size()-1));
        System.out.println("The current order is delayed. Rescheduling the next order due to delays in the current order.");
        int delayTime = r.nextInt(0, 4);
        Order nextOrder = a.getOrders().get(a.getOrders().size()-1);
        System.out.println("Next order:");
        System.out.println(nextOrder);
        System.out.println("New schedule for the next order.");
        nextOrder.getStartDate().setHours(delayTime + nextOrder.getStartDate().getHours());
        nextOrder.getEndDate().setHours(delayTime + nextOrder.getEndDate().getHours());
        System.out.println(nextOrder);
        System.out.println("Current order:");
        System.out.println(delayedOrder);
        System.out.println("New schedule for the next order.");
        delayedOrder.getStartDate().setHours(delayTime + delayedOrder.getStartDate().getHours());
        delayedOrder.getEndDate().setHours(delayTime + delayedOrder.getEndDate().getHours());
        System.out.println(delayedOrder);
    }

    public static void availableCarPlacesList(Autoservice a){
        System.out.println("--- Getting a list of available car places. ---");
        System.out.println(a.availableCarPlacesList());

        System.out.println("P.S. Checking the method when parking spaces are occupied.");
        for (int i = 0; i < a.getCarPlaces().size(); i++)
            a.getCarPlaces().get(i).setOccupied(true);
        System.out.println(a.availableCarPlacesList());
    }

    public static void sortingOrdersVariousTypes(Autoservice a){
        System.out.println("--- Sorted list of orders. ---");
        System.out.println("By start date:");
        System.out.println(a.sortingByStartDateOrders());
        System.out.println("By end date:");
        System.out.println(a.sortingByEndDateOrders());
        System.out.println("By loading date:");
        System.out.println(a.sortingByLoadingDateOrders());
        System.out.println("By cost:");
        for (int i = 0; i < a.getOrders().size(); i++) {
            a.getOrders().get(i).setCost(new Random().nextInt(200, 400));
        }
        System.out.println(a.sortingByCostOrders());
    }

    public static void sortingTechniciansVariousTypes(Autoservice a){
        System.out.println("--- Sorted list of technicians. ---");
        System.out.println("Alphabetically:");
        System.out.println(a.sortingTechniciansAlphabetically());
        System.out.println("By occupancy:");
        a.getTechnician(1).setAvailable(false);
        a.getTechnician(2).setAvailable(false);
        System.out.println(a.sortingTechniciansByOccupancy());
    }

    public static void sortingCurrentlyRunningOrdersVariousTypes(Autoservice a){
        System.out.println("--- Sorted list of currently running orders. ---");
        a.getOrders().get(0).setStatus(OrderStatus.IN_PROGRESS);
        a.getOrders().get(2).setStatus(OrderStatus.IN_PROGRESS);
        System.out.println("By start date:");
        System.out.println(a.sortingByStartDateCurrentlyRunningOrders());
        System.out.println("By end date:");
        System.out.println(a.sortingByEndDateCurrentlyRunningOrders());
        System.out.println("By loading date:");
        System.out.println(a.sortingByCostCurrentlyRunningOrders());
    }

    public static void findOrderByTechnician(Autoservice a){
        System.out.println("--- Find order by technician. ---");
        System.out.println(a.findOrderByTechnician(a.getTechnician(1)));
    }

    public static void findTechniciansByOrder(Autoservice a){
        System.out.println("--- Find technician(-s) by order. ---");
        System.out.println(a.findTechniciansByOrder(a.getOrder(1)));
    }

    public static void filterOrdersByTimePeriod(Autoservice a){
        System.out.println("--- Show orders for a period of time. ---");
        Random r = new Random();
        int d   = 22+ r.nextInt(0, 2);
        int h1  = 9 + r.nextInt(0, 2);
        int h2  = 16+ r.nextInt(0, 2);
        Date firstDate = new Date(2025, 9, d-1, h1, 0);
        Date secondDate = new Date(2025, 9, d, h2, 0);
        List<Date> dateList = new ArrayList<>();
        dateList.add(firstDate);
        dateList.add(secondDate);
        System.out.println("Time period: " + dateList);
        System.out.println(a.ordersOverPeriodTimeStrictEquality(dateList, OrderStatus.PENDING, new OrderStartDateComparator()));

    }

    public static void availableCarPlacesNumberInFuture(Autoservice a){
        System.out.println("---The number of available car places on any future date. ---");

        int month = 9;
        int date = 29;

        List<Technician> technicians1 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(0)));
        List<Technician> technicians2 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(1)));
        List<Technician> technicians3 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(2)));

        List<Technician> technicians4 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(3)));
        List<Technician> technicians5 = new ArrayList<>();
        technicians5.add(a.getTechnicians().get(0));
        technicians5.add(a.getTechnicians().get(2));
        List<Technician> technicians6 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(1)));
        List<Technician> technicians7 = new ArrayList<>();
        technicians7.add(a.getTechnicians().get(0));
        technicians7.add(a.getTechnicians().get(2));
        technicians7.add(a.getTechnicians().get(3));

        Order order1 = new Order(4, a.getServiceAdvisors().getFirst(), technicians1,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-1, 15, 0),
                new Date(2025, month, date, 8, 0),
                new Date(2025, month, date, 8, 10),
                new Date(2025, month, date, 11, 0));
        Order order2 = new Order(5, a.getServiceAdvisors().getFirst(), technicians2,
                a.getCustomers().getFirst(), a.getCarPlaces().get(1),
                new Date(2025, month, date-2, 13, 0),
                new Date(2025, month, date, 9, 0),
                new Date(2025, month, date, 9, 10),
                new Date(2025, month, date, 12, 0));
        Order order3 = new Order(6, a.getServiceAdvisors().getFirst(), technicians3,
                a.getCustomers().getFirst(), a.getCarPlaces().get(2),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 12, 0));
        Order order4 = new Order(7, a.getServiceAdvisors().getFirst(), technicians4,
                a.getCustomers().getFirst(), a.getCarPlaces().get(1),
                new Date(2025, month, date-1, 12, 0),
                new Date(2025, month, date, 13, 0),
                new Date(2025, month, date, 13, 10),
                new Date(2025, month, date, 16, 0));
        Order order5 = new Order(8, a.getServiceAdvisors().getFirst(), technicians5,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 14, 0),
                new Date(2025, month, date, 14, 10),
                new Date(2025, month, date, 16, 0));
        Order order6 = new Order(8, a.getServiceAdvisors().getFirst(), technicians6,
                a.getCustomers().getFirst(), a.getCarPlaces().get(2),
                new Date(2025, month, date-2, 15, 0),
                new Date(2025, month, date, 15, 0),
                new Date(2025, month, date, 15, 10),
                new Date(2025, month, date, 18, 0));
        Order order7 = new Order(9, a.getServiceAdvisors().getFirst(), technicians7,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-1, 11, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 18, 0));

        a.addOrder(order1);
        a.addOrder(order2);
        a.addOrder(order3);
        a.addOrder(order4);
        a.addOrder(order5);
        a.addOrder(order6);
        a.addOrder(order7);

        a.getDailyManagement().groupOrdersByDate(a.getOrders());
        Date definiteDate = new Date(2025, month, date);
        System.out.println("Number of available car places for the selected date [" +
                definiteDate + "]: " + a.getDailyManagement().getAvailableCarPlaces(definiteDate));

    }

    public static void availableDatesNumberInFuture(Autoservice a) {
        System.out.println("--- Time intervals of the current day for driving a car to the service. ---");

        int month = 9;
        int date = 29;

        List<Technician> technicians1 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(0)));
        List<Technician> technicians2 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(1)));
        List<Technician> technicians3 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(2)));

        List<Technician> technicians4 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(3)));
        List<Technician> technicians5 = new ArrayList<>();
        technicians5.add(a.getTechnicians().get(0));
        technicians5.add(a.getTechnicians().get(2));
        List<Technician> technicians6 =
                new ArrayList<>(Collections.singleton(a.getTechnicians().get(1)));
        List<Technician> technicians7 = new ArrayList<>();
        technicians7.add(a.getTechnicians().get(0));
        technicians7.add(a.getTechnicians().get(2));
        technicians7.add(a.getTechnicians().get(3));

        Order order1 = new Order(4, a.getServiceAdvisors().getFirst(), technicians1,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-1, 15, 0),
                new Date(2025, month, date, 8, 0),
                new Date(2025, month, date, 8, 10),
                new Date(2025, month, date, 11, 0));
        Order order2 = new Order(5, a.getServiceAdvisors().getFirst(), technicians2,
                a.getCustomers().getFirst(), a.getCarPlaces().get(1),
                new Date(2025, month, date-2, 13, 0),
                new Date(2025, month, date, 9, 0),
                new Date(2025, month, date, 9, 10),
                new Date(2025, month, date, 12, 0));
        Order order3 = new Order(6, a.getServiceAdvisors().getFirst(), technicians3,
                a.getCustomers().getFirst(), a.getCarPlaces().get(2),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 12, 0));
        Order order4 = new Order(7, a.getServiceAdvisors().getFirst(), technicians4,
                a.getCustomers().getFirst(), a.getCarPlaces().get(1),
                new Date(2025, month, date-1, 12, 0),
                new Date(2025, month, date, 13, 0),
                new Date(2025, month, date, 13, 10),
                new Date(2025, month, date, 16, 0));
        Order order5 = new Order(8, a.getServiceAdvisors().getFirst(), technicians5,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 14, 0),
                new Date(2025, month, date, 14, 10),
                new Date(2025, month, date, 16, 0));
        Order order6 = new Order(8, a.getServiceAdvisors().getFirst(), technicians6,
                a.getCustomers().getFirst(), a.getCarPlaces().get(2),
                new Date(2025, month, date-2, 15, 0),
                new Date(2025, month, date, 15, 0),
                new Date(2025, month, date, 15, 10),
                new Date(2025, month, date, 18, 0));
        Order order7 = new Order(9, a.getServiceAdvisors().getFirst(), technicians7,
                a.getCustomers().getFirst(), a.getCarPlaces().get(0),
                new Date(2025, month, date-1, 11, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 18, 0));

        a.addOrder(order1);
        a.addOrder(order2);
        a.addOrder(order3);
        a.addOrder(order4);
        a.addOrder(order5);
        a.addOrder(order6);
        a.addOrder(order7);

        a.getDailyManagement().groupOrdersByDate(a.getOrders());
        Date definiteDate = new Date(2025, month, date, 9, 52);
        Map<Integer, Integer> map = a.getDailyManagement().getAvailableDate(definiteDate);
        System.out.println("Available slots for car repairs [" + definiteDate + "]:");
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("- from " + entry.getKey() + " to " + (entry.getValue()+1) + " hours");
        }

    }

}