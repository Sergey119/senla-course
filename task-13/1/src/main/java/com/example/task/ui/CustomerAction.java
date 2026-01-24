package com.example.task.ui;

import com.example.task.dao.hibernate.CustomerHibernateDAO;
import com.example.task.model.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CustomerAction implements IAction {

    private static final Logger logger = LogManager.getLogger(CustomerAction.class);

    private final CustomerHibernateDAO customerDAO;

    private Integer createdId1;
    private Integer createdId2;
    private Integer createdId3;

    public CustomerAction() {
        this.customerDAO = new CustomerHibernateDAO();
    }

    @Override
    public void execute() {
        System.out.println("--- Testing CustomerHibernateDAO ---");

        try {
            testCreateCustomers();
            testExistsById();
            testFindById();
            testFindAllAndSorting();
            testUpdate();
            testExistsByName();
            testDelete();

            System.out.println("CustomerHibernateDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing CustomerDAO: " + e.getMessage());
            e.printStackTrace();
            logger.error("Error in CustomerAction: {}", e.getMessage(), e);
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateCustomers() {
        System.out.println("\n1. Creating customers...");
        logger.info("Start testing Hibernate method for creating customer records");

        try {
            var customer1 = new Customer("Colleen Rowe");
            var customer2 = new Customer("Cynthia Craig");

            createdId1 = customerDAO.save(customer1);
            createdId2 = customerDAO.save(customer2);

            System.out.println("Created customers with IDs: " + createdId1 + ", " + createdId2);
            System.out.println("Customer 1: " + customerDAO.findById(createdId1));
            System.out.println("Customer 2: " + customerDAO.findById(createdId2));

            logger.info("Successfully created customers with IDs: {}, {}", createdId1, createdId2);

        } catch (Exception e) {
            logger.error("Error creating customers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create customers", e);
        }
    }

    private void testExistsById() {
        System.out.println("\n2. Testing existsById()...");
        logger.info("Testing existsById() method");

        try {
            boolean existsValid = customerDAO.existsById(createdId1);
            System.out.println("Customer with ID " + createdId1 + " exists: " + existsValid + " (expected true)");

            boolean existsInvalid = customerDAO.existsById(1000);
            System.out.println("Customer with ID 1000 exists: " + existsInvalid + " (expected false)");

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
            Customer found = customerDAO.findById(createdId1);
            if (found != null) {
                System.out.println("Found customer by ID " + createdId1 + ":");
                System.out.println("ID: " + found.getId());
                System.out.println("Name: " + found.getName());
                System.out.println("Full object: " + found);
            } else {
                System.out.println("Customer with ID " + createdId1 + " not found!");
            }

            Customer notFound = customerDAO.findById(99999);
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
            List<Customer> allCustomers = customerDAO.findAll();
            allCustomers.forEach(c -> System.out.println("ID: " + c.getId() + ", name: " + c.getName()));

            List<Customer> sortedByName = customerDAO.findAllSortedByName();
            sortedByName.forEach(c -> System.out.println(" - " + c.getName()));

            int totalCount = customerDAO.countAll();
            System.out.println("Total count (via countAll()): " + totalCount);

        } catch (Exception e) {
            logger.error("Error testing findAll methods: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testUpdate() {
        System.out.println("\n5. Testing update()...");
        logger.info("Testing update() method");

        try {
            Customer customer = customerDAO.findById(createdId1);
            if (customer == null) {
                System.out.println("Customer for update not found!");
                return;
            }

            System.out.println("Customer before update:");
            System.out.println("ID: " + customer.getId());
            System.out.println("Name: " + customer.getName());

            String oldName = customer.getName();

            String newName = "Paul Updated";
            customer.setName(newName);

            customerDAO.update(customer);
            System.out.println("Updated name to: " + newName);

            Customer updatedCustomer = customerDAO.findById(createdId1);
            if (updatedCustomer != null && newName.equals(updatedCustomer.getName())) {
                System.out.println("Update verified successfully!");
            } else {
                System.out.println("Update verification failed!");
            }

            System.out.println("Testing updateName() method:");
            customerDAO.updateName(createdId1, "Paul Updated (via updateName)");
            Customer viaUpdateName = customerDAO.findById(createdId1);
            System.out.println("Name after updateName(): " +
                    (viaUpdateName != null ? viaUpdateName.getName() : "null"));

            customer.setName(oldName);
            customerDAO.update(customer);
            System.out.println("Restored original name: " + oldName);

        } catch (Exception e) {
            logger.error("Error testing update: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testExistsByName() {
        System.out.println("\n6. Testing existsByName()...");
        logger.info("Testing existsByName() method");

        try {
            boolean existsMargaret = customerDAO.existsByName("Cynthia Craig");
            System.out.println("Customer 'Cynthia Craig' exists: " + existsMargaret + " (expected true)");

            boolean existsFake = customerDAO.existsByName("Non existent customer");
            System.out.println("Customer 'Non existent customer' exists: " + existsFake + " (expected false)");

        } catch (Exception e) {
            logger.error("Error testing existsByName: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void testDelete() {
        System.out.println("\n8. Testing delete()...");
        logger.info("Testing delete() method");

        try {
            Customer customerToDelete = new Customer("Customer for deletion test");
            createdId3 = customerDAO.save(customerToDelete);
            System.out.println("Created customer for deletion with ID: " + createdId3);

            boolean existsBefore = customerDAO.existsById(createdId3);
            System.out.println("Customer exists before deletion: " + existsBefore);

            System.out.println("Deleting customer with ID: " + createdId3);
            customerDAO.delete(createdId3);
            System.out.println("Delete method completed");

            boolean existsAfter = customerDAO.existsById(createdId3);
            System.out.println("Customer exists after deletion: " + existsAfter);

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
                if (customerDAO.existsById(id)) {
                    try {
                        customerDAO.delete(id);
                        logger.info("Deleted customer with ID: {}", id);
                    } catch (Exception e) {
                        logger.error("Error deleting customer ID {}: {}", id, e.getMessage());
                    }
                }
            }

            // Финальная проверка
            boolean allDeleted = true;
            for (Integer id : idsToDelete) {
                if (customerDAO.existsById(id)) {
                    logger.warn("WARNING: Customer with ID {} still exists!", id);
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