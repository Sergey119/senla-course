import java.util.Comparator;

public class OrderLoadingDateComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getLoadingDate().compareTo(o2.getLoadingDate());
    }
}
