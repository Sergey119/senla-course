package com.example.task.ui;

import com.example.task.dao.*;
import com.example.task.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderAction implements IAction {

    private static final Logger logger = LogManager.getLogger(OrderAction.class);

    private final OrderDAO orderDAO;
    private final CarPlaceDAO carPlaceDAO;
    private final CustomerDAO customerDAO;
    private final ServiceAdvisorDAO serviceAdvisorDAO;
    private final TechnicianDAO technicianDAO;

    private final Integer carPlaceId = 501;
    private final Integer customerId = 501;
    private final Integer advisorId = 501;
    private final Integer technicianId1 = 501;
    private final Integer technicianId2 = 502;
    private final Integer orderId = 501;

    public OrderAction(OrderDAO orderDAO, CarPlaceDAO carPlaceDAO,
                       CustomerDAO customerDAO, ServiceAdvisorDAO serviceAdvisorDAO,
                       TechnicianDAO technicianDAO) {
        this.orderDAO = orderDAO;
        this.carPlaceDAO = carPlaceDAO;
        this.customerDAO = customerDAO;
        this.serviceAdvisorDAO = serviceAdvisorDAO;
        this.technicianDAO = technicianDAO;

        orderDAO.setCustomerDAO(customerDAO);
        orderDAO.setServiceAdvisorDAO(serviceAdvisorDAO);
        orderDAO.setCarPlaceDAO(carPlaceDAO);
        orderDAO.setTechnicianDAO(technicianDAO);
    }

    @Override
    public void execute() {
        System.out.println("--- Testing OrderDAO ---");

        try {
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

            System.out.println("OrderDAO testing complete!");

        } catch (Exception e) {
            System.out.println("Critical error while testing OrderDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private void prepareTestData() {
        System.out.println("1. Preparing test data.");

        try {

            if (!carPlaceDAO.existsById(carPlaceId)) {
                var testCarPlace = new CarPlace(40, true, false);
                testCarPlace.setId(carPlaceId);
                carPlaceDAO.save(testCarPlace);
                System.out.println("Created car place ID=" + carPlaceId);
            }

            if (!customerDAO.existsById(customerId)) {
                var testCustomer = new Customer("Test Customer for order");
                testCustomer.setId(customerId);
                customerDAO.save(testCustomer);
                System.out.println("Created customer ID=" + customerId);
            }

            if (!serviceAdvisorDAO.existsById(advisorId)) {
                var testAdvisor = new ServiceAdvisor("Test Advisor");
                testAdvisor.setId(advisorId);
                serviceAdvisorDAO.save(testAdvisor);
                System.out.println("Created advisor ID=" + advisorId);
            }

            if (!technicianDAO.existsById(technicianId1)) {
                var testTech1 = new Technician("Technician 1 for Order", "Diagnostics", true);
                testTech1.setId(technicianId1);
                technicianDAO.save(testTech1);
                System.out.println("Created technician 1 ID=" + technicianId1);
            }

            if (!technicianDAO.existsById(technicianId2)) {
                var testTech2 = new Technician("Technician 2 for Order", "Repair", true);
                testTech2.setId(technicianId2);
                technicianDAO.save(testTech2);
                System.out.println("Created technician 2 ID=" + technicianId2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testCreateOrder() {
        System.out.println("2. Creating order with technicians in a single transaction.");
        logger.info("Testing the JDBC method for creating a order record with technicians in a single transaction");
        try {
            if (orderDAO.existsById(orderId)) {
                System.out.println("Order ID=" + orderId + " already exists, skipping creation");
                return;
            }

            var advisor = new ServiceAdvisor("Test Advisor");
            advisor.setId(advisorId);
            var customer = new Customer("Test Customer");
            customer.setId(customerId);
            var carPlace = new CarPlace(40, true, false);
            carPlace.setId(carPlaceId);

            var testOrder = new Order(
                    advisor, null, customer, carPlace,
                    OrderStatus.PENDING, 10000,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2)
            );
            testOrder.setId(orderId);

            orderDAO.saveOrderWithTechnicians(testOrder, Arrays.asList(technicianId1, technicianId2));

            System.out.println("Order created successfully in transaction.");
            orderDAO.findById(orderId).ifPresent(System.out::println);

        } catch (Exception e) {
            System.err.println("Transaction failed: " + e.getMessage());
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for creating a order record");
    }

    private void testExistsById() {
        System.out.println("3. Testing existsById().");
        logger.info("Start of testing the JDBC method for existing order record");

        try {
            boolean exists = orderDAO.existsById(256);
            System.out.println("Executing method -> existsById(256): " + exists + " (expected false)");
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
        logger.info("Completion of testing the JDBC method for existing order record");
    }

    private void testFindById() {
        System.out.println("4. Testing findById().");
        logger.info("Start of testing the JDBC method for finding order record");

        try {
            orderDAO.findById(256).ifPresentOrElse(
                    o -> System.out.println("Found (wrong! it shouldn't have been found): " + o),
                    () -> System.out.println("Not found (expected)")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            orderDAO.findById(orderId).ifPresentOrElse(
                    o -> System.out.println("Found (expected): " + o),
                    () -> System.out.println("Not found")
            );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for finding order record");
    }

    private void testFindAll() {
        System.out.println("5. Testing retrieval of all records from the database.");
        logger.info("Start of testing the JDBC method for retrieval of all records");

        try {
            var list = orderDAO.findAll();
            System.out.println("Total records: " + list.size());
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for retrieval of all records");
    }

    private void testSpecificFindMethods() {
        System.out.println("6. Testing specific search methods.");
        logger.info("Start of testing the JDBC method for specific order search methods");

        try {
            var list = orderDAO.findByStatus(OrderStatus.PENDING);
            System.out.println("Found by status PENDING: " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findByCustomerId(customerId);
            System.out.println("Found by customer ID=" + customerId + ": " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findByCarPlaceId(carPlaceId);
            System.out.println("Found by car place ID=" + carPlaceId + ": " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for specific order search methods");
    }

    private void testSortingMethods() {
        System.out.println("7. Testing sorting methods.");
        logger.info("Start of testing the JDBC method for sorting order records");

        try {
            var list = orderDAO.findAllSortedByStartDate();
            System.out.println("Sorted by start date: " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findAllSortedByCost();
            System.out.println("Sorted by cost: " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for sorting order records");
    }

    private void testTechnicianRelations() {
        System.out.println("8. Testing technician relations.");
        logger.info("Start of testing the JDBC method for technician relations");

        try {
            orderDAO.addTechnicianToOrder(orderId, technicianId1);
            System.out.println("Added relation: order=" + orderId + ", tech=" + technicianId1);

            var technicianIds = orderDAO.findTechnicianIdsByOrderId(orderId);
            System.out.println("Technicians in order: " + technicianIds.size());
            System.out.println(technicianIds);

            orderDAO.removeTechnicianFromOrder(orderId, technicianId1);
            System.out.println("Removed relation: order=" + orderId + ", tech=" + technicianId1);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for technician relations");
    }

    private void testUpdateStatus() {
        System.out.println("9. Testing status update.");
        logger.info("Start of testing the JDBC method for status update");

        try {
            System.out.println("Before update:");
            orderDAO.findById(orderId).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Order not found")
            );

            orderDAO.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS);
            System.out.println("Status updated to IN_PROGRESS");

            var inProgressOrders = orderDAO.findByStatus(OrderStatus.IN_PROGRESS);
            System.out.println("IN_PROGRESS orders after update: " + inProgressOrders.size());

            System.out.println("After update:");
            orderDAO.findById(orderId).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Order not found")
            );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for status update");
    }

    private void testTimeShift() {
        System.out.println("10. Testing time shift.");
        logger.info("Start of testing the JDBC method for time shift");

        try {
            System.out.println("Before shift:");
            orderDAO.findById(orderId).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Order not found")
            );

            orderDAO.shiftOrderTime(orderId, 2);
            System.out.println("End date shifted by +2 hours");

            System.out.println("After shift:");
            orderDAO.findById(orderId).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Order not found")
            );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for time shift");
    }

    private void testFindOrdersByTechnician() {
        System.out.println("11. Testing find orders by technician.");
        logger.info("Start of testing the JDBC method for find orders by technician");

        try {
            var list = orderDAO.findOrdersByTechnicianId(technicianId1);
            System.out.println("Orders for technician ID=" + technicianId1 + ": " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for find orders by technician");
    }

    private void testDateRangeSearch() {
        System.out.println("12. Testing date range search.");
        logger.info("Start of testing the JDBC method for date range search");

        try {
            var startDate = LocalDateTime.now().minusDays(7);
            var endDate = LocalDateTime.now().plusDays(7);
            var list = orderDAO.findOrdersByDateRange(startDate, endDate, OrderStatus.PENDING);
            System.out.println("PENDING orders in date range (last 7 days + next 7 days): " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for date range search");
    }

    private void testRunningOrders() {
        System.out.println("13. Testing running orders.");
        logger.info("Start of testing the JDBC method for showing running orders.");

        try {
            var list = orderDAO.findCurrentlyRunningOrdersSortedByStartDate();
            System.out.println("Currently running orders sorted by start date: " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        try {
            var list = orderDAO.findCurrentlyRunningOrdersSortedByCost();
            System.out.println("Currently running orders sorted by cost: " + list.size() + " records");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for showing running orders");
    }

    private void testUpdateOrder() {
        System.out.println("14. Testing order update.");
        logger.info("Start of testing the JDBC method for update order.");

        try {

            Order orderToUpdate = new Order();
            orderToUpdate.setId(orderId);
            orderToUpdate.setStatus(OrderStatus.COMPLETED);
            orderToUpdate.setCost(15000);

            ServiceAdvisor advisor = new ServiceAdvisor();
            advisor.setId(advisorId);
            orderToUpdate.setServiceAdvisor(advisor);

            Customer customer = new Customer();
            customer.setId(customerId);
            orderToUpdate.setCustomer(customer);

            CarPlace carPlace = new CarPlace();
            carPlace.setId(carPlaceId);
            orderToUpdate.setCarPlace(carPlace);

            orderToUpdate.setCreatedDate(LocalDateTime.now());
            orderToUpdate.setStartDate(LocalDateTime.now().plusDays(1));
            orderToUpdate.setLoadingDate(LocalDateTime.now().plusDays(1));
            orderToUpdate.setEndDate(LocalDateTime.now().plusDays(2));

            orderDAO.update(orderToUpdate);
            System.out.println("Order updated: ID=" + orderId);

            orderDAO.findById(orderId).ifPresentOrElse(
                    o -> System.out.println("Record updated: " + o),
                    () -> System.out.println("Error, not found!")
            );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Completion of testing the JDBC method for update order");
    }

    private void cleanupTestData() {
        System.out.println("Clean");
        try {
            orderDAO.deleteOrderWithRelations(orderId);
            technicianDAO.delete(technicianId1);
            technicianDAO.delete(technicianId2);
            carPlaceDAO.delete(carPlaceId);
            customerDAO.delete(customerId);
            serviceAdvisorDAO.delete(advisorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}