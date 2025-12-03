/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

/**
 *
 * @author USER
 */
public class SaleItem {
    private int productId;
    private String productName;
    private int quantity;
    private double price;

    public SaleItem(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public double getSubtotal() {
        return price * quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }
}
