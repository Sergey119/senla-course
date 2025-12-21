import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final int bufferSize = 5;

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Start!");

        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(bufferSize);

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        Thread producerThread = new Thread(producer, "Producer");
        Thread consumerThread = new Thread(consumer, "Consumer");

        producerThread.start();
        consumerThread.start();

        System.in.read(); // Нажатие Enter
        System.out.println("Stop signal received...");
        producer.setEnd(true);

        Thread.sleep(6000); // Время для обработки остатков

        System.out.println("End! Please terminate the program.");
    }
}