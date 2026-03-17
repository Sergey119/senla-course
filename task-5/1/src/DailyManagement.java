import java.util.*;

public class DailyManagement {
    private Map<Date, List<Order>> groupOrdersByDate;
    private List<Integer> workSchedule;

    private int interval = 1; // размер интервала 1 час
    private int total = 24;
    private int slotsNumber = total / interval;

    private Map<CarPlace, Map<Integer, Boolean>> carPlaceSlots = new HashMap<>();
    private Map<Technician, Map<Integer, Boolean>> technicianSlots = new HashMap<>();

    public List<Integer> getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(List<Integer> workSchedule) { this.workSchedule = workSchedule; }

    // Метод группировки заказов по дням является сеттером для groupOrdersByDate
    public void groupOrdersByDate(Map<Integer, Order> ordersMap) {
        Map<Date, List<Order>> groupedOrders = new HashMap<>();

        List<Order> orders = new ArrayList<>();
        if (ordersMap != null) {
            for (Map.Entry<Integer, Order> entry : ordersMap.entrySet()) {
                orders.add(entry.getValue());
            }
        }

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
        if (groupedMap.containsKey(date)) {
            return new ArrayList<>(groupedMap.get(date));
        }
        return new ArrayList<>();
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
            for (Technician technician : order.getTechnicians().values()) {
                if (!map.containsKey(technician)) {
                    map.put(technician, new ArrayList<>());
                }
                map.get(technician).add(order);
            }
        }
        return map;
    }

    // Заполнение занятых слотов по машиноместам
    private void fillingOccupiedSlotsByCarPlace(Date date) {
        Map<CarPlace, List<Order>> map = groupOrdersByCarPlace(date);
        carPlaceSlots.clear();

        for (CarPlace carPlace : map.keySet()) {
            List<Order> orders = map.get(carPlace);
            Map<Integer, Boolean> slots = new HashMap<>();

            // Инициализируем все слоты как false (свободные)
            for (int i = 0; i < slotsNumber; i++) {
                slots.put(i, false);
            }

            for (Order order : orders) {
                int startDate = order.getStartDate().getHours();
                int firstSlot = startDate / interval;
                int endDate = order.getEndDate().getHours();
                int endSlot = endDate / interval;

                for (int i = firstSlot; i < endSlot; i++) {
                    if (i < slotsNumber) {
                        slots.put(i, true); // помечаем как занятый
                    }
                }
            }
            carPlaceSlots.put(carPlace, slots);
        }
    }

    // Заполнение занятых слотов по техникам
    private void fillingOccupiedSlotsByTechnician(Date date) {
        Map<Technician, List<Order>> map = groupOrdersByTechnician(date);
        technicianSlots.clear();

        for (Technician technician : map.keySet()) {
            List<Order> orders = map.get(technician);
            Map<Integer, Boolean> slots = new HashMap<>();

            // Инициализируем все слоты как false (свободные)
            for (int i = 0; i < slotsNumber; i++) {
                slots.put(i, false);
            }

            for (Order order : orders) {
                int startDate = order.getStartDate().getHours();
                int firstSlot = startDate / interval;
                int endDate = order.getEndDate().getHours();
                int endSlot = endDate / interval;

                for (int i = firstSlot; i < endSlot; i++) {
                    if (i < slotsNumber) {
                        slots.put(i, true); // помечаем как занятый
                    }
                }
            }
            technicianSlots.put(technician, slots);
        }
    }

    // Подсчет свободных слотов по машиноместам
    private Map<Integer, Integer> summarizeFreeSlotsByCarPlace(Date date) {
        fillingOccupiedSlotsByCarPlace(date);
        Map<Integer, Integer> freeCounts = new HashMap<>();

        // Инициализируем все слоты с 0 свободных мест
        for (int i = 0; i < slotsNumber; i++) {
            freeCounts.put(i, 0);
        }

        for (Map.Entry<CarPlace, Map<Integer, Boolean>> entry : carPlaceSlots.entrySet()) {
            Map<Integer, Boolean> slots = entry.getValue();
            for (int i = 0; i < slotsNumber; i++) {
                Boolean isOccupied = slots.get(i);
                if (isOccupied != null && !isOccupied) {
                    freeCounts.put(i, freeCounts.get(i) + 1);
                }
            }
        }

        return freeCounts;
    }

    // Подсчет свободных слотов по техникам
    private Map<Integer, Integer> summarizeFreeSlotsByTechnician(Date date) {
        fillingOccupiedSlotsByTechnician(date);
        Map<Integer, Integer> freeCounts = new HashMap<>();

        // Инициализируем все слоты с 0 свободных мастеров
        for (int i = 0; i < slotsNumber; i++) {
            freeCounts.put(i, 0);
        }

        for (Map.Entry<Technician, Map<Integer, Boolean>> entry : technicianSlots.entrySet()) {
            Map<Integer, Boolean> slots = entry.getValue();
            for (int i = 0; i < slotsNumber; i++) {
                Boolean isOccupied = slots.get(i);
                if (isOccupied != null && !isOccupied) {
                    freeCounts.put(i, freeCounts.get(i) + 1);
                }
            }
        }

        return freeCounts;
    }

    // Получение доступных машиномест
    public int getAvailableCarPlaces(Date date){
        List<Integer> workingDayLimitation = workSchedule;
        if (workingDayLimitation.size() < 2) {
            return 0;
        }

        Map<Integer, Integer> carPlacesSlots = summarizeFreeSlotsByCarPlace(date);
        Map<Integer, Integer> technicianSlotsMap = summarizeFreeSlotsByTechnician(date);

        int max = 0;
        int startHour = workingDayLimitation.get(0);
        int endHour = workingDayLimitation.get(1);

        for (int i = startHour; i < endHour; i++) {
            Integer carSlots = carPlacesSlots.getOrDefault(i, 0);
            Integer techSlots = technicianSlotsMap.getOrDefault(i, 0);

            int least = Math.min(carSlots, techSlots);
            if (least > max) {
                max = least;
            }
        }
        return max;
    }

    // Получение доступных дат
    public Map<Integer, Integer> getAvailableDate(Date date){
        Date dateWithoutTime = new Date(date.getYear(), date.getMonth(), date.getDate());
        List<Integer> workingDayLimitation = new ArrayList<>(workSchedule);
        Map<Integer, Integer> carPlacesSlots = summarizeFreeSlotsByCarPlace(dateWithoutTime);
        Map<Integer, Integer> technicianSlotsMap = summarizeFreeSlotsByTechnician(dateWithoutTime);
        Map<Integer, Integer> resultMap = new TreeMap<>();

        workingDayLimitation.set(0, date.getHours() + 1);
        int startHour = workingDayLimitation.get(0);
        int endHour = workingDayLimitation.get(1);
        int key = startHour;
        int flag = 0;

        for (int i = startHour; i < endHour; i++) {
            Integer carSlots = carPlacesSlots.getOrDefault(i, 0);
            Integer techSlots = technicianSlotsMap.getOrDefault(i, 0);

            int least = Math.min(carSlots, techSlots);
            if (least > 0) {    // есть свободные машиноместо и мастер
                if (flag == 0) {
                    resultMap.put(key, i);
                } else {
                    key = i;
                    resultMap.put(key, i);
                    flag = 0;
                }
            } else {
                flag = 1;
            }
        }

        return resultMap;
    }
}