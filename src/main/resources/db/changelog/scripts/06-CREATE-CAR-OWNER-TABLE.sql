CREATE TABLE IF NOT EXISTS cars.car_owner(
    CAR_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    PRIMARY KEY (CAR_ID, USER_ID),
    FOREIGN KEY (CAR_ID) REFERENCES CAR (ID),
    FOREIGN KEY (USER_ID) REFERENCES APP_USER (ID)
);