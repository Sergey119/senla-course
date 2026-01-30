package com.example.task.ui;

import com.example.task.dao.hibernate.*;
import com.example.task.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderAction implements IAction {

    private static final Logger logger = LogManager.getLogger(OrderAction.class);

    private final OrderHibernateDAO orderDAO;
    private final CarPlaceHibernateDAO carPlaceDAO;
    private final CustomerHibernateDAO customerDAO;
    private final ServiceAdvisorHibernateDAO serviceAdvisorDAO;
    private final TechnicianHibernateDAO technicianDAO;

    private Integer carPlaceId;
    private Integer customerId;
    private Integer advisorId;
    private Integer technicianId1;
    private Integer technicianId2;
    private Integer orderId;

    public OrderAction() {
        this.orderDAO = new OrderHibernateDAO();
        this.carPlaceDAO = new CarPlaceHibernateDAO();
        this.customerDAO = new CustomerHibernateDAO();
        this.serviceAdvisorDAO = new ServiceAdvisorHibernateDAO();
        this.technicianDAO = new TechnicianHibernateDAO();
    }

    @Override
    public void execute() {
        System.out.println("--- Testing OrderHibernateDAO ---");

        try {
            clearDatabase();

            prepareTestData();
            testCreateOrder();
            testExistsById();
            testFindById();
            testFindAll();
            testSpecificFindMethods();
            testSortingMethods();
            testTechnicianRelations();
            testUpdateStatus();
            testTimeShift();
            testFindOrdersByTechnician();
            testDateRangeSearch();
            testRunningOrders();
            testUpdateOrder();

            System.out.println("OrderHibernateDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing OrderHibernateDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void prepareTestData() {
        System.out.println("1. Preparing test data...");

        try {
            var testCarPlace = new CarPlace(40, true, false);
            Integer savedCarPlaceId = carPlaceDAO.save(testCarPlace);
            System.out.println("Created car place with ID: " + savedCarPlaceId);

            var testCustomer = new Customer("Test Customer for order");
            Integer savedCustomerId = customerDAO.save(testCustomer);
            System.out.println("Created customer with ID: " + savedCustomerId);

            var testAdvisor = new ServiceAdvisor("Test Advisor");
            Integer savedAdvisorId = serviceAdvisorDAO.save(testAdvisor);
            System.out.println("Created advisor with ID: " + savedAdvisorId);

            var testTech1 = new Technician("Technician 1 for Order", "Diagnostics", true);
            Integer savedTechId1 = technicianDAO.save(testTech1);
            System.out.println("Created technician 1 with ID: " + savedTechId1);

            var testTech2 = new Technician("Technician 2 for Order", "Repair", true);
            Integer savedTechId2 = technicianDAO.save(testTech2);
            System.out.println("Created technician 2 with ID: " + savedTechId2);

            carPlaceId = savedCarPlaceId;
            customerId = savedCustomerId;
            advisorId = savedAdvisorId;
            technicianId1 = savedTechId1;
            technicianId2 = savedTechId2;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testCreateOrder() {
        System.out.println("2. Creating order with technicians...");
        logger.info("Testing the Hibernate method for creating an order record with technicians");

        try {
            ServiceAdvisor advisor = serviceAdvisorDAO.findById(advisorId);
            Customer customer = customerDAO.findById(customerId);
            CarPlace carPlace = carPlaceDAO.findById(carPlaceId);

            if (advisor == null || customer == null || carPlace == null) {
                System.out.println("Required test data not found!");
                return;
            }

            // Техники будут добавлены отдельно
            var testOrder = new Order(
                    advisor, null, customer, carPlace, OrderStatus.PENDING, 10000, LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
            );

            orderDAO.saveOrderWithTechnicians(testOrder, Arrays.asList(technicianId1, technicianId2));
            System.out.println("Order created.");

            List<Order> allOrders = orderDAO.findAll();
            if (!allOrders.isEmpty()) {
                Order savedOrder = allOrders.get(allOrders.size() - 1);
                orderId = savedOrder.getId();
                System.out.println("Created order with ID: " + orderId);
            }

        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            logger.error(e.getMessage());
        }
    }

    private void testExistsById() {
        System.out.println("3. Testing existsById().");
        logger.info("Start of testing the Hibernate method for existing order record");

        try {
            boolean exists = orderDAO.existsById(99999);
            System.out.println("Executing method -> existsById(99999): " + exists + " (expected false)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            boolean exists = orderDAO.existsById(orderId);
            System.out.println("Executing method -> existsById(" + orderId + "): " + exists + " (expected true)");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for existing order record");
    }

    private void testFindById() {
        System.out.println("4. Testing findById().");
        logger.info("Start of testing the Hibernate method for finding order record");

        try {
            Order order = orderDAO.findById(99999);
            if (order != null) {
                System.out.println("Found (wrong! it shouldn't have been found): " + order);
            } else {
                System.out.println("Not found (expected)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            Order order = orderDAO.findById(orderId);
            if (order != null) {
                System.out.println("Found (expected): " + order);
            } else {
                System.out.println("Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for finding order record");
    }

    private void testFindAll() {
        System.out.println("5. Testing retrieval of all records from the database.");
        logger.info("Start of testing the Hibernate method for retrieval of all records");

        try {
            var list = orderDAO.findAll();
            System.out.println("Total records: " + list.size());
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for retrieval of all records");
    }

    private void testSpecificFindMethods() {
        System.out.println("6. Testing specific search methods.");
        logger.info("Start of testing the Hibernate method for specific order search methods");

        try {
            var list = orderDAO.findByStatus(OrderStatus.PENDING);
            System.out.println("Found by status PENDING: " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for specific order search methods");
    }

    private void testSortingMethods() {
        System.out.println("7. Testing sorting methods.");
        logger.info("Start of testing the Hibernate method for sorting order records");

        try {
            var list = orderDAO.findAllSortedByStartDate();
            System.out.println("Sorted by start date: " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findAllSortedByCost();
            System.out.println("Sorted by cost: " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for sorting order records");
    }

    private void testTechnicianRelations() {
        System.out.println("8. Testing technician relations.");
        logger.info("Start of testing the Hibernate method for technician relations");

        try {
            orderDAO.addTechnicianToOrder(orderId, technicianId1);
            System.out.println("Added relation: order=" + orderId + ", tech=" + technicianId1);

            var technicianIds = orderDAO.findTechnicianIdsByOrderId(orderId);
            System.out.println("Technicians in order: " + technicianIds.size());
            System.out.println("  " + technicianIds);

            orderDAO.removeTechnicianFromOrder(orderId, technicianId1);
            System.out.println("Removed relation: order=" + orderId + ", tech=" + technicianId1);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for technician relations");
    }

    private void testUpdateStatus() {
        System.out.println("9. Testing status update.");
        logger.info("Start of testing the Hibernate method for status update");

        try {
            System.out.println("Before update:");
            Order order = orderDAO.findById(orderId);
            if (order != null) {
                System.out.println("  " + order);
            } else {
                System.out.println("Order not found");
                return;
            }

            orderDAO.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS);
            System.out.println("Status updated to IN_PROGRESS");

            var inProgressOrders = orderDAO.findByStatus(OrderStatus.IN_PROGRESS);
            System.out.println("IN_PROGRESS orders after update: " + inProgressOrders.size());

            System.out.println("After update:");
            Order updatedOrder = orderDAO.findById(orderId);
            if (updatedOrder != null) {
                System.out.println("  " + updatedOrder);
            } else {
                System.out.println("Order not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for status update");
    }

    private void testTimeShift() {
        System.out.println("10. Testing time shift.");
        logger.info("Start of testing the Hibernate method for time shift");

        try {
            System.out.println("Before shift:");
            Order order = orderDAO.findById(orderId);
            if (order != null) {
                System.out.println("End date before: " + order.getEndDate());
            } else {
                System.out.println("Order not found");
                return;
            }

            orderDAO.shiftOrderTime(orderId, 2);
            System.out.println("End date shifted by +4 hours");

            System.out.println("After shift:");
            Order shiftedOrder = orderDAO.findById(orderId);
            if (shiftedOrder != null) {
                System.out.println("End date after: " + shiftedOrder.getEndDate());
            } else {
                System.out.println("Order not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for time shift");
    }

    private void testFindOrdersByTechnician() {
        System.out.println("11. Testing find orders by technician.");
        logger.info("Start of testing the Hibernate method for find orders by technician");

        try {
            var list = orderDAO.findOrdersByTechnicianId(technicianId1);
            System.out.println("Orders for technician ID=" + technicianId1 + ": " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for find orders by technician");
    }

    private void testDateRangeSearch() {
        System.out.println("12. Testing date range search.");
        logger.info("Start of testing the Hibernate method for date range search");

        try {
            var startDate = LocalDateTime.now().minusDays(7);
            var endDate = LocalDateTime.now().plusDays(7);
            var list = orderDAO.findOrdersByDateRange(startDate, endDate, OrderStatus.PENDING);
            System.out.println("PENDING orders in date range (last 7 days + next 7 days): " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for date range search");
    }

    private void testRunningOrders() {
        System.out.println("13. Testing running orders.");
        logger.info("Start of testing the Hibernate method for showing running orders.");

        try {
            var list = orderDAO.findCurrentlyRunningOrdersSortedByStartDate();
            System.out.println("Currently running orders sorted by start date: " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findCurrentlyRunningOrdersSortedByCost();
            System.out.println("Currently running orders sorted by cost: " + list.size() + " records");
            list.forEach(order -> System.out.println("  - " + order));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for showing running orders");
    }

    private void testUpdateOrder() {
        System.out.println("14. Testing order update.");
        logger.info("Start of testing the Hibernate method for update order.");

        try {
            Order orderToUpdate = orderDAO.findById(orderId);
            if (orderToUpdate == null) {
                System.out.println("Order not found for update");
                return;
            }

            orderToUpdate.setStatus(OrderStatus.COMPLETED);
            orderToUpdate.setCost(15000);
            orderToUpdate.setCreatedDate(LocalDateTime.now());
            orderToUpdate.setStartDate(LocalDateTime.now().plusDays(1));
            orderToUpdate.setLoadingDate(LocalDateTime.now().plusDays(1));
            orderToUpdate.setEndDate(LocalDateTime.now().plusDays(2));

            orderDAO.update(orderToUpdate);
            System.out.println("Order updated: ID=" + orderId);

            Order updatedOrder = orderDAO.findById(orderId);
            if (updatedOrder != null) {
                System.out.println("Record updated: " + updatedOrder);
            } else {
                System.out.println("Error, not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the Hibernate method for update order");
    }

    private void clearDatabase() {
        System.out.println("Clearing database before tests...");
        try {
            List<Order> orders = orderDAO.findAll();
            for (Order order : orders) {
                orderDAO.delete(order.getId());
            }

            List<Technician> technicians = technicianDAO.findAll();
            for (Technician tech : technicians) {
                technicianDAO.delete(tech.getId());
            }

            List<ServiceAdvisor> advisors = serviceAdvisorDAO.findAll();
            for (ServiceAdvisor advisor : advisors) {
                serviceAdvisorDAO.delete(advisor.getId());
            }

            List<Customer> customers = customerDAO.findAll();
            for (Customer customer : customers) {
                customerDAO.delete(customer.getId());
            }

            List<CarPlace> carPlaces = carPlaceDAO.findAll();
            for (CarPlace carPlace : carPlaces) {
                carPlaceDAO.delete(carPlace.getId());
            }

            System.out.println("Database cleared successfully.");
        } catch (Exception e) {
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }

    private void cleanupTestData() {
        System.out.println("\nCleaning up test data...");

        try {
            if (orderId != null) {
                orderDAO.deleteOrderWithRelations(orderId);
                System.out.println("Deleted order with ID: " + orderId);
            }

            if (technicianId1 != null) {
                technicianDAO.delete(technicianId1);
                System.out.println("Deleted technician with ID: " + technicianId1);
            }

            if (technicianId2 != null) {
                technicianDAO.delete(technicianId2);
                System.out.println("Deleted technician with ID: " + technicianId2);
            }

            if (carPlaceId != null) {
                carPlaceDAO.delete(carPlaceId);
                System.out.println("Deleted car place with ID: " + carPlaceId);
            }

            if (customerId != null) {
                customerDAO.delete(customerId);
                System.out.println("Deleted customer with ID: " + customerId);
            }

            if (advisorId != null) {
                serviceAdvisorDAO.delete(advisorId);
                System.out.println("Deleted advisor with ID: " + advisorId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Cleanup completed successfully");
    }
}