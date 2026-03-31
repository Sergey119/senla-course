public class SlowThread implements Runnable {

    private static final Object lock = new Object();

    public static Object getLock() { return lock; }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}