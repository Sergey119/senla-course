import java.util.List;

public class PermissionToShiftOrderTimeHandler implements IPropertyAccessResponseHandler<List<Integer>>{
    private final Autoservice autoservice = Autoservice.getInstance();

    @Override
    public void handleResponse(List<Integer> arg) {
        autoservice.shiftOrderTime(arg.get(0), arg.get(1));
        System.out.println(autoservice.getOrder(arg.get(0)));
        System.out.println("The order was successfully shifted in time!");
    }
}
