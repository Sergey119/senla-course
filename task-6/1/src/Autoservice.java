import java.util.*;

public class Autoservice {
    private static Autoservice instance;

    private Map<Integer, ServiceAdvisor> serviceAdvisors = new HashMap<>();
    private Map<Integer, Technician> technicians = new HashMap<>();
    private Map<Integer, CarPlace> carPlaces = new HashMap<>();
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, Order> orders = new HashMap<>();
    private DailyManagement dailyManagement = new DailyManagement();

    private Autoservice() {}
    public static synchronized Autoservice getInstance() {
        if (instance == null) {
            instance = new Autoservice();
        }
        return instance;
    }

    public void initializeDefaultData() {
//        addDefaultServiceAdvisor();
//        addDefaultCustomer();
//
//        addDefaultTechnician(0, "Brian Carter", "Auto electrician");
//        addDefaultTechnician(1, "Nathan Cole", "Technical engineer");
//        addDefaultTechnician(2, "Herbert Bates", "Motor mechanic");
//        addDefaultTechnician(3, "Steven Hardy", "Diagnostic wizard");
//
//        // От числа машиномест будет зависеть количество заказов,
//        // поэтому сначала производится генерация машиномест,
//        // а потом заказов
//        for (int i = 0; i < 3; i++) { addDefaultCarPlace(i); }
//        addDefaultOrder();
//        addDefaultWorkScheduleInDailyManagement();
//
//        addMoreDefaultInfo();
    }

    public Map<Integer, ServiceAdvisor> getServiceAdvisors() { return serviceAdvisors; }
    public void setServiceAdvisors(Map<Integer, ServiceAdvisor> serviceAdvisors) { this.serviceAdvisors = serviceAdvisors; }
    public ServiceAdvisor getServiceAdvisor(int i) { return serviceAdvisors.get(i); }

    public Map<Integer, Technician> getTechnicians() { return technicians; }
    public void setTechnicians(Map<Integer, Technician> technicians) { this.technicians = technicians; }
    public Technician getTechnician(int i) { return technicians.get(i); }
    public void addTechnician(Technician technician) {
        technicians.put(technician.getId(), technician);
        System.out.println("Added technician: " + technician);
    }
    public void removeTechnician(int i) {
        Technician technician = technicians.get(i);
        technicians.remove(i);
        System.out.println("Removed technician: " + technician);
    }
    public List<Technician> sortingTechniciansAlphabetically() {
        List<Technician> technicians = new ArrayList<>(this.technicians.values());
        technicians.sort(new TechnicianAlphabeticalComparator());
        return technicians;
    }
    public List<Technician> sortingTechniciansByOccupancy() {
        List<Technician> technicians = new ArrayList<>(this.technicians.values());
        technicians.sort(new TechnicianOccupancyComparator());
        return technicians;
    }
    public List<Technician> findTechniciansByOrder(int i) {
        if (orders.containsKey(i)) {
            return new ArrayList<>(orders.get(i).getTechnicians().values());
        } else {
            return null;
        }
    }

    public Map<Integer, CarPlace> getCarPlaces() { return carPlaces; }
    public void setCarPlaces(Map<Integer, CarPlace> carPlaces) { this.carPlaces = carPlaces; }
    public CarPlace getCarPlace(int i) { return carPlaces.get(i); }
    public void addCarPlace(CarPlace carPlace) {
        carPlaces.put(carPlace.getId(), carPlace);
        System.out.println("Added place: " + carPlace);
    }
    public void removeCarPlace(int i) {
        CarPlace carPlace = carPlaces.get(i);
        carPlaces.remove(i);
        System.out.println("Removed place: " + carPlace);
    }
    public List<CarPlace> availableCarPlacesList() {
        List<CarPlace> availableCarPlaces = new ArrayList<>();
        List<CarPlace> carPlaces = new ArrayList<>(this.carPlaces.values());
        for (CarPlace carPlace : carPlaces) {
            if (!carPlace.isOccupied()) {
                availableCarPlaces.add(carPlace);
            }
        }
        return availableCarPlaces;
    }

    public Map<Integer, Customer> getCustomers() { return customers; }
    public void setCustomers(Map<Integer, Customer> customers) { this.customers = customers; }
    public Customer getCustomer(int i) { return customers.get(i); }

