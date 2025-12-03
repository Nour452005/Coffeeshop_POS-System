/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * InventoryManagementView - Stock management interface
 */
public class InventoryManagementView {
    
    private VBox rootPane;
    private ProductService productService;
    private TableView<Product> inventoryTable;
    private ObservableList<Product> productList;
    
    public InventoryManagementView() {
        this.productService = new ProductService();
        this.productList = FXCollections.observableArrayList();
        buildUI();
        loadInventory();
    }
    
    private void buildUI() {
        rootPane = new VBox(20);
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.TOP_CENTER);
        
        Label title = new Label("📋 Inventory Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #5D4E37;");
        
        HBox actionBar = createActionBar();
        inventoryTable = createInventoryTable();
        HBox buttonBar = createButtonBar();
        
        rootPane.getChildren().addAll(title, actionBar, inventoryTable, buttonBar);
    }
    
    private HBox createActionBar() {
        HBox bar = new HBox(15);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(10));
        bar.setStyle("-fx-background-color: #E8DCC8; -fx-background-radius: 10; -fx-padding: 15;");
        
        Button allBtn = new Button("📦 All Products");
        Button lowStockBtn = new Button("⚠️ Low Stock");
        Button outOfStockBtn = new Button("❌ Out of Stock");
        
        String btnStyle = "-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-pref-height: 35;";
        allBtn.setStyle(btnStyle);
        lowStockBtn.setStyle(btnStyle);
        outOfStockBtn.setStyle(btnStyle);
        
        allBtn.setOnAction(e -> loadInventory());
        lowStockBtn.setOnAction(e -> showLowStock());
        outOfStockBtn.setOnAction(e -> showOutOfStock());
        
        bar.getChildren().addAll(allBtn, lowStockBtn, outOfStockBtn);
        return bar;
    }
    
    private TableView<Product> createInventoryTable() {
        TableView<Product> table = new TableView<>();
        table.setItems(productList);
        table.setPrefHeight(450);
        
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(50);
        
        TableColumn<Product, String> iconCol = new TableColumn<>("Icon");
        iconCol.setCellValueFactory(new PropertyValueFactory<>("imageIcon"));
        iconCol.setPrefWidth(60);
        iconCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 24px;");
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        
        TableColumn<Product, Product.Category> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(120);
        
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Current Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        stockCol.setPrefWidth(120);
        stockCol.setCellFactory(col -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                if (empty || stock == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(stock.toString());
                    Product product = getTableView().getItems().get(getIndex());
                    if (product.isOutOfStock()) {
                        setStyle("-fx-text-fill: #C87941; -fx-font-weight: bold;");
                    } else if (product.isLowStock()) {
                        setStyle("-fx-text-fill: #D4956F; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #6B8E23; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        TableColumn<Product, Integer> thresholdCol = new TableColumn<>("Alert Level");
        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("lowStockThreshold"));
        thresholdCol.setPrefWidth(100);
        
        TableColumn<Product, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(120);
        statusCol.setCellFactory(col -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Product product = getTableView().getItems().get(getIndex());
                    setText(product.getStockStatus());
                    if (product.isOutOfStock()) {
                        setStyle("-fx-text-fill: #C87941; -fx-font-weight: bold;");
                    } else if (product.isLowStock()) {
                        setStyle("-fx-text-fill: #D4956F; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #6B8E23; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        table.getColumns().addAll(idCol, iconCol, nameCol, catCol, stockCol, thresholdCol, statusCol);
        return table;
    }
    
    private HBox createButtonBar() {
        HBox bar = new HBox(15);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(10));
        
        Button restockBtn = new Button("📦 Restock");
        Button adjustBtn = new Button("✏️ Adjust Stock");
        Button refreshBtn = new Button("🔄 Refresh");
        
        String btnStyle = "-fx-background-color: #6B8E23; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-pref-width: 140; -fx-pref-height: 40;";
        restockBtn.setStyle(btnStyle);
        adjustBtn.setStyle(btnStyle);
        refreshBtn.setStyle(btnStyle);
        
        restockBtn.setOnAction(e -> handleRestock());
        adjustBtn.setOnAction(e -> handleAdjust());
        refreshBtn.setOnAction(e -> loadInventory());
        
        bar.getChildren().addAll(restockBtn, adjustBtn, refreshBtn);
        return bar;
    }
    
    private void loadInventory() {
        productList.clear();
        productList.addAll(productService.getAllProducts());
    }
    
    private void showLowStock() {
        productList.clear();
        productList.addAll(productService.getLowStockProducts());
    }
    
    private void showOutOfStock() {
        productList.clear();
        for (Product p : productService.getAllProducts()) {
            if (p.isOutOfStock()) {
                productList.add(p);
            }
        }
    }
    
    private void handleRestock() {
        Product selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a product to restock");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog("50");
        dialog.setTitle("Restock Product");
        dialog.setHeaderText("Restock: " + selected.getName());
        dialog.setContentText("Enter quantity to add:");
        
        dialog.showAndWait().ifPresent(input -> {
            try {
                int quantity = Integer.parseInt(input);
                if (quantity > 0) {
                    int newStock = selected.getStockQuantity() + quantity;
                    if (productService.updateStock(selected.getProductId(), newStock)) {
                        showAlert("Success", "Stock updated!\nOld: " + selected.getStockQuantity() + " → New: " + newStock);
                        loadInventory();
                    } else {
                        showAlert("Error", "Failed to update stock");
                    }
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number");
            }
        });
    }
    
    private void handleAdjust() {
        Product selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a product");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getStockQuantity()));
        dialog.setTitle("Adjust Stock");
        dialog.setHeaderText("Adjust: " + selected.getName());
        dialog.setContentText("Enter new stock quantity:");
        
        dialog.showAndWait().ifPresent(input -> {
            try {
                int newStock = Integer.parseInt(input);
                if (newStock >= 0) {
                    if (productService.updateStock(selected.getProductId(), newStock)) {
                        showAlert("Success", "Stock adjusted to: " + newStock);
                        loadInventory();
                    }
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number");
            }
        });
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return rootPane;
    }
}