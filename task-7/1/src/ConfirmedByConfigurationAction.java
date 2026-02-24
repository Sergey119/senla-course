import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfirmedByConfigurationAction extends ConsoleUserAccessAction {
    protected Properties config = new Properties();

    public void loadingConfigProperties(){
        try (var fis = new FileInputStream("C:\\Users\\admin\\IdeaProjects\\senla-course\\task-7\\1\\stream\\config.properties")) {
            config.load(fis);
            System.out.println("Configuration loaded successfully.");
        } catch (IOException e) {
            System.err.println("Configuration loading error: " + e.getMessage());
        }
    }
}
