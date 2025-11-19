public class OrderEndDateComparator extends OrderStartDateEndDateCostOnlyComparator {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getEndDate().compareTo(o2.getEndDate());
    }
}
