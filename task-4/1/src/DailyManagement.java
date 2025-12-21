import java.util.*;

public class DailyManagement {
    private Map<Date, List<Order>> groupOrdersByDate;
    private int[] workSchedule;

    private int interval = 1; // размер интервала 1 час
    private int total = 24;
    private int slotsNumber = total / interval;

    private Map<CarPlace, Boolean[]> carPlaceSlots = new HashMap<>();
    private Map<Technician, Boolean[]> technicianSlots = new HashMap<>();

    public int[] getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(int[] workSchedule) { this.workSchedule = workSchedule; }

    // Метод группировки заказов по дням является сеттером для groupOrdersByDate
    public void groupOrdersByDate(List<Order> orders) {
        Map<Date, List<Order>> groupedOrders = new HashMap<>();
        for (Order order : orders) {
            int year = order.getStartDate().getYear();
            int month = order.getStartDate().getMonth();
            int date = order.getStartDate().getDate();
            Date workDay = new Date(year, month, date);
            if (!groupedOrders.containsKey(workDay)) {
                groupedOrders.put(workDay, new ArrayList<>());
            }
            groupedOrders.get(workDay).add(order);
        }
        groupOrdersByDate = groupedOrders;
    }

    // Метод получения заказов по конкретной дате
    private List<Order> getOrdersByDate(Date date) {
        Map<Date, List<Order>> groupedMap = groupOrdersByDate;
        return groupedMap.getOrDefault(date, new ArrayList<>());
    }

    // Группировка заказов по машиноместам для конкретной даты
    private Map<CarPlace, List<Order>> groupOrdersByCarPlace(Date date) {
        List<Order> ordersForDate = getOrdersByDate(date);
        Map<CarPlace, List<Order>> map = new HashMap<>();

        for (Order order : ordersForDate) {
            CarPlace carPlace = order.getCarPlace();
            if (!map.containsKey(carPlace)) {
                map.put(carPlace, new ArrayList<>());
            }
            map.get(carPlace).add(order);
        }
        return map;
    }

    // Группировка заказов по сотрудникам сервиса для конкретной даты
    private Map<Technician, List<Order>> groupOrdersByTechnician(Date date) {
        List<Order> ordersForDate = getOrdersByDate(date);
        Map<Technician, List<Order>> map = new HashMap<>();

        for (Order order : ordersForDate) {
            for (Technician technician : order.getTechnicians()) {
                if (!map.containsKey(technician)) {
                    map.put(technician, new ArrayList<>());
                }
                map.get(technician).add(order);
            }
        }
        return map;
    }

    // Заполнение
    private void fillingOccupiedSlotsByCarPlace(Date date) {
        Map<CarPlace, List<Order>> map = groupOrdersByCarPlace(date);
        for (CarPlace carPlace : map.keySet()) {
            List<Order> orders = map.get(carPlace);
            Boolean[] slots = new Boolean[slotsNumber];
            Arrays.fill(slots, false);

            for (Order order : orders) {
                int startDate = order.getStartDate().getHours();
                int firstSlot = startDate / interval;
                int endDate = order.getEndDate().getHours();
                int endSlot = endDate / interval;

                for (int i = firstSlot; i < endSlot; i++) {
                    slots[i] = true;
                }
            }

            carPlaceSlots.put(carPlace, slots);
        }
    }

    // Заполнение
    private void fillingOccupiedSlotsByTechnician(Date date) {
        Map<Technician, List<Order>> map = groupOrdersByTechnician(date);
        for (Technician technician : map.keySet()) {
            List<Order> orders = map.get(technician);
            Boolean[] slots = new Boolean[slotsNumber];
            Arrays.fill(slots, false);

            for (Order order : orders) {
                int startDate = order.getStartDate().getHours();
                int firstSlot = startDate / interval;
                int endDate = order.getEndDate().getHours();
                int endSlot = endDate / interval;

                for (int i = firstSlot; i < endSlot; i++) {
                    slots[i] = true;
                }
            }

            technicianSlots.put(technician, slots);
        }
    }

    private int[] summarizeFreeSlotsByCarPlace(Date date) {
        fillingOccupiedSlotsByCarPlace(date);
        int length = carPlaceSlots.values().iterator().next().length;
        int[] freeCounts = new int[length];

        for (Map.Entry<CarPlace, Boolean[]> entry : carPlaceSlots.entrySet()) {
            Boolean[] arr = entry.getValue();
            for (int i = 0; i < length; i++) {
                if (!arr[i]) {
                    freeCounts[i]++;
                }
            }
        }

        return freeCounts;
    }

    private int[] summarizeFreeSlotsByTechnician(Date date) {
        fillingOccupiedSlotsByTechnician(date);
        int length = technicianSlots.values().iterator().next().length;
        int[] freeCounts = new int[length];

        for (Map.Entry<Technician, Boolean[]> entry : technicianSlots.entrySet()) {
            Boolean[] arr = entry.getValue();
            for (int i = 0; i < length; i++) {
                if (!arr[i]) {
                    freeCounts[i]++;
                }
            }
        }

        return freeCounts;
    }

    public int getAvailableCarPlaces(Date date){
        int[] workingDayLimitation = workSchedule;

        int[] carPlacesSlots = summarizeFreeSlotsByCarPlace(date);
        int[] technicianSlots = summarizeFreeSlotsByTechnician(date);
        int max = 0;
        for (int i = workingDayLimitation[0]; i < workingDayLimitation[1]; i++) {
            int least = Math.min(carPlacesSlots[i], technicianSlots[i]);
            if (least > max) {
                max = least;
            }
        }
        return max;
    }

    public Map<Integer, Integer> getAvailableDate(Date date){
        Date dateWithoutTime = new Date(date.getYear(), date.getMonth(), date.getDate());
        int[] workingDayLimitation = workSchedule;
        workingDayLimitation[0] = date.getHours() + 1;

        int[] carPlacesSlots = summarizeFreeSlotsByCarPlace(dateWithoutTime);
        int[] technicianSlots = summarizeFreeSlotsByTechnician(dateWithoutTime);

        Map<Integer, Integer> map = new TreeMap<>();
        int key = workingDayLimitation[0];
        int flag = 0;

        for (int i = workingDayLimitation[0]; i < workingDayLimitation[1]; i++) {
            int least = Math.min(carPlacesSlots[i], technicianSlots[i]);
            if (least > 0) {    // есть свободные машиноместо и мастер
                if (flag == 0) {
                    map.put(key, i);
                } else {
                    key = i;
                    map.put(key, i);
                    flag = 0;
                }
            } else {
                flag = 1;
            }
        }

        return map;
    }

}
