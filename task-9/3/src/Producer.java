import java.util.Random;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {
    private final BlockingQueue<Integer> buffer;
    private final Random random = new Random();

    // Поле отвечает за получение сигнала остановки - клавиши Enter
    private boolean end = false;

    public Producer(BlockingQueue<Integer> buffer) { this.buffer = buffer; }

    public void setEnd(boolean end) { this.end = end; }

    @Override
    public void run() {
        try {
            while (!end) {
                Integer item = random.nextInt(100);
                buffer.put(item); // В этом месте поток заблокируется, если буфер полон

                System.out.println(Thread.currentThread().getName() + " produces: " + item);
                System.out.println("The number " + item +
                        " is placed in the buffer. Buffer size: " + buffer.size());

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " is interrupted.");
        }
    }
}
