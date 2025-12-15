import java.io.IOException;

public class Main {

    private static final int interval = 2;

    public static void main(String[] args) throws InterruptedException, IOException {

        Logger loggerTask = new Logger(interval);
        Thread thread = new Thread(loggerTask);

        System.out.println("Starting thread.");
        thread.start();

        System.in.read();
        System.out.println("Stop signal received...");
        loggerTask.setEnd(true);

        thread.join();
        System.out.println("Ending thread.");
    }
}