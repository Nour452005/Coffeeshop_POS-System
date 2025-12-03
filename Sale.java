/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale - Represents a complete sale transaction
 */
public class Sale {
    
    private int saleId;
    private int userId;
    private String cashierName;
    private double totalAmount;
    private LocalDateTime saleDate;
    private List<SaleItem> items;
    
    public Sale(int userId, String cashierName) {
        this.userId = userId;
        this.cashierName = cashierName;
        this.items = new ArrayList<>();
        this.saleDate = LocalDateTime.now();
        this.totalAmount = 0;
    }
    
    public Sale(int saleId, int userId, String cashierName, double totalAmount, LocalDateTime saleDate) {
        this.saleId = saleId;
        this.userId = userId;
        this.cashierName = cashierName;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.items = new ArrayList<>();
    }
    
    public void addItem(SaleItem item) {
        items.add(item);
        calculateTotal();
    }
    
    public void removeItem(SaleItem item) {
        items.remove(item);
        calculateTotal();
    }
    
    private void calculateTotal() {
        totalAmount = items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return saleDate.format(formatter);
    }
    
    public String getFormattedTotal() {
        return String.format("$%.2f", totalAmount);
    }
    
    // Getters and Setters
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    
    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { 
        this.items = items;
        calculateTotal();
    }
}
