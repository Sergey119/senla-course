public class ServiceAdvisor extends Person {

    public ServiceAdvisor(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public ServiceAdvisor(){}

    @Override
    public String toString() {
        return "ServiceAdvisor{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
