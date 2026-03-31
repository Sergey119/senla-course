public class Printer {

    private final Object lock = new Object();

    // Поле отвечает за поочередную работу двух потоков
    private volatile boolean firstTurn = true;

    // Поле отвечает за получение сигнала остановки - клавиши Enter
    private volatile boolean end = false;

    public Object getLock() { return lock; }

    public boolean isFirstTurn() { return firstTurn; }
    public void setFirstTurn(boolean firstTurn) {  this.firstTurn = firstTurn; }

    public boolean isEnd() { return end; }
    public void setEnd(boolean end) { this.end = end; }

    // Первый поток печатает, когда firstTurn = true
    public void printFirst() {
        while (!end) {
            synchronized (lock) {
                while (!firstTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(Thread.currentThread().getName() + " is interrupted.");
                        return;
                    }
                }
                if (end) {
                    break;
                }
                System.out.println(Thread.currentThread().getName());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(Thread.currentThread().getName() + " is interrupted during a pause.");
                    return;
                }
                firstTurn = false;
                lock.notifyAll();
            }
        }
        System.out.println(Thread.currentThread().getName() + " closing.");
    }

    // Второй поток печатает, когда firstTurn = false
    public void printSecond() {
        while (!end) {
            synchronized (lock) {
                while (firstTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(Thread.currentThread().getName() + " is interrupted.");
                        return;
                    }
                }
                if (end) {
                    break;
                }
                System.out.println(Thread.currentThread().getName());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(Thread.currentThread().getName() + " is interrupted during a pause.");
                    return;
                }
                firstTurn = true;
                lock.notifyAll();
            }
        }
        System.out.println(Thread.currentThread().getName() + " closing.");
    }
}
