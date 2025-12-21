import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {
    private static final String fileName = "C:\\Users\\admin\\IdeaProjects\\senla-course\\task-7\\2\\stream\\json.json";
    private static final Autoservice singleton = Autoservice.getInstance();

    public static void main(String[] args) {
        System.out.println("Launch of the car service system...");

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line = reader.readLine();

            if (line != null) {
                // System.out.println("Read JSON: " + line);

                ObjectMapper mapper = new ObjectMapper();
                Autoservice deserialized = mapper.readValue(line, Autoservice.class);

                singleton.updateFrom(deserialized); // Обновление singleton данными из десериализованного объекта

                // System.out.println("Updated Singleton: " + singleton);

                MenuController menuController = MenuController.getInstance();
                menuController.initialize();
                menuController.run();

                try (PrintWriter printer = new PrintWriter(new FileWriter(fileName))){
                    printer.println(mapper.writeValueAsString(singleton));
                } catch (Exception e) {
                    System.err.println("Serialization exception: " + e.getMessage());
                }

            } else {
                System.out.println("File is empty or no data read.");
            }
        } catch (Exception e) {
            System.err.println("Exception in Main: " + e.getMessage());
        } finally {
            System.out.println("The auto service system has been completed.");
        }
    }
}