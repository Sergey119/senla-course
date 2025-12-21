public class Customer extends Person{

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Customer(){}

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
