import java.util.*;

public class Autoservice {
    private List<ServiceAdvisor> serviceAdvisors = new ArrayList<>();
    private List<Technician> technicians = new ArrayList<>();
    private List<CarPlace> carPlaces = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private DailyManagement dailyManagement = new DailyManagement();

    public void initializeDefaultData() {
        addDefaultServiceAdvisor();
        addDefaultCustomer();

        addDefaultTechnician(0, "Brian Carter", "Auto electrician");
        addDefaultTechnician(1, "Nathan Cole", "Technical engineer");
        addDefaultTechnician(2, "Herbert Bates", "Motor mechanic");
        addDefaultTechnician(3, "Steven Hardy", "Diagnostic wizard");

        // От числа машиномест будет зависеть количество заказов,
        // поэтому сначала производится генерация машиномест,
        // а потом заказов
        for (int i = 0; i < 3; i++) { addDefaultCarPlace(i); }
        addDefaultOrder();
        addDefaultWorkScheduleInDailyManagement();
    }

    public List<ServiceAdvisor> getServiceAdvisors() {  return serviceAdvisors; }
    public void setServiceAdvisors(List<ServiceAdvisor> serviceAdvisors) { this.serviceAdvisors = serviceAdvisors; }

    public List<Technician> getTechnicians() { return technicians; }
    public void setTechnicians(List<Technician> technicians) { this.technicians = technicians; }
    public Technician getTechnician(int i) { return technicians.get(i); }
    public void addTechnician(Technician technician) {
        technicians.add(technician);
        System.out.println("Added technician: " + technician);
    }
    public void removeTechnician(Technician technician) {
        technicians.remove(technician);
        System.out.println("Removed technician: " + technician);
    }
    public List<Technician> sortingTechniciansAlphabetically() {
        List<Technician> technicians = this.technicians;
        technicians.sort(new TechnicianAlphabeticalComparator());
        return technicians;
    }
    public List<Technician> sortingTechniciansByOccupancy() {
        List<Technician> technicians = this.technicians;
        technicians.sort(new TechnicianOccupancyComparator());
        return technicians;
    }
    public List<Technician> findTechniciansByOrder(Order order) {
        if (this.orders.contains(order)) {
            return this.orders.get(order.getId()).getTechnicians();
        } else {
            return null;
        }
    }

    public List<CarPlace> getCarPlaces() { return carPlaces; }
    public void setCarPlaces(List<CarPlace> carPlaces) { this.carPlaces = carPlaces; }
    public void addCarPlace(CarPlace carPlace) {
        carPlaces.add(carPlace);
        System.out.println("Added place: " + carPlace);
    }
    public void removeCarPlace(CarPlace carPlace) {
        carPlaces.remove(carPlace);
        System.out.println("Removed place: " + carPlace);
    }
    public List<CarPlace> availableCarPlacesList() {
        List<CarPlace> availableCarPlaces = new ArrayList<>();
        for (CarPlace carPlace : this.carPlaces) {
            if (!carPlace.isOccupied()) {
                availableCarPlaces.add(carPlace);
            }
        }
        return availableCarPlaces;
    }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public Order getOrder(int i) { return orders.get(i); }
    public void addOrder(Order order) {
        orders.add(order);
        System.out.println("Added order: " + order);
    }
    public void removeOrder(Order order) {
        orders.remove(order);
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
        List<Order> orders = this.orders;
        orders.sort(new OrderStartDateComparator());
        return orders;
    }
    public List<Order> sortingByEndDateOrders() {
        List<Order> orders = this.orders;
        orders.sort(new OrderEndDateComparator());
        return orders;
    }
    public List<Order> sortingByLoadingDateOrders() {
        List<Order> orders = this.orders;
        orders.sort(new OrderLoadingDateComparator());
        return orders;
    }
    public List<Order> sortingByCostOrders() {
        List<Order> orders = this.orders;
        orders.sort(new OrderCostComparator());
        return orders;
    }
    public List<Order> sortingByStartDateCurrentlyRunningOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : this.orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                orders.add(order);
            }
        }
        orders.sort(new OrderStartDateComparator());
        return orders;
    }
    public List<Order> sortingByEndDateCurrentlyRunningOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : this.orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                orders.add(order);
            }
        }
        orders.sort(new OrderEndDateComparator());
        return orders;
    }
    public List<Order> sortingByCostCurrentlyRunningOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : this.orders) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                orders.add(order);
            }
        }
        orders.sort(new OrderCostComparator());
        return orders;
    }
    public List<Order> findOrderByTechnician(Technician technician) {
        List<Order> orders = new ArrayList<>();
        for (Order order : this.orders) {
            if (order.getTechnicians().contains(technician)) {
                orders.add(order);
            }
        }
        return orders;
    }
    public List<Order> ordersOverPeriodTimeStrictEquality(List<Date> timePeriod,
        OrderStatus status, OrderStartDateEndDateCostOnlyComparator comparator) {

        if (status == OrderStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Invalid status " + status + " detected");
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : this.orders) {
            if (order.getStatus() == status) {
                if (timePeriodMatching(order, timePeriod)) {
                    orders.add(order);
                }
            }
        }
        orders.sort(comparator);
        return orders;
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
        serviceAdvisors.add(serviceAdvisor);
        System.out.println("Added service advisor: " + serviceAdvisor);
    }
    private void addDefaultTechnician(int i, String name, String sp) {
        Technician technician = new Technician(i, name, sp);
        technicians.add(technician);
        System.out.println("Added technician: " + technician);
    }
    private void addDefaultCarPlace(int i) {
        Random r = new Random();
        CarPlace place = new CarPlace(i, 20 + r.nextInt(0, 10), r.nextBoolean());
        carPlaces.add(place);
        System.out.println("Added place: " + place);
    }
    private void addDefaultCustomer() {
        Customer customer = new Customer(0, "Raymond Anderson");
        customers.add(customer);
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
            List<Technician> techniciansList = new ArrayList<>(techniciansSet);
            int d   = 21+ r.nextInt(0, 4);
            int h1  = 9 + r.nextInt(0, 8);
            int h2  = 9 + r.nextInt(0, 4);
            int h3  = 13+ r.nextInt(0, 4);
            Order order = new Order(i, serviceAdvisors.getFirst(), techniciansList,
                    customers.getFirst(), carPlaces.get(i),
                    new Date(2025, 9, d-1, h1, 0),
                    new Date(2025, 9, d, h2, 0),
                    new Date(2025, 9, d, h2, 10),
                    new Date(2025, 9, d, h3, 0));
            orders.add(order);
            System.out.println("Added order: " + order);
        }
    }

    private void addDefaultWorkScheduleInDailyManagement() {
        dailyManagement.setWorkSchedule(new int[]{8, 18});

    }
}
