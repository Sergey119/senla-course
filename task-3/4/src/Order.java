import java.util.Date;

public class Order {
    private int id;
    private ServiceAdvisor serviceAdvisor;
    private Technician technician;
    private Customer customer;
    private CarPlace carPlace;
    private OrderStatus status;
    private Date createdDate;
    private Date startDate;
    private Date endDate;

    public Order(int id, ServiceAdvisor serviceAdvisor, Technician technician, Customer customer,
                 CarPlace carPlace, OrderStatus status, Date createdDate, Date startDate, Date endDate) {
        this.id = id;
        this.serviceAdvisor = serviceAdvisor;
        this.technician = technician;
        this.customer = customer;
        this.carPlace = carPlace;
        this.status = status;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }

    public ServiceAdvisor getServiceAdvisor() { return serviceAdvisor; }
    public void setServiceAdvisor(ServiceAdvisor serviceAdvisor) { this.serviceAdvisor = serviceAdvisor; }

    public Technician getTechnician() { return technician; }
    public void setTechnician(Technician technician) { this.technician = technician; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public CarPlace getCarPlace() { return carPlace; }
    public void setCarPlace(CarPlace carPlace) { this.carPlace = carPlace; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "\nOrder{" +
                "id=" + id +
                ", serviceAdvisor=" + serviceAdvisor +
                ", technician=" + technician +
                ",\ncustomer=" + customer +
                ", carPlace=" + carPlace +
                ", status=" + status +
                ",\ncreatedDate=" + createdDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
