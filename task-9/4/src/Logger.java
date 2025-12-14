import java.time.LocalDateTime;

public class Logger implements Runnable {

    private final int intervalSeconds;

    // Поле отвечает за получение сигнала остановки - клавиши Enter
    private boolean end = false;

    public boolean isEnd() { return end;  }
    public void setEnd(boolean end) { this.end = end; }

    public Logger(int intervalSeconds) {
        if (intervalSeconds <= 0) {
            throw new IllegalArgumentException("The interval must be a positive integer");
        }
        this.intervalSeconds = intervalSeconds;
    }

    @Override
    public void run() {
        try{
            while (!end) {
                System.out.println("Current time: " + LocalDateTime.now());
                Thread.sleep(intervalSeconds * 1000);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
