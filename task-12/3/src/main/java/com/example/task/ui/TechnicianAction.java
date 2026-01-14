package com.example.task.ui;

import com.example.task.dao.TechnicianDAO;
import com.example.task.model.Technician;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TechnicianAction implements IAction {

    private static final Logger logger = LogManager.getLogger(TechnicianAction.class);

    private final TechnicianDAO technicianDAO;

    private static final Integer testId1 = 401;
    private static final Integer testId2 = 402;
    private static final Integer testId3 = 403;

    public TechnicianAction(TechnicianDAO technicianDAO) {
        this.technicianDAO = technicianDAO;
    }

    @Override
    public void execute() {
        System.out.println("--- Testing TechnicianDAO ---");

        try {
            testCreateTechnicians();
            testExistsById();
            testFindById();
            testFindAll();
            testSpecificMethods();
            testUpdate();
            testDelete();

            System.out.println("TechnicianDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing TechnicianDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateTechnicians() {
        System.out.println("1. Creating technicians (id is specified via a setter).");
        logger.info("Start of testing the JDBC method for creating a technical record");

        var technician1 = new Technician("Luis Reed", "Technical engineer", true);
        technician1.setId(testId1);

        var technician2 = new Technician("Daniel Wood", "Motor mechanic", false);
        technician2.setId(testId2);

        try {
            technicianDAO.save(technician1);
            technicianDAO.save(technician2);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        System.out.println("Records have been created in the database.");
        technicianDAO.findById(testId1).ifPresent(System.out::println);
        technicianDAO.findById(testId2).ifPresent(System.out::println);
        logger.info("End of testing the JDBC method for creating a technicians");
    }

    private void testExistsById() {
        System.out.println("2. Testing existsById().");
        logger.info("Start of testing the JDBC method for existing technician record");

        try {
            var exists = technicianDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var exists = technicianDAO.existsById(testId1);
            System.out.println("Executing method -> existsById(" + testId1 + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for existing technician record");
    }

    private void testFindById() {
        System.out.println("3. Testing findById().");
        logger.info("Start of testing the JDBC method for finding technician record");

        try {
            technicianDAO.findById(256).ifPresentOrElse(
                    t -> System.out.println("Found (wrong! it shouldn't have been found): " + t),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            technicianDAO.findById(testId1).ifPresentOrElse(
                    t -> System.out.println("Found (expected): " + t),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for finding technician record");
    }

    private void testFindAll() {
        System.out.println("4. Testing retrieval of all records from the database.");
        logger.info("Start of testing the JDBC method for retrieval of all records");

        try {
            var list = technicianDAO.findAll();
            System.out.println("Total records: " + list.size());
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for retrieval of all records");
    }

    private void testSpecificMethods() {
        System.out.println("5. Testing specific methods.");
        logger.info("Start of testing the JDBC method for specific technician methods");

        try {
            var list = technicianDAO.findAvailableTechnicians();
            System.out.println("Available technicians: " + list.size() + " records");
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = technicianDAO.findAllSortedByName();
            System.out.println("Sorted by name: " + list.size() + " total records");
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = technicianDAO.findAllSortedByAvailability();
            System.out.println("Sorted by availability: " + list.size() + " total records");
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = technicianDAO.findByOrderId(1);
            System.out.println("Technicians for order 1: " + list.size() + " records");
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for specific technician methods");
    }

    private void testUpdate() {
        System.out.println("6. Testing update");
        logger.info("Start of testing the JDBC method for update");

        try {
            var technician = new Technician("Edward Horton", "Auto electrician", false);
            technician.setId(testId1);

            technicianDAO.update(technician);
            technicianDAO.findById(testId1).ifPresentOrElse(
                    t -> System.out.println("Record updated: " + t),
                    () -> System.out.println("Error, not found!")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for update");
    }

    private void testDelete() {
        System.out.println("7. Testing delete");
        logger.info("Start of testing the JDBC method for delete");

        try {
            var forDelete = new Technician("Kevin Bryant", "Diagnostic wizard", true);
            forDelete.setId(testId3);

            try {
                technicianDAO.save(forDelete);
                System.out.println("A record has been created for deletion: " + forDelete);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            var before = technicianDAO.existsById(testId3);
            technicianDAO.delete(testId3);
            var after = technicianDAO.existsById(testId3);
            System.out.println("Delete: before=" + before + ", after=" + after);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for delete");
    }

    private void cleanupTestData() {
        System.out.println("Clean");
        try {
            technicianDAO.delete(testId1);
            technicianDAO.delete(testId2);
            technicianDAO.delete(testId3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}