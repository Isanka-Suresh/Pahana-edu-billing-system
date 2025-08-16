CREATE DATABASE pahana_db;
USE pahana_db;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50) DEFAULT 'user'
);

CREATE TABLE customers (
                           account_no VARCHAR(30) PRIMARY KEY,
                           name VARCHAR(150) NOT NULL,
                           address TEXT,
                           phone VARCHAR(30),
                           units_consumed INT DEFAULT 0
);

CREATE TABLE items (
                       item_id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(150),
                       price DECIMAL(10,2),
                       description TEXT
);

CREATE TABLE bills (
                       bill_id INT AUTO_INCREMENT PRIMARY KEY,
                       account_no VARCHAR(30),
                       bill_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                       units INT,
                       amount DECIMAL(10,2),
                       FOREIGN KEY (account_no) REFERENCES customers(account_no)
);
