package com.example.task.ui;

import com.example.task.dao.CarPlaceDAO;
import com.example.task.model.CarPlace;

import java.util.List;

public class CarPlaceAction implements IAction {

    private final CarPlaceDAO carPlaceDAO;

    private static final Integer testId1 = 101;
    private static final Integer testId2 = 102;
    private static final Integer testId3 = 103;

    public CarPlaceAction(CarPlaceDAO carPlaceDAO) {
        this.carPlaceDAO = carPlaceDAO;
    }

    @Override
    public void execute() {
        System.out.println("--- Testing CarPlaceDAO ---");

        try {
            testCreateCarPlaces();
            testExistsById();
            testFindById();
            testFindAll();
            testSpecificMethods();
            testUpdate();
            testDelete();

            System.out.println("CarPlaceDAO testing complete!");

        } catch (Exception e) {
            System.err.println("Critical error while testing CarPlaceDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateCarPlaces() {
        System.out.println("1. Creating car places (id is specified via a setter).");

        var carPlace1 = new CarPlace(30, true, false);
        carPlace1.setId(testId1);

        var carPlace2 = new CarPlace(25, false, true);
        carPlace2.setId(testId2);

        try {
            carPlaceDAO.save(carPlace1);
            carPlaceDAO.save(carPlace2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Records have been created in the database.");
        carPlaceDAO.findById(testId1).ifPresent(System.out::println);
        carPlaceDAO.findById(testId2).ifPresent(System.out::println);
    }

    private void testExistsById() {
        System.out.println("2. Testing existsById().");

        try {
            var exists = carPlaceDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            var exists = carPlaceDAO.existsById(testId1);
            System.out.println("Executing method -> existsById(" + testId1 + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFindById() {
        System.out.println("3. Testing findById().");

        try {
            carPlaceDAO.findById(256).ifPresentOrElse(
                    cp -> System.out.println("Found (wrong! it shouldn't have been found): " + cp),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            carPlaceDAO.findById(testId1).ifPresentOrElse(
                    cp -> System.out.println("Found (expected): " + cp),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFindAll() {
        System.out.println("4. Testing retrieval of all records from the database.");

        try {
            var list = carPlaceDAO.findAll();
            System.out.println("Total records: " + list.size());
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testSpecificMethods() {
        System.out.println("5. Testing specific methods.");

        try {
            var list = carPlaceDAO.findAvailableCarPlaces();
            System.out.println("Available (unoccupied) car places: " + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            var count = carPlaceDAO.countAvailableCarPlaces();
            System.out.println("Number of available (unoccupied) car places: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Occupation status update.");
            carPlaceDAO.findById(testId1).ifPresentOrElse(
                    System.out::println, () -> System.out.println("Error, not found! x1")
            );
            carPlaceDAO.updateOccupation(testId1, true);
            carPlaceDAO.findById(testId1).ifPresentOrElse(
                    cp -> System.out.println("Status updated: " + cp),
                    () -> System.out.println("Error, not found! x2")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testUpdate() {
        System.out.println("6. Testing update");

        try {
            var carPlace = new CarPlace(40, true, true);
            carPlace.setId(testId1);

            carPlaceDAO.update(carPlace);
            carPlaceDAO.findById(testId1).ifPresentOrElse(
                    cp -> System.out.println("Record updated: " + cp),
                    () -> System.out.println("Error, not found!")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testDelete() {
        System.out.println("7. Testing delete");

        try {
            var forDelete = new CarPlace(20, false, false);
            forDelete.setId(testId3);

            try {
                carPlaceDAO.save(forDelete);
                System.out.println("A record has been created for deletion: " + forDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }

            var before = carPlaceDAO.existsById(testId3);
            carPlaceDAO.delete(testId3);
            var after = carPlaceDAO.existsById(testId3);
            System.out.println("Delete: before=" + before + ", after=" + after);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanupTestData() {
        System.out.println("Clean");
        try {
            carPlaceDAO.delete(testId1);
            carPlaceDAO.delete(testId2);
            carPlaceDAO.delete(testId3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}