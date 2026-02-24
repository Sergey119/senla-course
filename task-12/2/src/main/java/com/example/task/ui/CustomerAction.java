package com.example.task.ui;

import com.example.task.dao.CustomerDAO;
import com.example.task.model.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerAction implements IAction {

    private static final Logger logger = LogManager.getLogger(CustomerAction.class);

    private final CustomerDAO customerDAO;

    private static final Integer testId1 = 201;
    private static final Integer testId2 = 202;
    private static final Integer testId3 = 203;

    public CustomerAction(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public void execute() {
        System.out.println("--- Testing CustomerDAO ---");

        try {
            testCreateCustomers();
            testExistsById();
            testFindById();
            testFindAll();
            testUpdate();
            testDelete();

            System.out.println("CustomerDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing CustomerDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateCustomers() {
        System.out.println("1. Creating customers (id is specified via a setter).");
        logger.info("Start of testing the JDBC method for creating a customer record");

        var customer1 = new Customer("Margaret Rodriguez");
        customer1.setId(testId1);

        var customer2 = new Customer("George Morris");
        customer2.setId(testId2);

        try {
            customerDAO.save(customer1);
            customerDAO.save(customer2);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        System.out.println("Records have been created in the database.");
        customerDAO.findById(testId1).ifPresent(System.out::println);
        customerDAO.findById(testId2).ifPresent(System.out::println);
        logger.info("End of testing the JDBC method for creating a customer record");
    }

    private void testExistsById() {
        System.out.println("2. Testing existsById().");
        logger.info("Start of testing the JDBC method for existing customer record");

        try {
            var exists = customerDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var exists = customerDAO.existsById(testId1);
            System.out.println("Executing method -> existsById(" + testId1 + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for existing customer record");
    }

    private void testFindById() {
        System.out.println("3. Testing findById().");
        logger.info("Start of testing the JDBC method for finding a customer record");

        try {
            customerDAO.findById(256).ifPresentOrElse(
                    c -> System.out.println("Found (wrong! it shouldn't have been found): " + c),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            customerDAO.findById(testId1).ifPresentOrElse(
                    c -> System.out.println("Found (expected): " + c),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for finding a customer record");
    }

    private void testFindAll() {
        System.out.println("4. Testing retrieval of all records from the database.");
        logger.info("Start of testing the JDBC method for retrieving all records");

        try {
            var list = customerDAO.findAll();
            System.out.println("Total records: " + list.size());
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for retrieving all records");
    }

    private void testUpdate() {
        System.out.println("5. Testing update");
        logger.info("Start of testing the JDBC method for updating a customer record");

        try {
            var customer = new Customer("Deborah Kelly");
            customer.setId(testId1);

            customerDAO.update(customer);
            customerDAO.findById(testId1).ifPresentOrElse(
                    c -> System.out.println("Record updated: " + c),
                    () -> System.out.println("Error, not found!")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for updating a customer record");
    }

    private void testDelete() {
        System.out.println("6. Testing delete");
        logger.info("Start of testing the JDBC method for deleting a customer record");

        try {
            var forDelete = new Customer("Pamela Jones");
            forDelete.setId(testId3);

            try {
                customerDAO.save(forDelete);
                System.out.println("A record has been created for deletion: " + forDelete);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            var before = customerDAO.existsById(testId3);
            customerDAO.delete(testId3);
            var after = customerDAO.existsById(testId3);
            System.out.println("Delete: before=" + before + ", after=" + after);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End of testing the JDBC method for deleting a customer record");
    }

    private void cleanupTestData() {
        System.out.println("Clean");
        try {
            customerDAO.delete(testId1);
            customerDAO.delete(testId2);
            customerDAO.delete(testId3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}