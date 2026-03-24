package com.example.task.ui;

import com.example.task.dao.hibernate.TechnicianHibernateDAO;
import com.example.task.model.Technician;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TechnicianAction implements IAction {

    private static final Logger logger = LogManager.getLogger(TechnicianAction.class);

    private final TechnicianHibernateDAO technicianDAO;

    private Integer createdId1;
    private Integer createdId2;
    private Integer createdId3;

    public TechnicianAction() {
        this.technicianDAO = new TechnicianHibernateDAO();
    }

    @Override
    public void execute() {
        System.out.println("--- Testing TechnicianHibernateDAO ---");

        try {
            testCreateTechnicians();
            testExistsById();
            testFindById();
            testFindAllAndSorting();
            testAvailableTechnicians();
            testUpdate();
            testSpecificUpdateMethods();
            testDelete();

            System.out.println("TechnicianHibernateDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing TechnicianDAO: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error in TechnicianAction: {}", e.getMessage(), e);
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateTechnicians() {
        System.out.println("\n1. Creating technicians...");
        logger.info("Start testing Hibernate method for creating technician records");

        try {
            var technician1 = new Technician("Luis Reed", "Technical engineer", true);
            var technician2 = new Technician("Daniel Wood", "Motor mechanic", false);

            createdId1 = technicianDAO.save(technician1);
            createdId2 = technicianDAO.save(technician2);

            System.out.println("Created technicians with IDs: " + createdId1 + ", " + createdId2);
            System.out.println("Technician 1: " + technicianDAO.findById(createdId1));
            System.out.println("Technician 2: " + technicianDAO.findById(createdId2));

            logger.info("Successfully created technicians with IDs: {}, {}", createdId1, createdId2);

        } catch (Exception e) {
            logger.error("Error creating technicians: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create technicians", e);
        }
    }

    private void testExistsById() {
        System.out.println("\n2. Testing existsById()...");
        logger.info("Testing existsById() method");

        try {
            boolean existsValid = technicianDAO.existsById(createdId1);
            System.out.println("Technician with ID " + createdId1 + " exists: " + existsValid + " (expected true)");

            boolean existsInvalid = technicianDAO.existsById(99999);
            System.out.println("Technician with ID 99999 exists: " + existsInvalid + " (expected false)");

            if (existsValid && !existsInvalid) {
                System.out.println("existsById() works correctly");
            }

        } catch (Exception e) {
            logger.error("Error testing existsById: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testFindById() {
        System.out.println("\n3. Testing findById()...");
        logger.info("Testing findById() method");

        try {
            Technician found = technicianDAO.findById(createdId1);
            if (found != null) {
                System.out.println("Found technician by ID " + createdId1 + ":");
                System.out.println("ID: " + found.getId());
                System.out.println("Name: " + found.getName());
                System.out.println("Specialization: " + found.getSpecialization());
                System.out.println("Available: " + found.getIsAvailable());
                System.out.println("Full object: " + found);
            } else {
                System.out.println("Technician with ID " + createdId1 + " not found!");
            }

            Technician notFound = technicianDAO.findById(99999);
            if (notFound == null) {
                System.out.println("Correctly returned null for non-existent ID 99999");
            }

        } catch (Exception e) {
            logger.error("Error testing findById: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testFindAllAndSorting() {
        System.out.println("\n4. Testing findAll() and sorting methods...");
        logger.info("Testing findAll() and sorting methods");

        try {
            List<Technician> allTechnicians = technicianDAO.findAll();
            allTechnicians.forEach(t -> System.out.println("ID: " + t.getId() + ", name: " + t.getName()));

            List<Technician> sortedByName = technicianDAO.findAllSortedByName();
            sortedByName.forEach(t -> System.out.println(" - " + t.getName() + " (" + t.getSpecialization() + ")"));

            List<Technician> sortedByAvailability = technicianDAO.findAllSortedByAvailability();
            sortedByAvailability.forEach(t -> System.out.println(" - " + t.getName() + " - available: " + t.getIsAvailable()));

        } catch (Exception e) {
            logger.error("Error testing findAll methods: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testAvailableTechnicians() {
        System.out.println("\n5. Testing available technicians...");
        logger.info("Testing specific search methods");

        try {
            System.out.println("Searching for available technicians:");
            List<Technician> availableTechs = technicianDAO.findAvailableTechnicians();
            availableTechs.forEach(t -> System.out.println(" - " + t.getName() + " (" + t.getSpecialization() + ")"));

        } catch (Exception e) {
            logger.error("Error testing specific search methods: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testUpdate() {
        System.out.println("\n6. Testing update()...");
        logger.info("Testing update() method");

        try {
            Technician technician = technicianDAO.findById(createdId1);
            if (technician == null) {
                System.out.println("Technician for update not found!");
                return;
            }

            System.out.println("Technician before update:");
            System.out.println("ID: " + technician.getId());
            System.out.println("Name: " + technician.getName());
            System.out.println("Specialization: " + technician.getSpecialization());
            System.out.println("Available: " + technician.getIsAvailable());

            String oldName = technician.getName();
            String oldSpecialization = technician.getSpecialization();
            Boolean oldAvailability = technician.getIsAvailable();

            technician.setName("Luis Updated");
            technician.setSpecialization("Senior Technical Engineer");
            technician.setIsAvailable(false);

            technicianDAO.update(technician);
            System.out.println("Updated technician:");
            System.out.println("Name: " + technician.getName());
            System.out.println("Specialization: " + technician.getSpecialization());
            System.out.println("Available: " + technician.getIsAvailable());

            Technician updatedTechnician = technicianDAO.findById(createdId1);
            if (updatedTechnician != null &&
                    "Luis Updated".equals(updatedTechnician.getName()) &&
                    "Senior Technical Engineer".equals(updatedTechnician.getSpecialization()) &&
                    !updatedTechnician.getIsAvailable()) {
                System.out.println("Update verified successfully!");
            } else {
                System.out.println("Update verification failed!");
            }

            technician.setName(oldName);
            technician.setSpecialization(oldSpecialization);
            technician.setIsAvailable(oldAvailability);
            technicianDAO.update(technician);
            System.out.println("Restored original values");

        } catch (Exception e) {
            logger.error("Error testing update: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testSpecificUpdateMethods() {
        System.out.println("\n7. Testing specific update methods...");
        logger.info("Testing specific update methods");

        try {
            System.out.println("Testing updateAvailability():");
            System.out.println("Before: " + technicianDAO.findById(createdId1).getIsAvailable());

            technicianDAO.updateAvailability(createdId1, false);
            Technician afterUpdate = technicianDAO.findById(createdId1);
            System.out.println("After setting to false: " + afterUpdate.getIsAvailable());

            technicianDAO.updateAvailability(createdId1, true);
            afterUpdate = technicianDAO.findById(createdId1);
            System.out.println("After setting back to true: " + afterUpdate.getIsAvailable());

            System.out.println("\n  Testing updateSpecialization():");
            System.out.println("Before: " + technicianDAO.findById(createdId2).getSpecialization());

            technicianDAO.updateSpecialization(createdId2, "Master Motor Mechanic");
            afterUpdate = technicianDAO.findById(createdId2);
            System.out.println("After update: " + afterUpdate.getSpecialization());

        } catch (Exception e) {
            logger.error("Error testing specific update methods: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testDelete() {
        System.out.println("\n8. Testing delete()...");
        logger.info("Testing delete() method");

        try {
            Technician technicianToDelete = new Technician("Technician for deletion test", "Test Specialist", true);
            createdId3 = technicianDAO.save(technicianToDelete);
            System.out.println("Created technician for deletion with ID: " + createdId3);

            boolean existsBefore = technicianDAO.existsById(createdId3);
            System.out.println("Technician exists before deletion: " + existsBefore);

            System.out.println("\n  Deleting technician with ID: " + createdId3);
            technicianDAO.delete(createdId3);
            System.out.println("Delete method completed");

            boolean existsAfter = technicianDAO.existsById(createdId3);
            System.out.println("Technician exists after deletion: " + existsAfter);

        } catch (Exception e) {
            logger.error("Error testing delete: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void cleanupTestData() {
        logger.info("Cleaning up test data...");

        var idsToDelete = new ArrayList<Integer>();
        idsToDelete.add(createdId1);
        idsToDelete.add(createdId2);
        idsToDelete.add(createdId3);

        try {
            for (Integer id : idsToDelete) {
                if (technicianDAO.existsById(id)) {
                    try {
                        technicianDAO.delete(id);
                        logger.info("Deleted technician with ID: {}", id);
                    } catch (Exception e) {
                        logger.error("Error deleting technician ID {}: {}", id, e.getMessage());
                    }
                }
            }

            // Финальная проверка
            boolean allDeleted = true;
            for (Integer id : idsToDelete) {
                if (technicianDAO.existsById(id)) {
                    logger.warn("WARNING: Technician with ID {} still exists!", id);
                    allDeleted = false;
                }
            }

            if (allDeleted) {
                logger.info("All test data successfully cleaned up");
            }

        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage(), e);
        }
    }
}