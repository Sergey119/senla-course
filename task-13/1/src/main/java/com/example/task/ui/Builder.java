package com.example.task.ui;

import java.util.HashMap;
import java.util.Map;

public class Builder {
    private Menu rootMenu;

    public Builder() {
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {
        var mainItems = new HashMap<Integer, MenuItem>();

        var carPlaceAction = new CarPlaceAction();
        var technicianAction = new TechnicianAction();
        var customerAction = new CustomerAction();
        var serviceAdvisorAction = new ServiceAdvisorAction();
        var orderAction = new OrderAction();

        mainItems.put(0, new MenuItem("Exit", null, null));
        mainItems.put(1, new MenuItem("Testing CarPlaceDAO (Hibernate)", carPlaceAction, null));
        mainItems.put(2, new MenuItem("Testing TechnicianDAO (Hibernate)", technicianAction, null));
        mainItems.put(3, new MenuItem("Testing CustomerDAO (Hibernate)", customerAction, null));
        mainItems.put(4, new MenuItem("Testing ServiceAdvisorDAO (Hibernate)", serviceAdvisorAction, null));
        mainItems.put(5, new MenuItem("Testing OrderDAO (Hibernate)", orderAction, null));

        rootMenu = new Menu("Testing Hibernate DAO Implementation", mainItems);
    }
}