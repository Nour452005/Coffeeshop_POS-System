/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.util.List;

/**
 * SaleService - Business logic for sales
 */
public class SaleService {
    
    private SaleDAO saleDAO;
    private ProductService productService;
    
    public SaleService() {
        this.saleDAO = new SaleDAO();
        this.productService = new ProductService();
    }
    
    // Complete a sale and update inventory
  public boolean completeSale(Sale sale) {
    for (SaleItem item : sale.getItems()) {

        Product product = productService.getProductById(item.getProductId());  // NEW

        if (product == null) {
            System.err.println("Product not found: " + item.getProductId());
            return false;
        }

        int currentStock = product.getStockQuantity();   // ALWAYS fresh
        int newStock = currentStock - item.getQuantity();
        
        if (newStock < 0) {
            System.err.println("Insufficient stock for: " + product.getName());
            return false;
        }

        productService.updateStock(product.getProductId(), newStock);
    }

    return saleDAO.saveSale(sale);
}


    
    public List<Sale> getAllSales() {
        return saleDAO.getAllSales();
    }
    
    public double getTodayTotal() {
        return saleDAO.getTodayTotal();
    }
    
    public int getTodaySalesCount() {
        return saleDAO.getTodaySalesCount();
    }
    public List<SaleItem> getSaleItems(int saleId) {
    return saleDAO.getSaleItems(saleId);
}

}
