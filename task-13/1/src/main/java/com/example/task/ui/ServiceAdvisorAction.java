package com.example.task.ui;

import com.example.task.dao.hibernate.ServiceAdvisorHibernateDAO;
import com.example.task.model.ServiceAdvisor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdvisorAction implements IAction {

    private static final Logger logger = LogManager.getLogger(ServiceAdvisorAction.class);

    private final ServiceAdvisorHibernateDAO serviceAdvisorDAO;

    private Integer createdId1;
    private Integer createdId2;
    private Integer createdId3;

    public ServiceAdvisorAction() {
        this.serviceAdvisorDAO = new ServiceAdvisorHibernateDAO();
    }

    @Override
    public void execute() {
        System.out.println("--- Testing ServiceAdvisorHibernateDAO ---");

        try {
            testCreateServiceAdvisors();
            testExistsById();
            testFindById();
            testFindAllAndSorting();
            testUpdate();
            testExistsByName();
            testDelete();

            System.out.println("ServiceAdvisorHibernateDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing ServiceAdvisorDAO: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error in ServiceAdvisorAction: {}", e.getMessage(), e);
        } finally {
            // Очистка тестовых данных
            cleanupTestData();
        }
    }

    private void testCreateServiceAdvisors() {
        System.out.println("\n1. Creating service advisors...");
        logger.info("Start testing Hibernate method for creating service advisor records");

        try {
            var advisor1 = new ServiceAdvisor("Kathy Smith");
            var advisor2 = new ServiceAdvisor("Robin Moore");

            // Сохраняем через Hibernate - он сам сгенерирует ID
            createdId1 = serviceAdvisorDAO.save(advisor1);
            createdId2 = serviceAdvisorDAO.save(advisor2);

            System.out.println("Created advisors with IDs: " + createdId1 + ", " + createdId2);
            System.out.println("Advisor 1: " + serviceAdvisorDAO.findById(createdId1));
            System.out.println("Advisor 2: " + serviceAdvisorDAO.findById(createdId2));

            logger.info("Successfully created service advisors with IDs: {}, {}", createdId1, createdId2);

        } catch (Exception e) {
            logger.error("Error creating service advisors: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create service advisors", e);
        }
    }

    private void testExistsById() {
        System.out.println("\n2. Testing existsById()...");
        logger.info("Testing existsById() method");

        try {
            boolean existsValid = serviceAdvisorDAO.existsById(createdId1);
            System.out.println("Advisor with ID " + createdId1 + " exists: " + existsValid + " (expected true)");

            boolean existsInvalid = serviceAdvisorDAO.existsById(99999);
            System.out.println("Advisor with ID 99999 exists: " + existsInvalid + " (expected false)");

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
            ServiceAdvisor found = serviceAdvisorDAO.findById(createdId1);
            if (found != null) {
                System.out.println("Found advisor by ID " + createdId1 + ":");
                System.out.println("ID: " + found.getId());
                System.out.println("Name: " + found.getName());
                System.out.println("Full object: " + found);
            } else {
                System.out.println("Advisor with ID " + createdId1 + " not found!");
            }

            ServiceAdvisor notFound = serviceAdvisorDAO.findById(99999);
            if (notFound == null) {
                System.out.println("  ✓ Correctly returned null for non-existent ID 99999");
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
            List<ServiceAdvisor> allAdvisors = serviceAdvisorDAO.findAll();
            allAdvisors.forEach(a -> System.out.println("ID: " + a.getId() + ", name: " + a.getName()));

            List<ServiceAdvisor> sortedByName = serviceAdvisorDAO.findAllSortedByName();
            sortedByName.forEach(a -> System.out.println(" - " + a.getName()));

            int totalCount = serviceAdvisorDAO.countAll();
            System.out.println("\n  Total count (via countAll()): " + totalCount);

        } catch (Exception e) {
            logger.error("Error testing findAll methods: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testUpdate() {
        System.out.println("\n5. Testing update()...");
        logger.info("Testing update() method");

        try {
            // Получаем консультанта для обновления
            ServiceAdvisor advisor = serviceAdvisorDAO.findById(createdId1);
            if (advisor == null) {
                System.out.println("  ✗ Advisor for update not found!");
                return;
            }

            System.out.println("  Advisor before update:");
            System.out.println("    ID: " + advisor.getId());
            System.out.println("    Name: " + advisor.getName());

            String oldName = advisor.getName();

            String newName = "Kathy Updated";
            advisor.setName(newName);

            serviceAdvisorDAO.update(advisor);
            System.out.println("\n  Updated name to: " + newName);

            ServiceAdvisor updatedAdvisor = serviceAdvisorDAO.findById(createdId1);
            if (updatedAdvisor != null && newName.equals(updatedAdvisor.getName())) {
                System.out.println("Update verified successfully!");
            } else {
                System.out.println("Update verification failed!");
            }

            System.out.println("Testing updateName() method:");
            serviceAdvisorDAO.updateName(createdId1, "Kathy Updated (via updateName)");
            ServiceAdvisor viaUpdateName = serviceAdvisorDAO.findById(createdId1);
            System.out.println("Name after updateName(): " +
                    (viaUpdateName != null ? viaUpdateName.getName() : "null"));

            advisor.setName(oldName);
            serviceAdvisorDAO.update(advisor);
            System.out.println("  Restored original name: " + oldName);

        } catch (Exception e) {
            logger.error("Error testing update: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testExistsByName() {
        System.out.println("\n6. Testing existsByName()...");
        logger.info("Testing existsByName() method");

        try {
            boolean existsKeith = serviceAdvisorDAO.existsByName("Kathy Smith");
            System.out.println("Advisor 'Kathy Smith' exists: " + existsKeith + " (expected true)");

            boolean existsFake = serviceAdvisorDAO.existsByName("Non existent advisor");
            System.out.println("Advisor 'Non Existent Advisor' exists: " + existsFake + " (expected false)");

        } catch (Exception e) {
            logger.error("Error testing existsByName: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testDelete() {
        System.out.println("\n8. Testing delete()...");
        logger.info("Testing delete() method");

        try {
            ServiceAdvisor advisorToDelete = new ServiceAdvisor("Advisor for deletion test");
            createdId3 = serviceAdvisorDAO.save(advisorToDelete);
            System.out.println("Created advisor for deletion with ID: " + createdId3);

            boolean existsBefore = serviceAdvisorDAO.existsById(createdId3);
            System.out.println("Advisor exists before deletion: " + existsBefore);

            System.out.println("Deleting advisor with ID: " + createdId3);
            serviceAdvisorDAO.delete(createdId3);
            System.out.println("Delete method completed");

            boolean existsAfter = serviceAdvisorDAO.existsById(createdId3);
            System.out.println("Advisor exists after deletion: " + existsAfter);

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
                if (serviceAdvisorDAO.existsById(id)) {
                    try {
                        serviceAdvisorDAO.delete(id);
                        logger.info("Deleted advisor with ID: {}", id);
                    } catch (Exception e) {
                        logger.error("Error deleting advisor ID {}: {}", id, e.getMessage());
                    }
                }
            }

            // Финальная проверка
            boolean allDeleted = true;
            for (Integer id : idsToDelete) {
                if (serviceAdvisorDAO.existsById(id)) {
                    logger.warn("WARNING: Advisor with ID {} still exists!", id);
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