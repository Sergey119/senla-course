public class OrderCostComparator extends OrderStartDateEndDateCostOnlyComparator {

    @Override
    public int compare(Order o1, Order o2) {
        Integer cost1 = o1.getCost();
        Integer cost2 = o2.getCost();
        return cost1.compareTo(cost2);
    }
}
