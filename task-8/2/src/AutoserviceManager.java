import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

@Singleton
public class AutoserviceManager {
    @Inject
    private Autoservice autoservice;

    private final String basePath = ConfigPropertiesDirectory.getInstance().getPath() + "json.json";

    public void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(basePath))) {
            String line = reader.readLine();

            if (line != null && !line.trim().isEmpty()) {
                System.out.println("Loading Autoservice data from file...");

                ObjectMapper mapper = new ObjectMapper();
                Autoservice deserialized = mapper.readValue(line, Autoservice.class);

                autoservice.updateFrom(deserialized);

            } else {
                System.out.println("No data file found, using empty Autoservice");
            }

        } catch (Exception e) {
            System.out.println("Could not load autoservice data, using empty instance: " + e.getMessage());
        }
    }

    public void saveData() {
        try (PrintWriter printer = new PrintWriter(new FileWriter(basePath))) {
            ObjectMapper mapper = new ObjectMapper();
            printer.println(mapper.writeValueAsString(autoservice));
            System.out.println("Autoservice data saved successfully.");
        } catch (Exception e) {
            System.err.println("Failed to save autoservice data: " + e.getMessage());
        }
    }

}