    public Map<Integer, Order> getOrders() { return orders; }
    public void setOrders(Map<Integer, Order> orders) { this.orders = orders; }
    public Order getOrder(int i) { return orders.get(i); }
    public void addOrder(Order order) {
        orders.put(order.getId(), order);
        System.out.println("Added order: " + order);
    }
    public void removeOrder(int i) {
        Order order = orders.get(i);
        orders.remove(i);
        System.out.println("Removed order: " + order);
    }
    public void completeOrder(int i) {
        orders.get(i).setStatus(OrderStatus.COMPLETED);
    }
    public void cancelOrder(int i) {
        orders.get(i).setStatus(OrderStatus.CANCELLED);
    }
    public void shiftOrderTime(int i, int j) {
        orders.get(i).getStartDate().setHours(j + orders.get(i).getStartDate().getHours());
        orders.get(i).getLoadingDate().setHours(j + orders.get(i).getLoadingDate().getHours());
        orders.get(i).getEndDate().setHours(j + orders.get(i).getEndDate().getHours());
    }
    public List<Order> sortingByStartDateOrders() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(new OrderStartDateComparator());
        return orders;
    }
    public List<Order> sortingByEndDateOrders() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(new OrderEndDateComparator());
        return orders;
    }
    public List<Order> sortingByLoadingDateOrders() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(new OrderLoadingDateComparator());
        return orders;
    }
    public List<Order> sortingByCostOrders() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(new OrderCostComparator());
        return orders;
    }
    public List<Order> sortingByStartDateCurrentlyRunningOrders() {
        List<Order> sortedOrders = new ArrayList<>();
        List<Order> orders = new ArrayList<>(this.orders.values());
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                sortedOrders.add(order);
            }
        }
        sortedOrders.sort(new OrderStartDateComparator());
        return sortedOrders;
    }
    public List<Order> sortingByEndDateCurrentlyRunningOrders() {
        List<Order> sortedOrders = new ArrayList<>();
        List<Order> orders = new ArrayList<>(this.orders.values());
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                sortedOrders.add(order);
            }
        }
        sortedOrders.sort(new OrderEndDateComparator());
        return sortedOrders;
    }
    public List<Order> sortingByCostCurrentlyRunningOrders() {
        List<Order> sortedOrders = new ArrayList<>();
        List<Order> orders = new ArrayList<>(this.orders.values());
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                sortedOrders.add(order);
            }
        }
        sortedOrders.sort(new OrderCostComparator());
        return sortedOrders;
    }
    public List<Order> findOrderByTechnician(int i) {
        if (technicians.containsKey(i)) {
            List<Order> orders = new ArrayList<>();
            for (Order order : this.orders.values()) {
                if (order.getTechnicians().containsKey(i)) {
                    orders.add(order);
                }
            }
            return orders;
        } else {
            return null;
        }
    }
    public List<Order> ordersOverPeriodTimeStrictEquality(List<Date> timePeriod,
        OrderStatus status, OrderStartDateEndDateCostOnlyComparator comparator) {

        if (status == OrderStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Invalid status " + status + " detected");
        }

        List<Order> sortedOrders = new ArrayList<>();
        List<Order> orders = new ArrayList<>(this.orders.values());
        for (Order order : orders) {
            if (order.getStatus() == status) {
                if (timePeriodMatching(order, timePeriod)) {
                    sortedOrders.add(order);
                }
            }
        }
        sortedOrders.sort(comparator);
        return sortedOrders;
    }

    public DailyManagement getDailyManagement() { return dailyManagement; }
    public void setDailyManagement(DailyManagement dailyManagement) { this.dailyManagement = dailyManagement; }

    private boolean timePeriodMatching(Order order, List<Date> timePeriod) {
        if ((order.getStartDate().getYear() >= timePeriod.get(0).getYear())
            && (order.getEndDate().getYear() <= timePeriod.get(1).getYear())) {
            if ((order.getStartDate().getMonth() >= timePeriod.get(0).getMonth())
                && (order.getEndDate().getMonth() <= timePeriod.get(1).getMonth())) {
                if ((order.getStartDate().getDay() >= timePeriod.get(0).getDay())
                    && (order.getEndDate().getDay() <= timePeriod.get(1).getDay())) {
                    if ((order.getStartDate().getHours() >= timePeriod.get(0).getHours())
                        && (order.getEndDate().getHours() <= timePeriod.get(1).getHours())) {
                        if ((order.getStartDate().getMinutes() >= timePeriod.get(0).getMinutes())
                            && (order.getEndDate().getMinutes() <= timePeriod.get(1).getMinutes())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void addDefaultServiceAdvisor() {
        ServiceAdvisor serviceAdvisor = new ServiceAdvisor(0, "James Parker");
        serviceAdvisors.put(serviceAdvisor.getId(), serviceAdvisor);
        System.out.println("Added service advisor: " + serviceAdvisor);
    }
    private void addDefaultTechnician(int i, String name, String sp) {
        Technician technician = new Technician(i, name, sp);
        technicians.put(technician.getId(), technician);
        System.out.println("Added technician: " + technician);
    }
    private void addDefaultCarPlace(int i) {
        Random r = new Random();
        CarPlace place = new CarPlace(i, 20 + r.nextInt(0, 10), r.nextBoolean());
        carPlaces.put(place.getId(), place);
        System.out.println("Added place: " + place);
    }
    private void addDefaultCustomer() {
        Customer customer = new Customer(0, "Raymond Anderson");
        customers.put(customer.getId(), customer);
        System.out.println("Added customer: " + customer);
    }
    private void addDefaultOrder() {
        int size = carPlaces.size();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int technicianNumber = r.nextInt(1, technicians.size());
            Set<Technician> techniciansSet = new HashSet<>();
            for (int j = 0; j < technicianNumber; j++) {
                int randomTechnician = r.nextInt(technicians.size());
                if (!techniciansSet.contains(technicians.get(randomTechnician))) {
                    techniciansSet.add(technicians.get(randomTechnician));
                } else {
                    j--;
                }
            }
            Map<Integer, Technician> techniciansMap = new HashMap<>();
            for(Technician technician : techniciansSet) {
                techniciansMap.put(technician.getId(), technician);
            }

            int d   = 21+ r.nextInt(0, 4);
            int h1  = 9 + r.nextInt(0, 8);
            int h2  = 9 + r.nextInt(0, 4);
            int h3  = 13+ r.nextInt(0, 4);
            Order order = new Order(i, serviceAdvisors.get(0), techniciansMap,
                    customers.get(0), carPlaces.get(i),
                    new Date(2025, 9, d-1, h1, 0),
                    new Date(2025, 9, d, h2, 0),
                    new Date(2025, 9, d, h2, 10),
                    new Date(2025, 9, d, h3, 0));
            orders.put(order.getId(), order);
            System.out.println("Added order: " + order);
        }
    }

    private void addMoreDefaultInfo() {
        int month = 9;
        int date = 29;

        Map<Integer, Technician> technicians1 = new HashMap<>();
        technicians1.put(0, technicians.get(0));
        Map<Integer, Technician> technicians2 = new HashMap<>();
        technicians2.put(1, technicians.get(1));
        Map<Integer, Technician> technicians3 = new HashMap<>();
        technicians3.put(2, technicians.get(2));
        Map<Integer, Technician> technicians4 = new HashMap<>();
        technicians4.put(3, technicians.get(3));
        Map<Integer, Technician> technicians5 = new HashMap<>();
        technicians5.put(0, technicians.get(0));
        technicians5.put(2, technicians.get(2));
        Map<Integer, Technician> technicians6 = new HashMap<>();
        technicians6.put(1, technicians.get(1));
        Map<Integer, Technician> technicians7 = new HashMap<>();
        technicians7.put(0, technicians.get(0));
        technicians7.put(2, technicians.get(2));
        technicians7.put(3, technicians.get(3));

        Order order1 = new Order(3, serviceAdvisors.get(0), technicians1,
                customers.get(0), carPlaces.get(0),
                new Date(2025, month, date-1, 15, 0),
                new Date(2025, month, date, 8, 0),
                new Date(2025, month, date, 8, 10),
                new Date(2025, month, date, 11, 0));
        Order order2 = new Order(4, serviceAdvisors.get(0), technicians2,
                customers.get(0), carPlaces.get(1),
                new Date(2025, month, date-2, 13, 0),
                new Date(2025, month, date, 9, 0),
                new Date(2025, month, date, 9, 10),
                new Date(2025, month, date, 12, 0));
        Order order3 = new Order(5, serviceAdvisors.get(0), technicians3,
                customers.get(0), carPlaces.get(2),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 10, 0),
                new Date(2025, month, date, 12, 0));
        Order order4 = new Order(6, serviceAdvisors.get(0), technicians4,
                customers.get(0), carPlaces.get(1),
                new Date(2025, month, date-1, 12, 0),
                new Date(2025, month, date, 13, 0),
                new Date(2025, month, date, 13, 10),
                new Date(2025, month, date, 16, 0));
        Order order5 = new Order(7, serviceAdvisors.get(0), technicians5,
                customers.get(0), carPlaces.get(0),
                new Date(2025, month, date-2, 16, 0),
                new Date(2025, month, date, 14, 0),
                new Date(2025, month, date, 14, 10),
                new Date(2025, month, date, 16, 0));
        Order order6 = new Order(8, serviceAdvisors.get(0), technicians6,
                customers.get(0), carPlaces.get(2),
                new Date(2025, month, date-2, 15, 0),
                new Date(2025, month, date, 15, 0),
                new Date(2025, month, date, 15, 10),
                new Date(2025, month, date, 18, 0));
        Order order7 = new Order(9, serviceAdvisors.get(0), technicians7,
                customers.get(0), carPlaces.get(0),
                new Date(2025, month, date-1, 11, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 17, 0),
                new Date(2025, month, date, 18, 0));

        orders.put(order1.getId(), order1);
        orders.put(order2.getId(), order2);
        orders.put(order3.getId(), order3);
        orders.put(order4.getId(), order4);
        orders.put(order5.getId(), order5);
        orders.put(order6.getId(), order6);
        orders.put(order7.getId(), order7);
    }

    private void addDefaultWorkScheduleInDailyManagement() {
        List<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(18);
        dailyManagement.setWorkSchedule(new ArrayList<>(list));
       // dailyManagement.setWorkSchedule(new int[]{8, 18});
    }
}
