-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS orderdb;

USE orderdb;

-- Table User
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Table Category
CREATE TABLE IF NOT EXISTS category (
    idCategory INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Table Product
CREATE TABLE IF NOT EXISTS product (
    idProduct INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    idCategory INT,
    FOREIGN KEY (idCategory) REFERENCES category(idCategory) ON DELETE SET NULL
);

-- Table Order
CREATE TABLE IF NOT EXISTS `order` (
    idOrder INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DOUBLE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Table Order_Product
CREATE TABLE IF NOT EXISTS order_product (
    order_id INT,
    product_id INT,
    PRIMARY KEY(order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES `order`(idOrder) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(idProduct) ON DELETE CASCADE
);
