DELETE FROM order_technician;
DELETE FROM "order";
DELETE FROM customer;
DELETE FROM technician;
DELETE FROM car_place;
DELETE FROM service_advisor;

INSERT INTO service_advisor (id, name)
VALUES
    (1, 'James Parker'),
    (2, 'Sarah Johnson'),
    (3, 'Michael Chen');

INSERT INTO customer (id, name)
VALUES
    (1, 'Raymond Anderson'),
    (2, 'Emma Wilson'),
    (3, 'David Miller'),
    (4, 'Sophia Garcia');

INSERT INTO technician (id, name, specialization, is_available)
VALUES
    (1, 'John Smith', 'Auto electrician', true),
    (2, 'Maria Rodriguez', 'Technical engineer', true),
    (3, 'Robert Brown', 'Motor mechanic', false),
    (4, 'David Anderson', 'Diagnostic wizard', true),
    (5, 'Tom Davis', 'Technical engineer', true);

INSERT INTO car_place (id, square, car_lift, is_occupied)
VALUES
    (1, 30, true, false),
    (2, 25, false, false),
    (3, 35, true, true),
    (4, 28, false, false),
    (5, 40, true, false);

INSERT INTO "order" (id, service_advisor_id, customer_id, car_place_id, status, cost, 
                     created_date, start_date, loading_date, end_date)
VALUES
    (1, 1, 1, 1, 'COMPLETED', 5000,
'2024-01-10 09:00:00', '2024-01-10 10:00:00', '2024-01-10 10:10:00', '2024-01-10 14:00:00'),
    (2, 2, 2, 3, 'IN_PROGRESS', 7500,
'2024-01-12 08:30:00', '2024-01-12 09:00:00', '2024-01-12 09:10:00', '2024-01-12 16:00:00'),
    (3, 1, 3, 2, 'PENDING', 3000,
'2024-01-15 10:00:00', '2024-01-16 11:00:00', '2024-01-16 11:10:00', '2024-01-16 13:00:00'),
    (4, 3, 4, 4, 'CANCELLED', 0,
'2024-01-05 14:00:00', '2024-01-06 15:00:00', '2024-01-06 15:10:00', '2024-01-06 17:00:00'),
    (5, 2, 1, 5, 'IN_PROGRESS', 9000,
'2024-01-14 13:00:00', '2024-01-14 14:00:00', '2024-01-14 14:10:00', '2024-01-15 18:00:00');

INSERT INTO order_technician (order_id, technician_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4),
    (3, 1),
    (4, 5),
    (5, 2),
    (5, 4),
    (5, 5);