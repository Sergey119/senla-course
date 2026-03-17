import java.util.Comparator;

public class TechnicianAlphabeticalComparator implements Comparator<Technician> {
    @Override
    public int compare(Technician o1, Technician o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
