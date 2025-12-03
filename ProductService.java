/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.util.List;

/**
 * ProductService - SIMPLIFIED business logic
 */
public class ProductService {
    
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }
    
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    public List<Product> getAvailableProducts() {
        return productDAO.getAvailableProducts();
    }
    
    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }
    
    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }
    
    public boolean updateStock(int productId, int newQuantity) {
        return productDAO.updateStock(productId, newQuantity);
    }
    
    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }
    public Product getProductById(int productId) {
    return productDAO.getProductById(productId);
}
    public Product getProductByBarcode(String barcode) {
    return productDAO.getProductByBarcode(barcode);
}


}