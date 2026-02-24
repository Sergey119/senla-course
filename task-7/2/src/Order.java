import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Order {
    private int id;
    private ServiceAdvisor serviceAdvisor;
    private Map<Integer, Technician> technicians;
    private Customer customer;
    private CarPlace carPlace;
    private OrderStatus status;
    private int cost;
    private Date createdDate;
    private Date startDate;
    private Date loadingDate;
    private Date endDate;

    public Order(int id, ServiceAdvisor serviceAdvisor, Map<Integer, Technician> technicians, Customer customer,
                 CarPlace carPlace, Date createdDate, Date startDate, Date loadingDate, Date endDate) {
        this.id = id;
        this.serviceAdvisor = serviceAdvisor;
        this.technicians = technicians;
        this.customer = customer;
        this.carPlace = carPlace;
        this.status = OrderStatus.PENDING;
        this.cost = 0;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.loadingDate = loadingDate;
        this.endDate = endDate;
    }
    public Order() {}

    public int getId() { return id; }

    public ServiceAdvisor getServiceAdvisor() { return serviceAdvisor; }
    public void setServiceAdvisor(ServiceAdvisor serviceAdvisor) { this.serviceAdvisor = serviceAdvisor; }

    public Map<Integer, Technician> getTechnicians() { return technicians; }
    public void setTechnicians(Map<Integer, Technician> technicianList) { this.technicians = technicianList; }
    public Technician getTechnician(int i) { return technicians.get(i); }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public CarPlace getCarPlace() { return carPlace; }
    public void setCarPlace(CarPlace carPlace) { this.carPlace = carPlace; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getLoadingDate() { return loadingDate; }
    public void setLoadingDate(Date loadingDate) { this.loadingDate = loadingDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "\nOrder{" +
                "id=" + id +
                ", serviceAdvisor=" + serviceAdvisor +
                ", technicians=" + technicians +
                ",\ncustomer=" + customer +
                ", carPlace=" + carPlace +
                ", status=" + status +
                ", cost=" + cost +
                ",\ncreatedDate=" + createdDate +
                ", startDate=" + startDate +
                ", loadingDate=" + loadingDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
