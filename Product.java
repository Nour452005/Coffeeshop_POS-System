/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.time.LocalDateTime;

/**
 * Product Model - SIMPLIFIED
 */
public class Product {
    
    public enum Category {
        COFFEE, TEA, PASTRY, SNACK, BEVERAGE, OTHER
    }
    
    private int productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private int lowStockThreshold;
    private Category category;
    private String imageIcon;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String barcode;

    // Full constructor
    public Product(int productId, String name, String description, double price,
                   int stockQuantity, int lowStockThreshold, Category category,
                   String imageIcon, boolean isAvailable, 
                   LocalDateTime createdAt, LocalDateTime updatedAt,String barcode) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.category = category;
        this.imageIcon = imageIcon;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.barcode = barcode;
    }
    
    // Constructor for new product
    public Product(String name, String description, double price,
                   int stockQuantity, int lowStockThreshold, Category category,
                   String imageIcon) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.category = category;
        this.imageIcon = imageIcon;
        this.isAvailable = true;
    }
    
    // Simple utility methods
    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold;
    }
    
    public boolean isOutOfStock() {
        return stockQuantity == 0;
    }
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    public String getStockStatus() {
        if (isOutOfStock()) return "Out of Stock";
        if (isLowStock()) return "Low Stock";
        return "In Stock";
    }
    
    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public String getImageIcon() { return imageIcon; }
    public void setImageIcon(String imageIcon) { this.imageIcon = imageIcon; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getBarcode() { return barcode; }
public void setBarcode(String barcode) { this.barcode = barcode; }
}

