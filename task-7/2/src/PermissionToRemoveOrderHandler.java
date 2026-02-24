public class PermissionToRemoveOrderHandler implements IPropertyAccessResponseHandler<Integer> {
    private final Autoservice autoservice = Autoservice.getInstance();

    @Override
    public void handleResponse(Integer arg) {
        System.out.println("Мы в PermissionToRemoveOrderHandler");
        autoservice.removeOrder(arg);
        System.out.println("The order was successfully shifted in time!");
    }
}
