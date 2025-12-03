/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager - Singleton pattern for database connection
 * SIMPLIFIED VERSION - No auto-table creation
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    private Connection connection;
    
    // Database configuration - CHANGE THESE IF NEEDED
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coffee_pos_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "20102023#"; // Your MySQL password here
    
    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Database connected!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            System.err.println("  Make sure MySQL is running and database exists!");
        }
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get connection: " + e.getMessage());
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}