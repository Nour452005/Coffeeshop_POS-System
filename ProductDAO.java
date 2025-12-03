/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO - supports barcode field
 */
public class ProductDAO {
    
    private DatabaseManager dbManager;
    
    public ProductDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // CREATE
    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (name, description, price, stock_quantity, low_stock_threshold, category, image_icon, is_available, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getLowStockThreshold());
            pstmt.setString(6, product.getCategory().name());
            pstmt.setString(7, product.getImageIcon());
            pstmt.setBoolean(8, product.isAvailable());
            pstmt.setString(9, product.getBarcode());  // ADDED
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
        return false;
    }
    
    // READ - Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products ORDER BY name";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                products.add(createProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        return products;
    }
    
    // READ - Get available products only
    public List<Product> getAvailableProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE is_available = TRUE ORDER BY name";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                products.add(createProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available products: " + e.getMessage());
        }
        return products;
    }

    // READ - find product by barcode
    public Product getProductByBarcode(String barcode) {
        String query = "SELECT * FROM products WHERE barcode = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createProductFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product by barcode: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Get low stock products
    public List<Product> getLowStockProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE stock_quantity <= low_stock_threshold ORDER BY stock_quantity";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                products.add(createProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting low stock products: " + e.getMessage());
        }
        return products;
    }
    
    // UPDATE
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, low_stock_threshold = ?, category = ?, image_icon = ?, is_available = ?, barcode = ? WHERE product_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getLowStockThreshold());
            pstmt.setString(6, product.getCategory().name());
            pstmt.setString(7, product.getImageIcon());
            pstmt.setBoolean(8, product.isAvailable());
            pstmt.setString(9, product.getBarcode());
            pstmt.setInt(10, product.getProductId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
        return false;
    }
    
    // UPDATE - Stock only
    public boolean updateStock(int productId, int newQuantity) {
        String query = "UPDATE products SET stock_quantity = ? WHERE product_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
        return false;
    }
    
    // DELETE
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
        return false;
    }
    
    // Helper method to create Product from ResultSet
    private Product createProductFromResultSet(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getInt("stock_quantity"),
            rs.getInt("low_stock_threshold"),
            Product.Category.valueOf(rs.getString("category")),
            rs.getString("image_icon"),
            rs.getBoolean("is_available"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime(),
            rs.getString("barcode")   // ADDED
        );
    }

    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createProductFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
        }
        return null;
    }
}
