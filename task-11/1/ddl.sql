CREATE TABLE service_advisor (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE customer (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE technician (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE car_place (
    id INT PRIMARY KEY,
    square INT NOT NULL,
    car_lift BOOLEAN DEFAULT FALSE,
    is_occupied BOOLEAN DEFAULT FALSE
);

CREATE TABLE "order" (
    id INT PRIMARY KEY,
    service_advisor_id INT NOT NULL REFERENCES service_advisor(id),
    customer_id INT NOT NULL REFERENCES customer(id),
    car_place_id INT NOT NULL REFERENCES car_place(id),
    status VARCHAR(50) NOT NULL,
    cost INT DEFAULT 0,
    created_date TIMESTAMP,
    start_date TIMESTAMP,
    loading_date TIMESTAMP,
    end_date TIMESTAMP
);

CREATE TABLE order_technician (
    order_id INT NOT NULL REFERENCES "order"(id),
    technician_id INT NOT NULL REFERENCES technician(id)
);