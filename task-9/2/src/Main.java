import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Printer printer = new Printer();

        Thread firstThread = new Thread(printer::printFirst, "First thread");
        Thread secondThread = new Thread(printer::printSecond, "Second thread");

        System.out.println("Starting threads. Press Enter to stop.");
        firstThread.start();
        secondThread.start();

        System.in.read();

        System.out.println("Stop signal received...");

        printer.setEnd(true); // Устанавливается флаг остановки

        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread is interrupted.");
        }

        System.out.println("Closing the main thread.");
    }
}