import java.util.*;

@Singleton
public class Autoservice {
    private Map<Integer, ServiceAdvisor> serviceAdvisors = new HashMap<>();
    private Map<Integer, Technician> technicians = new HashMap<>();
    private Map<Integer, CarPlace> carPlaces = new HashMap<>();
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, Order> orders = new HashMap<>();
    private DailyManagement dailyManagement = new DailyManagement();

    public Autoservice() { /* Для DI */ }

    public void initializeDefaultData() {
//        addDefaultServiceAdvisor();
//        addDefaultCustomer();
//        addDefaultWorkScheduleInDailyManagement();
    }

    public Map<Integer, ServiceAdvisor> getServiceAdvisors() { return serviceAdvisors; }
    public void setServiceAdvisors(Map<Integer, ServiceAdvisor> serviceAdvisors) { this.serviceAdvisors = serviceAdvisors; }
    public ServiceAdvisor getServiceAdvisor(int key) { return serviceAdvisors.get(key); }

    public Map<Integer, Technician> getTechnicians() { return technicians; }
    public void setTechnicians(Map<Integer, Technician> technicians) { this.technicians = technicians; }
    public Technician getTechnician(int key) { return technicians.get(key); }
    public void addTechnician(Technician technician) {
        technicians.put(technician.getId(), technician);
        System.out.println("Added technician: " + technician);
    }
    public void removeTechnician(int key) {
        Technician technician = technicians.get(key);
        technicians.remove(key);
        System.out.println("Removed technician: " + technician);
    }
    public List<Technician> sortingTechniciansAlphabetically() {
        return technicians.values().stream()
                .sorted(new TechnicianAlphabeticalComparator())
                .toList();
    }
    public List<Technician> sortingTechniciansByOccupancy() {
        return technicians.values().stream()
                .sorted(new TechnicianOccupancyComparator())
                .toList();
    }
    public List<Technician> findTechniciansByOrder(int key) {
        return Optional.ofNullable(orders.get(key))
                .map(order -> order.getTechnicians().values().stream().toList())
                .orElse(null);
    }

    public Map<Integer, CarPlace> getCarPlaces() { return carPlaces; }
    public void setCarPlaces(Map<Integer, CarPlace> carPlaces) { this.carPlaces = carPlaces; }
    public CarPlace getCarPlace(int key) { return carPlaces.get(key); }
    public void addCarPlace(CarPlace carPlace) {
        carPlaces.put(carPlace.getId(), carPlace);
        System.out.println("Added place: " + carPlace);
    }
    public void removeCarPlace(int key) {
        CarPlace carPlace = carPlaces.get(key);
        carPlaces.remove(key);
        System.out.println("Removed place: " + carPlace);
    }
    public List<CarPlace> availableCarPlacesList() {
        return carPlaces.values().stream()
                .filter(carPlace -> !carPlace.isOccupied())
                .toList();
    }

    public Map<Integer, Customer> getCustomers() { return customers; }
    public void setCustomers(Map<Integer, Customer> customers) { this.customers = customers; }
    public Customer getCustomer(int key) { return customers.get(key); }

