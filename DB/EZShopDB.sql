#drop database ezshopdb;
create database ezshopdb;

use ezshopdb;

create table user(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, username VARCHAR(30) NOT NULL, password VARCHAR(30) NOT NULL, role VARCHAR(13) NOT NULL, CONSTRAINT UNIQUE(username));

create table product_type(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, barcode VARCHAR(14) NOT NULL UNIQUE, description VARCHAR(200), pricePerUnit double NOT NULL, quantity Int UNSIGNED, notes VARCHAR(200), aisleID Int UNSIGNED, rackID VARCHAR(1), levelID Int UNSIGNED);

create table balance_operation(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, type VARCHAR(6), amount double NOT NULL, date_time datetime NOT NULL);

create table product_order(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, pricePerUnity double NOT NULL, quantity Int UNSIGNED NOT NULL, status VARCHAR(9) NOT NULL, balance_operation Int UNSIGNED, product_type Int UNSIGNED NOT NULL, FOREIGN KEY(balance_operation) REFERENCES balance_operation(id), FOREIGN KEY(product_type) REFERENCES product_type(id));

create table sale_transaction(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, cost double, payment_type Int UNSIGNED, discount_rate double, date_time datetime, balance_operation Int UNSIGNED, FOREIGN KEY(balance_operation) REFERENCES balance_operation(id));

create table return_transaction(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, returned_value double, sale_transaction Int UNSIGNED NOT NULL, balance_operation Int UNSIGNED, FOREIGN KEY(sale_transaction) REFERENCES sale_transaction(id), FOREIGN KEY(balance_operation) REFERENCES balance_operation(id));

create table product_entry(rfid VARCHAR(12) PRIMARY KEY, product_type Int UNSIGNED NOT NULL, available BIT(1), FOREIGN KEY(product_type) REFERENCES product_type(id));

create table return_product(return_transaction Int UNSIGNED, product_type Int UNSIGNED, CONSTRAINT bond PRIMARY KEY(return_transaction, product_type), quantity Int UNSIGNED NOT NULL, FOREIGN KEY(return_transaction) REFERENCES return_transaction(id), FOREIGN KEY(product_type) REFERENCES product_type(id));

create table return_product_rfid(return_transaction Int UNSIGNED, product_entry VARCHAR(12), CONSTRAINT bond PRIMARY KEY(return_transaction, product_entry), FOREIGN KEY(return_transaction) REFERENCES return_transaction(id), FOREIGN KEY(product_entry) REFERENCES product_entry(rfid));

create table product_in_transaction(sale_transaction Int UNSIGNED, product_type Int UNSIGNED, CONSTRAINT bond PRIMARY KEY(sale_transaction, product_type), amount Int UNSIGNED NOT NULL, discount_rate double, FOREIGN KEY(sale_transaction) REFERENCES sale_transaction(id), FOREIGN KEY(product_type) REFERENCES product_type(id));

create table product_in_transaction_rfid(sale_transaction Int UNSIGNED, product_entry VARCHAR(12), CONSTRAINT bond PRIMARY KEY(sale_transaction, product_entry), FOREIGN KEY(sale_transaction) REFERENCES sale_transaction(id), FOREIGN KEY(product_entry) REFERENCES product_entry(rfid));

create table loyalty_card(serial_number VARCHAR(10) PRIMARY KEY, points Int UNSIGNED NOT NULL);

create table customer(id Int UNSIGNED AUTO_INCREMENT PRIMARY KEY, name VARCHAR(80) NOT NULL, loyalty_card VARCHAR(10), FOREIGN KEY(loyalty_card) REFERENCES loyalty_card(serial_number), CONSTRAINT UNIQUE(name));
