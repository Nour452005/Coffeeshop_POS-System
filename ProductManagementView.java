/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.util.List;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
public class ProductManagementView {
    
    private VBox rootPane;
    private ProductService productService;
    private TableView<Product> productTable;
    private ObservableList<Product> productList;
    private TextField searchField;
    private ComboBox<String> categoryFilter;
    
    public ProductManagementView() {
        this.productService = new ProductService();
        this.productList = FXCollections.observableArrayList();
        buildUI();
        loadProducts();
    }
    private void buildUI() {
        rootPane = new VBox(20);
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.TOP_CENTER);
        Label titleLabel = new Label("📦 Product Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #5D4E37;");
        HBox actionBar = createActionBar();
        productTable = createProductTable();
        HBox buttonBar = createButtonBar();
        
        rootPane.getChildren().addAll(titleLabel, actionBar, productTable, buttonBar);
    }
    private HBox createActionBar() {
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(10));
        actionBar.setStyle(
            "-fx-background-color: #E8DCC8;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;"
        );
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("🔍 Search products...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);
        searchField.getStyleClass().add("text-field");
        
        // Search on typing
        searchField.textProperty().addListener((obs, old, newVal) -> {
            filterProducts();
        });
        
        // Category filter
        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().add("All Categories");
        for (Product.Category cat : Product.Category.values()) {
            categoryFilter.getItems().add(cat.toString());
        }
        categoryFilter.setValue("All Categories");
        categoryFilter.setPrefWidth(150);
        categoryFilter.setPrefHeight(35);
        categoryFilter.getStyleClass().add("combo-box");
        
        categoryFilter.setOnAction(e -> filterProducts());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Add Product button
        Button addButton = new Button("➕ Add Product");
        addButton.setPrefHeight(35);
        addButton.setStyle(
            "-fx-background-color: #6B8E23;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;"
        );
        
        addButton.setOnAction(e -> showAddProductDialog());
        
        addButton.setOnMouseEntered(e -> {
            addButton.setStyle(
                "-fx-background-color: #7BA428;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10 20;" +
                "-fx-cursor: hand;"
            );
        });
        
        addButton.setOnMouseExited(e -> {
            addButton.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10 20;" +
                "-fx-cursor: hand;"
            );
        });
        
        actionBar.getChildren().addAll(searchField, categoryFilter, spacer, addButton);
        
        return actionBar;
    }
    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();
        table.setItems(productList);
        table.setPrefHeight(400);
        table.getStyleClass().add("table-view");
        
        // ID Column
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(50);
        
        // Icon Column
        TableColumn<Product, String> iconCol = new TableColumn<>("Icon");
        iconCol.setCellValueFactory(new PropertyValueFactory<>("imageIcon"));
        iconCol.setPrefWidth(60);
        iconCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 24px;");
        
        // Name Column
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);
        
        // Description Column
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);
        
        // Category Column
        TableColumn<Product, Product.Category> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        catCol.setPrefWidth(100);
        
        // Price Column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);
        
        // Custom cell factory for price formatting
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #8B6F47;");
                }
            }
        });
        
        // Stock Column
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        stockCol.setPrefWidth(80);
        
        // Custom cell factory for stock with color coding
        stockCol.setCellFactory(col -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                if (empty || stock == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(stock.toString());
                    
                    // Get product from row
                    Product product = getTableView().getItems().get(getIndex());
                    
                    if (product != null) {
                        if (product.isOutOfStock()) {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: #C87941;");
                        } else if (product.isLowStock()) {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: #D4956F;");
                        } else {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: #6B8E23;");
                        }
                    }
                }
            }
        });
        
        // Status Column
        TableColumn<Product, Boolean> statusCol = new TableColumn<>("Available");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        statusCol.setPrefWidth(90);
        
        // Custom cell factory for checkbox
        statusCol.setCellFactory(col -> new TableCell<Product, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            
            {
                checkBox.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    product.setAvailable(checkBox.isSelected());
                    productService.updateProduct(product);
                });
            }
            
            @Override
            protected void updateItem(Boolean available, boolean empty) {
                super.updateItem(available, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(available);
                    setGraphic(checkBox);
                }
            }
        });
        
        table.getColumns().addAll(idCol, iconCol, nameCol, descCol, catCol, priceCol, stockCol, statusCol);
        
        // Row selection highlight
        table.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #F5EDE0;");
                }
            });
            row.setOnMouseExited(e -> {
                row.setStyle("");
            });
            return row;
        });
        
        return table;
    }
    private HBox createButtonBar() {
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));
        
        Button editButton = createStyledButton("✏️ Edit", "#8B6F47");
        Button deleteButton = createStyledButton("🗑️ Delete", "#C87941");
        Button refreshButton = createStyledButton("🔄 Refresh", "#B8956A");
        
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> loadProducts());
        
        buttonBar.getChildren().addAll(editButton, deleteButton, refreshButton);
        
        return buttonBar;
    }
    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefSize(120, 40);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        final String originalColor = color;
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                "-fx-background-color: derive(" + originalColor + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            );
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                "-fx-background-color: " + originalColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            );
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        return btn;
    }
    private void loadProducts() {
        productList.clear();
        productList.addAll(productService.getAllProducts());
        
        // Animate table refresh
        FadeTransition fade = new FadeTransition(Duration.millis(300), productTable);
        fade.setFromValue(0.5);
        fade.setToValue(1.0);
        fade.play();
    }
    private void filterProducts() {
        String searchTerm = searchField.getText().toLowerCase();
        String category = categoryFilter.getValue();
        
        productList.clear();
        
        List<Product> allProducts = productService.getAllProducts();
        
        for (Product product : allProducts) {
            boolean matchesSearch = searchTerm.isEmpty() ||
                                   product.getName().toLowerCase().contains(searchTerm) ||
                                   product.getDescription().toLowerCase().contains(searchTerm);
            
            boolean matchesCategory = category.equals("All Categories") ||
                                     product.getCategory().toString().equals(category);
            
            if (matchesSearch && matchesCategory) {
                productList.add(product);
            }
        }
    }
    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter product details");
        
        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Product name");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        
        TextField priceField = new TextField();
        priceField.setPromptText("0.00");
        
        TextField stockField = new TextField();
        stockField.setPromptText("0");
        
        TextField thresholdField = new TextField();
        thresholdField.setPromptText("10");
        
        ComboBox<Product.Category> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(Product.Category.values());
        categoryCombo.setValue(Product.Category.COFFEE);
        
        TextField iconField = new TextField();
        iconField.setPromptText("☕");
        iconField.setMaxWidth(60);
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        
        grid.add(new Label("Price ($):"), 0, 2);
        grid.add(priceField, 1, 2);
        
        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);
        
        grid.add(new Label("Low Stock Alert:"), 0, 4);
        grid.add(thresholdField, 1, 4);
        
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryCombo, 1, 5);
        
        grid.add(new Label("Icon:"), 0, 6);
        grid.add(iconField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText();
                    String description = descArea.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());
                    int threshold = Integer.parseInt(thresholdField.getText());
                    Product.Category category = categoryCombo.getValue();
                    String icon = iconField.getText();
                    
                    return new Product(name, description, price, stock, threshold, category, icon);
                    
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers for price and stock");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(product -> {
            if (product != null) {
                if (productService.addProduct(product)) {
                    showAlert("Success", "Product added successfully!");
                    loadProducts();
                } else {
                    showAlert("Error", "Failed to add product. Name may already exist.");
                }
            }
        });
    }
    private void handleEdit() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("No Selection", "Please select a product to edit");
            return;
        }
        
        showEditProductDialog(selected);
    }
    private void showEditProductDialog(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Modify product details");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(product.getName());
        TextArea descArea = new TextArea(product.getDescription());
        descArea.setPrefRowCount(3);
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField stockField = new TextField(String.valueOf(product.getStockQuantity()));
        TextField thresholdField = new TextField(String.valueOf(product.getLowStockThreshold()));
        ComboBox<Product.Category> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(Product.Category.values());
        categoryCombo.setValue(product.getCategory());
        TextField iconField = new TextField(product.getImageIcon());
        iconField.setMaxWidth(60);
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Price ($):"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Low Stock Alert:"), 0, 4);
        grid.add(thresholdField, 1, 4);
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryCombo, 1, 5);
        grid.add(new Label("Icon:"), 0, 6);
        grid.add(iconField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    product.setName(nameField.getText());
                    product.setDescription(descArea.getText());
                    product.setPrice(Double.parseDouble(priceField.getText()));
                    product.setStockQuantity(Integer.parseInt(stockField.getText()));
                    product.setLowStockThreshold(Integer.parseInt(thresholdField.getText()));
                    product.setCategory(categoryCombo.getValue());
                    product.setImageIcon(iconField.getText());
                    
                    return product;
                    
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(updatedProduct -> {
            if (updatedProduct != null) {
                if (productService.updateProduct(updatedProduct)) {
                    showAlert("Success", "Product updated successfully!");
                    loadProducts();
                } else {
                    showAlert("Error", "Failed to update product");
                }
            }
        });
    }
    private void handleDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert("No Selection", "Please select a product to delete");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Product");
        confirm.setContentText("Are you sure you want to delete:\n" + selected.getName() + "?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (productService.deleteProduct(selected.getProductId())) {
                    showAlert("Success", "Product deleted successfully!");
                    loadProducts();
                } else {
                    showAlert("Error", "Failed to delete product");
                }
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
