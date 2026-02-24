ALTER TABLE "order" ADD CONSTRAINT fk_order_service_advisor
    FOREIGN KEY (service_advisor_id) REFERENCES service_advisor(id);

ALTER TABLE "order" ADD CONSTRAINT fk_order_customer
    FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE "order" ADD CONSTRAINT fk_order_car_place
    FOREIGN KEY (car_place_id) REFERENCES car_place(id);

ALTER TABLE order_technician ADD CONSTRAINT fk_order_technician_order
    FOREIGN KEY (order_id) REFERENCES "order"(id) ON DELETE CASCADE;

ALTER TABLE order_technician ADD CONSTRAINT fk_order_technician_technician
    FOREIGN KEY (technician_id) REFERENCES technician(id) ON DELETE CASCADE;