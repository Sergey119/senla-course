import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ShiftOrderTimeAction extends ConfirmedByConfigurationAction {

    @Override
    public void execute() {
        System.out.println("--- Shifting order time. ---");
        var list = new ArrayList<Integer>();
        System.out.println(autoservice.getOrders());
        System.out.print("ID of order: ");
        var id = new Scanner(System.in).nextInt();
        list.add(id);
        System.out.println(autoservice.getOrder(id));
        System.out.println("Time required to shift the order (measured in hours).");
        var time = new Scanner(System.in).nextInt();
        list.add(time);

        loadingConfigProperties();

        var strategyHandlers = new HashMap<String, IPropertyAccessResponseHandler<List<Integer>>>();
        strategyHandlers.put("true", new PermissionToShiftOrderTimeHandler());
        var responseHandler = strategyHandlers.getOrDefault(config.getProperty("shift.order.time.permission"),
                new ProhibitionToShiftOrderTimeHandler());
        responseHandler.handleResponse(list);

    }
}