import java.util.Objects;

public class CarPlace {
    private int id;
    private int square;
    private boolean carLift;
    private boolean isOccupied;

    public CarPlace(int id, int square, boolean carLift) {
        this.id = id;
        this.square = square;
        this.carLift = carLift;
        this.isOccupied = false;
    }

    public int getId() { return id; }

    public int getSquare() { return square; }
    public void setSquare(int square) { this.square = square; }

    public boolean isCarLift() { return carLift; }
    public void setCarLift(boolean carLift) { this.carLift = carLift; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    @Override
    public String toString() {
        return "CarPlace{" +
                "id=" + id +
                ", square=" + square +
                ", carLift=" + carLift +
                ", isOccupied=" + isOccupied +
                '}';
    }


}
