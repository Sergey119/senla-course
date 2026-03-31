import java.util.HashMap;
import java.util.Scanner;

public class RemoveOrderAction extends ConfirmedByConfigurationAction  {

    @Override
    public void execute() {
        System.out.println("--- Removing a order. ---");
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        var id = new Scanner(System.in).nextInt();

        loadingConfigProperties();

        var strategyHandlers = new HashMap<String, IPropertyAccessResponseHandler<Integer>>();
        strategyHandlers.put("true", new PermissionToRemoveOrderHandler());
        var responseHandler = strategyHandlers.getOrDefault(config.getProperty("remove.order.permission"),
                new ProhibitionToRemoveOrderHandler());
        responseHandler.handleResponse(id);

        System.out.println(autoservice.getOrders());
        System.out.println("Order has been successfully removed!");
    }
}