import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

public class Autoservice {
    private List<ServiceAdvisor> serviceAdvisors = new ArrayList<>();
    private List<Technician> technicians = new ArrayList<>();
    private List<CarPlace> carPlaces = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public Autoservice() {
    }

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
    }

    public List<ServiceAdvisor> getServiceAdvisors() {  return serviceAdvisors; }
    public void setServiceAdvisors(List<ServiceAdvisor> serviceAdvisors) { this.serviceAdvisors = serviceAdvisors; }

    public List<Technician> getTechnicians() { return technicians; }
    public void setTechnicians(List<Technician> technicians) { this.technicians = technicians; }
    public void addTechnician(Technician technician) {
        technicians.add(technician);
        System.out.println("Added technician: " + technician);
    }
    public void removeTechnician(Technician technician) {
        technicians.remove(technician);
        System.out.println("Removed technician: " + technician);
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

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
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
        orders.get(i).getEndDate().setHours(j + orders.get(i).getEndDate().getHours());
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
            int d   = 21+ r.nextInt(0, 4);
            int h1  = 9 + r.nextInt(0, 8);
            int h2  = 9 + r.nextInt(0, 4);
            int h3  = 13+ r.nextInt(0, 4);
            Order order = new Order(i, serviceAdvisors.getFirst(), technicians.get(i),
                    customers.getFirst(), carPlaces.get(i), OrderStatus.PENDING,
                    new Date(2025, 9, d-1, h1, 0),
                    new Date(2025, 9, d, h2, 0),
                    new Date(2025, 9, d, h3, 0));
            orders.add(order);
            System.out.println("Added order: " + order);
        }
    }
}
