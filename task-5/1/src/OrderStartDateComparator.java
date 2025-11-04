public class OrderStartDateComparator extends OrderStartDateEndDateCostOnlyComparator {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}
