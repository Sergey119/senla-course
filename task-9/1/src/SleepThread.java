public class SleepThread implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
