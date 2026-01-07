package com.example.task.ui;

import com.example.task.dao.ServiceAdvisorDAO;
import com.example.task.model.ServiceAdvisor;

import java.util.List;

public class ServiceAdvisorAction implements IAction {

    private final ServiceAdvisorDAO serviceAdvisorDAO;

    private static final Integer testId1 = 301;
    private static final Integer testId2 = 302;
    private static final Integer testId3 = 303;

    public ServiceAdvisorAction(ServiceAdvisorDAO serviceAdvisorDAO) {
        this.serviceAdvisorDAO = serviceAdvisorDAO;
    }

    @Override
    public void execute() {
        System.out.println("--- Testing ServiceAdvisorDAO ---");

        try {
            testCreateServiceAdvisors();
            testExistsById();
            testFindById();
            testFindAll();
            testUpdate();
            testDelete();

            System.out.println("ServiceAdvisorDAO testing complete!");

        } catch (Exception e) {
            System.err.println("Critical error while testing ServiceAdvisorDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void testCreateServiceAdvisors() {
        System.out.println("1. Creating service advisors (id is specified via a setter).");

        var advisor1 = new ServiceAdvisor("Keith Oliver");
        advisor1.setId(testId1);

        var advisor2 = new ServiceAdvisor("Robin Moore");
        advisor2.setId(testId2);

        try {
            serviceAdvisorDAO.save(advisor1);
            serviceAdvisorDAO.save(advisor2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Records have been created in the database.");
        serviceAdvisorDAO.findById(testId1).ifPresent(System.out::println);
        serviceAdvisorDAO.findById(testId2).ifPresent(System.out::println);
    }

    private void testExistsById() {
        System.out.println("2. Testing existsById().");

        try {
            var exists = serviceAdvisorDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            var exists = serviceAdvisorDAO.existsById(testId1);
            System.out.println("Executing method -> existsById(" + testId1 + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFindById() {
        System.out.println("3. Testing findById().");

        try {
            serviceAdvisorDAO.findById(256).ifPresentOrElse(
                    sa -> System.out.println("Found (wrong! it shouldn't have been found): " + sa),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            serviceAdvisorDAO.findById(testId1).ifPresentOrElse(
                    sa -> System.out.println("Found (expected): " + sa),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFindAll() {
        System.out.println("4. Testing retrieval of all records from the database.");

        try {
            var list = serviceAdvisorDAO.findAll();
            System.out.println("Total records: " + list.size());
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testUpdate() {
        System.out.println("5. Testing update");

        try {
            var advisor = new ServiceAdvisor("Jeremy Woods");
            advisor.setId(testId1);

            serviceAdvisorDAO.update(advisor);
            serviceAdvisorDAO.findById(testId1).ifPresentOrElse(
                    sa -> System.out.println("Record updated: " + sa),
                    () -> System.out.println("Error, not found!")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testDelete() {
        System.out.println("6. Testing delete");

        try {
            var forDelete = new ServiceAdvisor("Sara Stevenson");
            forDelete.setId(testId3);

            try {
                serviceAdvisorDAO.save(forDelete);
                System.out.println("A record has been created for deletion: " + forDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }

            var before = serviceAdvisorDAO.existsById(testId3);
            serviceAdvisorDAO.delete(testId3);
            var after = serviceAdvisorDAO.existsById(testId3);
            System.out.println("Delete: before=" + before + ", after=" + after);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanupTestData() {
        System.out.println("Clean");
        try {
            serviceAdvisorDAO.delete(testId1);
            serviceAdvisorDAO.delete(testId2);
            serviceAdvisorDAO.delete(testId3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}