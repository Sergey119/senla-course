ALTER TABLE "order" ADD CONSTRAINT chk_order_status
    CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'));

ALTER TABLE "order" ADD CONSTRAINT chk_order_cost
    CHECK (cost > 0);

ALTER TABLE car_place ADD CONSTRAINT chk_car_place_square
    CHECK (square > 0);

ALTER TABLE "order" ADD CONSTRAINT chk_order_dates
    CHECK (end_date > start_date);