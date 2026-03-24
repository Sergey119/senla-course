package com.example.task.ui;

import com.example.task.dao.*;

import java.util.HashMap;
import java.util.Map;

public class Builder {
    private Menu rootMenu;

    private final CarPlaceDAO carPlaceDAO;
    private final TechnicianDAO technicianDAO;
    private final CustomerDAO customerDAO;
    private final ServiceAdvisorDAO serviceAdvisorDAO;
    private final OrderDAO orderDAO;

    public Builder(CarPlaceDAO carPlaceDAO, TechnicianDAO technicianDAO,
                   CustomerDAO customerDAO, ServiceAdvisorDAO serviceAdvisorDAO,
                   OrderDAO orderDAO) {
        this.carPlaceDAO = carPlaceDAO;
        this.technicianDAO = technicianDAO;
        this.customerDAO = customerDAO;
        this.serviceAdvisorDAO = serviceAdvisorDAO;
        this.orderDAO = orderDAO;
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {
        var mainItems = new HashMap<Integer, MenuItem>();

        var carPlaceAction = new CarPlaceAction(carPlaceDAO);
        var technicianAction = new TechnicianAction(technicianDAO);
        var customerAction = new CustomerAction(customerDAO);
        var serviceAdvisorAction = new ServiceAdvisorAction(serviceAdvisorDAO);
        var orderAction = new OrderAction(orderDAO, carPlaceDAO, customerDAO,
                serviceAdvisorDAO, technicianDAO);

        mainItems.put(0, new MenuItem("Exit", null, null));
        mainItems.put(1, new MenuItem("Testing CarPlaceDAO", carPlaceAction, null));
        mainItems.put(2, new MenuItem("Testing TechnicianDAO", technicianAction, null));
        mainItems.put(3, new MenuItem("Testing CustomerDAO", customerAction, null));
        mainItems.put(4, new MenuItem("Testing ServiceAdvisorDAO", serviceAdvisorAction, null));
        mainItems.put(5, new MenuItem("Testing OrderDAO", orderAction, null));

        rootMenu = new Menu("Testing DAO classes", mainItems);
    }
}