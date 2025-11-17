import java.util.Random;

public class WarehouseAccounting {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        Random random = new Random();
        int totalWeight;

        for(totalWeight = 0; totalWeight < warehouse.getCapacity(); totalWeight++) {
            int productType = random.nextInt(3);
            int weight = random.nextInt(1, 9);
            if((totalWeight + weight) < warehouse.getCapacity()) {
                switch(productType) {
                    case 0:
                        warehouse.addProduct(new Electronic("electronic", weight));
                        break;
                    case 1:
                        warehouse.addProduct(new Sport("sport_item", weight));
                        break;
                    case 2:
                        warehouse.addProduct(new Tool("tool", weight));
                        break;
                }
                totalWeight = warehouse.calculateTotalWeight();
            }
        }

        System.out.println("Total weight of products stored in warehouse: " + totalWeight + " kg");
    }

}
