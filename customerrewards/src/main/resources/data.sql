DROP TABLE IF EXISTS customer;
CREATE TABLE customer
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS customer_transaction;
CREATE TABLE customer_transaction
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id            INT,
    payment_type           VARCHAR(10)    NOT NULL,
    last4digits_of_payment VARCHAR(10)    NOT NULL,
    transaction_amount     DECIMAL(20, 2) NOT NULL,
    transaction_time       TIMESTAMP      NOT NULL,
    foreign key (customer_id) references customer (id)

);

INSERT INTO customer(customer_name)
values ('Bob'),
       ('Joe'),
       ('will');

INSERT INTO customer_transaction(customer_id, payment_type, last4digits_of_payment,
                                 transaction_amount, transaction_time)
values (1, 'ACH', 'XXX3456', 120, NOW()),
       (1, 'ACH', 'XXX3456', 80, NOW() - 32),
       (1, 'ACH', 'XXX3456', 49, NOW() - 60),
       (1, 'ACH', 'XXX3456', 101, NOW() - 62),
       (1, 'ACH', 'XXX3456', 80, NOW() - 91),
       (1, 'ACH', 'XXX3456', 75, NOW() - 125),
       (2, 'DISCOVER', 'XXX7246', 120, NOW()),
       (2, 'AMEX', 'XXX5478', 240, NOW() - 32),
       (2, 'VISA', 'XXX5869', 40, NOW() - 60),
       (2, 'VISA', 'XXX5869', 80, NOW() - 62),
       (2, 'VISA', 'XXX5869', 60, NOW() - 91),
       (2, 'VISA', 'XXX5869', 115, NOW() - 125),
       (3, 'MASTER', 'XXX4523', 98, NOW()),
       (3, 'ACH', 'XXX3642', 51, NOW() - 32),
       (3, 'VISA', 'XXX9874', 49, NOW() - 60),
       (3, 'VISA', 'XXX9874', 75, NOW() - 62),
       (3, 'VISA', 'XXX9874', 110, NOW() - 91);





