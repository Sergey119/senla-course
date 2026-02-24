package com.example.task.ui;

import com.example.task.dao.CarPlaceDAO;
import com.example.task.model.CarPlace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarPlaceAction implements IAction {

    private static final Logger logger = LogManager.getLogger(CarPlaceAction.class);

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
            System.out.println("Critical error while testing CarPlaceDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateCarPlaces() {
        System.out.println("1. Creating car places (id is specified via a setter).");
        logger.info("Start of testing the JDBC method for creating a car place record");

        var carPlace1 = new CarPlace(30, true, false);
        carPlace1.setId(testId1);

        var carPlace2 = new CarPlace(25, false, true);
        carPlace2.setId(testId2);

        try {
            carPlaceDAO.save(carPlace1);
            carPlaceDAO.save(carPlace2);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        System.out.println("Records have been created in the database.");
        carPlaceDAO.findById(testId1).ifPresent(System.out::println);
        carPlaceDAO.findById(testId2).ifPresent(System.out::println);
        logger.info("Completion of testing the JDBC method for creating a car place record");
    }

    private void testExistsById() {
        System.out.println("2. Testing existsById().");
        logger.info("Start of testing the JDBC method for existing car place record");

        try {
            var exists = carPlaceDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var exists = carPlaceDAO.existsById(testId1);
            System.out.println("Executing method -> existsById(" + testId1 + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for existing car place record");
    }

    private void testFindById() {
        System.out.println("3. Testing findById().");
        logger.info("Start of testing the JDBC method for finding a car place record");

        try {
            carPlaceDAO.findById(256).ifPresentOrElse(
                    cp -> System.out.println("Found (wrong! it shouldn't have been found): " + cp),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            carPlaceDAO.findById(testId1).ifPresentOrElse(
                    cp -> System.out.println("Found (expected): " + cp),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for finding a car place record");
    }

    private void testFindAll() {
        System.out.println("4. Testing retrieval of all records from the database.");
        logger.info("Start of testing the JDBC method for retrieval of all records");

        try {
            var list = carPlaceDAO.findAll();
            System.out.println("Total records: " + list.size());
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for retrieval of all records");
    }

    private void testSpecificMethods() {
        System.out.println("5. Testing specific methods.");
        logger.info("Start of testing the JDBC method for specific car places methods.");

        try {
            var list = carPlaceDAO.findAvailableCarPlaces();
            System.out.println("Available (unoccupied) car places: " + list.size());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var count = carPlaceDAO.countAvailableCarPlaces();
            System.out.println("Number of available (unoccupied) car places: " + count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for specific car places methods");
    }

    private void testUpdate() {
        System.out.println("6. Testing update");
        logger.info("Start of testing the JDBC method for update car place record.");

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
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for update car place record");
    }

    private void testDelete() {
        System.out.println("7. Testing delete");
        logger.info("Start of testing the JDBC method for delete car place record.");

        try {
            var forDelete = new CarPlace(20, false, false);
            forDelete.setId(testId3);

            try {
                carPlaceDAO.save(forDelete);
                System.out.println("A record has been created for deletion: " + forDelete);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            var before = carPlaceDAO.existsById(testId3);
            carPlaceDAO.delete(testId3);
            var after = carPlaceDAO.existsById(testId3);
            System.out.println("Delete: before=" + before + ", after=" + after);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for delete car place record");
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