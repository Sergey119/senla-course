import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Autoservice autoservice = new Autoservice();
        autoservice.initializeDefaultData();

        // Запускать 1 из 4 реализованных функций

        addRemoveTechnicianScript(autoservice);
        // addRemoveCarPlaceScript(autoservice);
        // addRemoveCloseCancelOrderScript(autoservice);
        // shiftInTimeOrderScript(autoservice);
    }

    public static void addRemoveTechnicianScript(Autoservice a){
        System.out.println("--- Technician is being added to the autoservice. ---");
        List<Technician> technicianList = a.getTechnicians();
        System.out.println(technicianList);
        Technician newTechnician = new Technician(4, "Anthony Mitchell", "probationer, continuing student");
        a.addTechnician(newTechnician);
        technicianList = a.getTechnicians();
        System.out.println(technicianList);
        System.out.println("--- Technician is being removed from the autoservice. ---");
        Technician removeTechnician = technicianList.get(0);
        a.removeTechnician(removeTechnician);
        technicianList = a.getTechnicians();
        System.out.println(technicianList);
    }

    public static void addRemoveCarPlaceScript(Autoservice a){
        System.out.println("--- Place is being added to the autoservice. ---");
        List<CarPlace> placeList = a.getCarPlaces();
        System.out.println(placeList);
        Random r = new Random();
        CarPlace newPlace = new CarPlace(3, 20 + r.nextInt(0, 10), r.nextBoolean());
        a.addCarPlace(newPlace);
        placeList = a.getCarPlaces();
        System.out.println(placeList);
        System.out.println("--- Place is being removed from the autoservice. ---");
        CarPlace removePlace = placeList.get(0);
        a.removeCarPlace(removePlace);
        placeList = a.getCarPlaces();
        System.out.println(placeList);
    }

    public static void addRemoveCloseCancelOrderScript(Autoservice a){
        System.out.println("--- Order is being added in the system. ---");
        List<Order> orderList = a.getOrders();
        System.out.println(orderList);
        Random r = new Random();
        int d   = 21+ r.nextInt(0, 4);
        int h1  = 9 + r.nextInt(0, 8);
        int h2  = 9 + r.nextInt(0, 4);
        int h3  = 13+ r.nextInt(0, 4);
        Order newOrder = new Order(orderList.size(), a.getServiceAdvisors().getFirst(),
                a.getTechnicians().getFirst(), a.getCustomers().getFirst(),
                a.getCarPlaces().getFirst(), OrderStatus.PENDING,
                new Date(2025, 9, d-1, h1, 0),
                new Date(2025, 9, d, h2, 0),
                new Date(2025, 9, d, h3, 0));
        a.addOrder(newOrder);
        orderList = a.getOrders();
        System.out.println(orderList);
        System.out.println("--- Order is being removed from the system. ---");
        Order removeOrder = orderList.get(0);
        a.removeOrder(removeOrder);
        orderList = a.getOrders();
        System.out.println(orderList);
        System.out.println("--- Closing an order at a autoservice. ---");
        System.out.println(orderList.get(1));
        orderList.get(1).setStatus(OrderStatus.COMPLETED);
        System.out.println(orderList.get(1));
        System.out.println("--- Cancelling an order at a autoservice. ---");
        System.out.println(orderList.get(2));
        orderList.get(2).setStatus(OrderStatus.CANCELLED);
        System.out.println(orderList.get(2));
    }

    public static void shiftInTimeOrderScript(Autoservice a){
        System.out.println("--- Order fulfillment time shift. ---");
        System.out.println("Create order following the current one, which will conflict with it.");
        Order delayedOrder = a.getOrders().get(0);
        delayedOrder.setStatus(OrderStatus.IN_PROGRESS);
        System.out.println("Current order:");
        System.out.println(delayedOrder);
        Random r = new Random();
        int d   = delayedOrder.getStartDate().getDay();
        int h1  = 9 + r.nextInt(0, 8);
        int h2  = delayedOrder.getEndDate().getHours();
        int h3  = h2 + r.nextInt(0, 4);
        Order newOrder = new Order(a.getOrders().size(), a.getServiceAdvisors().getFirst(),
                a.getTechnicians().getFirst(), a.getCustomers().getFirst(),
                a.getCarPlaces().getFirst(), OrderStatus.PENDING,
                new Date(2025, 9, d-1, h1, 0),
                new Date(2025, 9, d, h2, 0),
                new Date(2025, 9, d, h3, 0));
        a.addOrder(newOrder);
        System.out.println("Next order:");
        System.out.println(a.getOrders().get(a.getOrders().size()-1));
        System.out.println("The current order is delayed. Rescheduling the next order due to delays in the current order.");
        int delayTime = r.nextInt(0, 4);
        Order nextOrder = a.getOrders().get(a.getOrders().size()-1);
        System.out.println("Next order:");
        System.out.println(nextOrder);
        System.out.println("New schedule for the next order.");
        nextOrder.getStartDate().setHours(delayTime + nextOrder.getStartDate().getHours());
        nextOrder.getEndDate().setHours(delayTime + nextOrder.getEndDate().getHours());
        System.out.println(nextOrder);
        System.out.println("Current order:");
        System.out.println(delayedOrder);
        System.out.println("New schedule for the next order.");
        delayedOrder.getStartDate().setHours(delayTime + delayedOrder.getStartDate().getHours());
        delayedOrder.getEndDate().setHours(delayTime + delayedOrder.getEndDate().getHours());
        System.out.println(delayedOrder);
    }

}