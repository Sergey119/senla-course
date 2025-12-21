public class PermissionToRemoveOrderHandler implements IPropertyAccessResponseHandler<Integer> {
    @Inject
    private Autoservice autoservice;

    public PermissionToRemoveOrderHandler() {
        DIContainer.injectDependencies(this);
    }

    @Override
    public void handleResponse(Integer arg) {
        autoservice.removeOrder(arg);
        System.out.println("The order was successfully removed!");
    }
}