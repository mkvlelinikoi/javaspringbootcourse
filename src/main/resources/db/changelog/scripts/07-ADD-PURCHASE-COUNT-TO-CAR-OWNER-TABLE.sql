ALTER TABLE cars.car_owner
ADD COLUMN purchase_count INT DEFAULT 1;
--fixing so that user can buy the same car multiple times