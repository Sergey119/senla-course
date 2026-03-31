package com.example.task.ui;

import com.example.task.dao.hibernate.CarPlaceHibernateDAO;
import com.example.task.model.CarPlace;
import java.util.List;

public class CarPlaceAction implements IAction {

    private final CarPlaceHibernateDAO carPlaceDAO;

    public CarPlaceAction() {
        this.carPlaceDAO = new CarPlaceHibernateDAO();
    }

    @Override
    public void execute() {
        System.out.println("--- Testing Hibernate CarPlaceDAO ---");

        try {
            System.out.println("1. Creating car places...");
            CarPlace cp1 = new CarPlace(30, true, false);
            CarPlace cp2 = new CarPlace(25, false, false);

            Integer id1 = carPlaceDAO.save(cp1);
            Integer id2 = carPlaceDAO.save(cp2);
            System.out.println("Created with IDs: " + id1 + ", " + id2);

            System.out.println("\n2. Finding by ID...");
            CarPlace found1 = carPlaceDAO.findById(id1);
            CarPlace found2 = carPlaceDAO.findById(id2);
            System.out.println("Found 1: " + found1);
            System.out.println("Found 2: " + found2);

            System.out.println("\n3. Checking existence...");
            boolean exists = carPlaceDAO.existsById(id1);
            System.out.println("Exists: " + exists);

            System.out.println("\n4. Getting all car places...");
            List<CarPlace> all = carPlaceDAO.findAll();
            System.out.println("All: " + all);

            System.out.println("\n5. Available car places...");
            List<CarPlace> available = carPlaceDAO.findAvailableCarPlaces();
            System.out.println("Available: " + available.size());

            int count = carPlaceDAO.countAvailableCarPlaces();
            System.out.println("Count available: " + count);

            System.out.println("\n6. Updating occupation...");
            carPlaceDAO.updateOccupation(id1, true);
            CarPlace found = carPlaceDAO.findById(id1);
            System.out.println("Updated: " + found);

            System.out.println("\n7. Deleting...");
            carPlaceDAO.delete(id2);
            exists = carPlaceDAO.existsById(id2);
            System.out.println("After delete exists: " + exists);

            carPlaceDAO.delete(id1);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}