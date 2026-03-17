public class Main {
    public static void main(String[] args) {
        try {
            Thread firstThread = new Thread(new SleepThread());
            Thread secondThread = new Thread(new SlowThread());
            System.out.println(firstThread.getState());
            firstThread.start();
            secondThread.start();
            Thread.sleep(500);
            System.out.println(firstThread.getState());
            System.out.println(secondThread.getState());
            Object lock = SlowThread.getLock();
            synchronized (lock) {
                lock.notify();
            }
            System.out.println(secondThread.getState());
            Thread.sleep(2000);
            System.out.println(secondThread.getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}