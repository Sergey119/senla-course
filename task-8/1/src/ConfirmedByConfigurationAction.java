public class ConfirmedByConfigurationAction extends ConsoleUserAccessAction {
    protected AutoserviceConfig config = new AutoserviceConfig();

    public ConfirmedByConfigurationAction() {
        ConfigurationInjector.inject(config);
        System.out.println("Configuration loaded successfully");
    }
}