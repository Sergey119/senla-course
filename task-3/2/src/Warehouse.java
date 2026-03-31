import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private List<Product> products = new ArrayList<>();
    private final int CAPACITY = 100;

    public Warehouse() {}
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    public int getCapacity() { return CAPACITY; }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName() +
                " with weight " + product.getWeight() + " kg.");
    }
    public int calculateTotalWeight() {
        int totalWeight = 0;
        for (Product product : products) {
            totalWeight += product.getWeight();
        }
        return totalWeight;
    }
}