    public Map<Integer, Order> getOrders() { return orders; }
    public void setOrders(Map<Integer, Order> orders) { this.orders = orders; }
    public Order getOrder(int key) { return orders.get(key); }
    public void addOrder(Order order) {
        orders.put(order.getId(), order);
        System.out.println("Added order: " + order);
    }
    public void removeOrder(int key) {
        Order order = orders.get(key);
        orders.remove(key);
        System.out.println("Removed order: " + order);
    }
    public void completeOrder(int key) {
        orders.get(key).setStatus(OrderStatus.COMPLETED);
    }
    public void cancelOrder(int key) {
        orders.get(key).setStatus(OrderStatus.CANCELLED);
    }
    public void shiftOrderTime(int key, int time) {
        orders.get(key).getStartDate().setHours(time + orders.get(key).getStartDate().getHours());
        orders.get(key).getLoadingDate().setHours(time + orders.get(key).getLoadingDate().getHours());
        orders.get(key).getEndDate().setHours(time + orders.get(key).getEndDate().getHours());
    }
    public List<Order> sortingByStartDateOrders() {
        return orders.values().stream()
                .sorted(new OrderStartDateComparator())
                .toList();
    }
    public List<Order> sortingByEndDateOrders() {
        return orders.values().stream()
                .sorted(new OrderEndDateComparator())
                .toList();
    }
    public List<Order> sortingByLoadingDateOrders() {
        return orders.values().stream()
                .sorted(new OrderLoadingDateComparator())
                .toList();
    }
    public List<Order> sortingByCostOrders() {
        return orders.values().stream()
                .sorted(new OrderCostComparator())
                .toList();
    }
    public List<Order> sortingByStartDateCurrentlyRunningOrders() {
        return orders.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.IN_PROGRESS)
                .sorted(new OrderStartDateComparator())
                .toList();
    }
    public List<Order> sortingByEndDateCurrentlyRunningOrders() {
        return orders.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.IN_PROGRESS)
                .sorted(new OrderEndDateComparator())
                .toList();
    }
    public List<Order> sortingByCostCurrentlyRunningOrders() {
        return orders.values().stream()
                .filter(order -> order.getStatus() == OrderStatus.IN_PROGRESS)
                .sorted(new OrderCostComparator())
                .toList();
    }
    public List<Order> findOrderByTechnician(int key) {
        var strategyHandlers = new HashMap<Boolean, ITechnicianExistenceResponseHandler>();
        strategyHandlers.put(true, new TechnicianExistsHandler());
        strategyHandlers.put(false, new TechnicianNotFoundHandler());

        var responseHandler = strategyHandlers.get(technicians.containsKey(key));
        return responseHandler.handleResponse(key);

    }
    public List<Order> ordersOverPeriodTimeStrictEquality(List<Date> timePeriod,
        OrderStatus status, OrderStartDateEndDateCostOnlyComparator comparator) {

        if (status == OrderStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Invalid status " + status + " detected");
        }

        return this.orders.values().stream()
                .filter(order -> order.getStatus() == status && timePeriodMatching(order, timePeriod))
                .sorted(comparator).toList();
    }

    public DailyManagement getDailyManagement() { return dailyManagement; }
    public void setDailyManagement(DailyManagement dailyManagement) { this.dailyManagement = dailyManagement; }

    public void updateFrom(Autoservice other) {
        if (other == null) {
            return;
        }
        this.serviceAdvisors = other.serviceAdvisors;
        this.technicians = other.technicians;
        this.carPlaces = other.carPlaces;
        this.customers = other.customers;
        this.orders = other.orders;
        this.dailyManagement = other.dailyManagement;
        System.out.println("Autoservice updated from deserialized data."); // Логирование
    }

    private boolean timePeriodMatching(Order order, List<Date> timePeriod) {
        return (order.getStartDate().getTime() >= timePeriod.get(0).getTime())
                && (order.getEndDate().getTime() <= timePeriod.get(1).getTime());
    }

    private void addDefaultServiceAdvisor() {
        ServiceAdvisor serviceAdvisor = new ServiceAdvisor(0, "James Parker");
        serviceAdvisors.put(serviceAdvisor.getId(), serviceAdvisor);
        System.out.println("Added service advisor: " + serviceAdvisor);
    }
    private void addDefaultCustomer() {
        Customer customer = new Customer(0, "Raymond Anderson");
        customers.put(customer.getId(), customer);
        System.out.println("Added customer: " + customer);
    }
    private void addDefaultWorkScheduleInDailyManagement() {
        List<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(18);
        dailyManagement.setWorkSchedule(new ArrayList<>(list));
        // dailyManagement.setWorkSchedule(new int[]{8, 18});
    }

    @Override
    public String toString() {
        return "Autoservice{" +
                "serviceAdvisors=" + serviceAdvisors +
                ",\ntechnicians=" + technicians +
                ",\ncarPlaces=" + carPlaces +
                ",\ncustomers=" + customers +
                ",\norders=" + orders +
                ",\ndailyManagement=" + dailyManagement +
                '}';
    }
}
