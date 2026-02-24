import java.util.Comparator;

public class TechnicianOccupancyComparator implements Comparator<Technician> {
    @Override
    public int compare(Technician o1, Technician o2) {
        return Boolean.compare(o1.isAvailable(), o2.isAvailable());
    }
}
