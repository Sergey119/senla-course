CREATE INDEX idx_car_place_car_lift ON car_place(car_lift);
CREATE INDEX idx_car_place_occupied ON car_place(is_occupied);
CREATE INDEX idx_technician_specialization ON technician(specialization);
CREATE INDEX idx_technician_available ON technician(is_available);
CREATE INDEX idx_order_status ON "order"(status);
CREATE INDEX idx_order_customer_id ON "order"(customer_id);
CREATE INDEX idx_order_created_date ON "order"(created_date);