import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable {
    private final BlockingQueue<Integer> buffer;

    public Consumer(BlockingQueue<Integer> buffer) { this.buffer = buffer; }


    @Override
    public void run() {
        try {
            while (true) {
                Integer item = buffer.take(); // В этом месте поток заблокируется, если буфер пуст

                System.out.println(Thread.currentThread().getName() + " consumes: " + item);
                System.out.println("The number " + item +
                        " is removed from the buffer. Buffer size: " + buffer.size());

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " is interrupted.");
        }
    }
}
