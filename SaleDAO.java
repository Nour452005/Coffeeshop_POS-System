/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SaleDAO - Database operations for sales
 */
public class SaleDAO {
    
    private DatabaseManager dbManager;
    
    public SaleDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // Save complete sale with items
public boolean saveSale(Sale sale) {
    String saleQuery = "INSERT INTO sales (user_id, cashier_name, total_amount) VALUES (?, ?, ?)";
    String itemQuery = "INSERT INTO sale_items (sale_id, product_id, product_name, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
    String stockQuery = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";

    Connection conn = null;
    try {
        conn = dbManager.getConnection();
        conn.setAutoCommit(false);

        // Insert sale
        PreparedStatement saleStmt = conn.prepareStatement(saleQuery, Statement.RETURN_GENERATED_KEYS);
        saleStmt.setInt(1, sale.getUserId());
        saleStmt.setString(2, sale.getCashierName());
        saleStmt.setDouble(3, sale.getTotalAmount());
        saleStmt.executeUpdate();

        ResultSet rs = saleStmt.getGeneratedKeys();
        rs.next();
        int saleId = rs.getInt(1);
        sale.setSaleId(saleId);

        // Insert items + reduce stock
        PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
        PreparedStatement stockStmt = conn.prepareStatement(stockQuery);

        for (SaleItem item : sale.getItems()) {

            // insert sale item
            itemStmt.setInt(1, saleId);
            itemStmt.setInt(2, item.getProductId());
            itemStmt.setString(3, item.getProductName());
            itemStmt.setInt(4, item.getQuantity());
            itemStmt.setDouble(5, item.getPrice());
            itemStmt.setDouble(6, item.getSubtotal());
            itemStmt.addBatch();

            // 🔥 reduce stock
            stockStmt.setInt(1, item.getQuantity());
            stockStmt.setInt(2, item.getProductId());
            stockStmt.addBatch();
        }

        itemStmt.executeBatch();
        stockStmt.executeBatch();

        conn.commit();
        return true;

    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ignore) {}
        System.err.println("❗ Error saving sale: " + e.getMessage());
        return false;

    } finally {
        try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignore) {}
    }
}

    // Get all sales
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM sales ORDER BY sale_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("sale_id"),
                    rs.getInt("user_id"),
                    rs.getString("cashier_name"),
                    rs.getDouble("total_amount"),
                    rs.getTimestamp("sale_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales: " + e.getMessage());
        }
        return sales;
    }
    
    // Get today's sales total
    public double getTodayTotal() {
        String query = "SELECT SUM(total_amount) FROM sales WHERE DATE(sale_date) = CURDATE()";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting today's total: " + e.getMessage());
        }
        return 0;
    }
    
    // Get sales count for today
    public int getTodaySalesCount() {
        String query = "SELECT COUNT(*) FROM sales WHERE DATE(sale_date) = CURDATE()";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales count: " + e.getMessage());
        }
        return 0;
    }
    public List<SaleItem> getSaleItems(int saleId) {
    List<SaleItem> items = new ArrayList<>();
    String query = "SELECT * FROM sale_items WHERE sale_id = ?";

    try (Connection conn = dbManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, saleId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            items.add(new SaleItem(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getDouble("price")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error getting sale items: " + e.getMessage());
    }
    return items;
}

}