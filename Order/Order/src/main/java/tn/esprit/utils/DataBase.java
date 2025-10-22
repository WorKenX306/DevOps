package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    // Variables d'environnement avec valeurs par défaut
    private static final String DB_HOST = System.getenv("DB_HOST") != null ? 
        System.getenv("DB_HOST") : "localhost";
    private static final String DB_PORT = System.getenv("DB_PORT") != null ? 
        System.getenv("DB_PORT") : "3306";
    private static final String DB_NAME = System.getenv("DB_NAME") != null ? 
        System.getenv("DB_NAME") : "orderdb";
    private static final String DB_USER = System.getenv("DB_USER") != null ? 
        System.getenv("DB_USER") : "root";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null ? 
        System.getenv("DB_PASSWORD") : "";
    
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";

    /**
     * Retourne une connexion à la base de données.
     * Crée la base si elle n'existe pas.
     */
    public static Connection getConnection() throws SQLException {
        // Connexion au serveur MySQL
        Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);

        // Crée la base si elle n'existe pas
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        // Connexion à la base créée
        return DriverManager.getConnection(URL + DB_NAME, DB_USER, DB_PASSWORD);
    }

    /**
     * Crée toutes les tables si elles n'existent pas.
     */
    public static void initTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Table User
            String userSql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL" +
                    ");";
            stmt.executeUpdate(userSql);

            // Table Category
            String categorySql = "CREATE TABLE IF NOT EXISTS category (" +
                    "idCategory INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL" +
                    ");";
            stmt.executeUpdate(categorySql);

            // Table Product
            String productSql = "CREATE TABLE IF NOT EXISTS product (" +
                    "idProduct INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "idCategory INT," +
                    "FOREIGN KEY (idCategory) REFERENCES category(idCategory) ON DELETE SET NULL" +
                    ");";
            stmt.executeUpdate(productSql);

            // Table Order
            String orderSql = "CREATE TABLE IF NOT EXISTS `order` (" +
                    "idOrder INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "total DOUBLE," +
                    "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE" +
                    ");";
            stmt.executeUpdate(orderSql);
            
            String orderProductSql = "CREATE TABLE IF NOT EXISTS order_product (" +
                    "order_id INT," +
                    "product_id INT," +
                    "PRIMARY KEY(order_id, product_id)," +
                    "FOREIGN KEY (order_id) REFERENCES `order`(idOrder) ON DELETE CASCADE," +
                    "FOREIGN KEY (product_id) REFERENCES product(idProduct) ON DELETE CASCADE" +
                    ");";
            stmt.executeUpdate(orderProductSql);

            System.out.println("Base et toutes les tables créées ou déjà existantes.");
            System.out.println("Connexion à: " + URL + DB_NAME);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
