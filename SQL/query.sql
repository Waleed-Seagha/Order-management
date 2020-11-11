CREATE DATABASE order_management;
USE order_management;

DROP TABLE IF EXISTS clients;
CREATE TABLE clients (
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL DEFAULT '',
address VARCHAR(45) NOT NULL DEFAULT ''
) ENGINE = InnoDB;

DROP TABLE IF EXISTS products;
CREATE TABLE products (
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL DEFAULT '',
quantity INT,
price DOUBLE
) ENGINE = InnoDB;

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
id INT PRIMARY KEY AUTO_INCREMENT,
client_id INT,
product_id INT,
quantity INT,
FOREIGN KEY(client_id) REFERENCES clients (id),
FOREIGN KEY(product_id) REFERENCES products (id)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS order_details;
CREATE TABLE order_details (
order_id INT,
price DOUBLE,
`date` VARCHAR(45) NOT NULL DEFAULT '',
FOREIGN KEY(order_id) REFERENCES orders (id)
) ENGINE = InnoDB;

SELECT * FROM clients;
SELECT * FROM products;
SELECT * FROM orders;
SELECT * FROM order_details;

SELECT price, `date` FROM order_details WHERE order_id = 2;


INSERT INTO Orders VALUES (1, 4, 3, 3);