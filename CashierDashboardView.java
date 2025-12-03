package com.mycompany.coffee_pos;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;              // NEW
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;


public class CashierDashboardView extends DashboardView {
    
    // POS interface components
    private VBox cartArea;
    private Label totalLabel;
    private Sale currentSale;
   
    public CashierDashboardView(User user) {
        super(user);
        this.saleService = new SaleService();
        this.productService = new ProductService();
        this.currentSale = new Sale(user.getUserId(), user.getFullName());
    }
    
    @Override
    protected void buildSpecificContent() {
        buildCashierSidebar();
        showPOSInterface();  // Default view for cashiers
    }
    
    private void buildCashierSidebar() {
        Button newSaleBtn = createMenuButton("New Sale", "🛒");
        Button productsBtn = createMenuButton("Products", "📦");
        Button historyBtn = createMenuButton("History", "📜");
        Button summaryBtn = createMenuButton("Daily Summary", "📊");
        
        newSaleBtn.setOnAction(e -> showPOSInterface());
        productsBtn.setOnAction(e -> showProducts());
        historyBtn.setOnAction(e -> showHistory());
        summaryBtn.setOnAction(e -> showDailySummary());
        
        sidebar.getChildren().addAll(
            newSaleBtn,
            productsBtn,
            historyBtn,
            summaryBtn
        );
    }
    
    private void showPOSInterface() {
        BorderPane posLayout = new BorderPane();
        posLayout.setStyle("-fx-background-color: #F5EDE0;");
        
        // LEFT: PRODUCT GRID
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(650);
        
        Label productsTitle = new Label("☕ Menu");
        productsTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        productsTitle.setStyle("-fx-text-fill: #5D4E37;");
        
        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(400);
        searchField.setPrefHeight(40);
        searchField.getStyleClass().add("text-field");
        
        Button searchBtn = new Button("🔍 Search");
        searchBtn.setPrefHeight(40);
        searchBtn.setStyle(
            "-fx-background-color: #8B6F47;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;"
        );
        
        searchBox.getChildren().addAll(searchField, searchBtn);
        
        // Product grid
        GridPane productGrid = new GridPane();
        productGrid.setHgap(15);
        productGrid.setVgap(15);
        productGrid.setPadding(new Insets(10));
        
        // Load actual products from database (master list for filtering)
        final List<Product> allProducts = productService.getAvailableProducts();

        // Initial fill
        refreshProductGrid(productGrid, allProducts);

        // Search logic (button + Enter key)
        Runnable doSearch = () -> {
            String keyword = searchField.getText().trim().toLowerCase();
            List<Product> filtered = new ArrayList<>();

            if (keyword.isEmpty()) {
                filtered = allProducts; // show all
            } else {
                for (Product p : allProducts) {
                    if (p.getName().toLowerCase().contains(keyword)) {
                        filtered.add(p);
                    }
                }
            }
            refreshProductGrid(productGrid, filtered);
        };

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
    String keyword = newValue.toLowerCase();

    List<Product> filtered = new ArrayList<>();
    for (Product p : allProducts) {
        if (p.getName().toLowerCase().contains(keyword)) {
            filtered.add(p);
        }
    }
    refreshProductGrid(productGrid, filtered);
});

        
        ScrollPane productsScroll = new ScrollPane(productGrid);
        productsScroll.setFitToWidth(true);
        productsScroll.setStyle("-fx-background: #F5EDE0; -fx-background-color: #F5EDE0;");
        
        leftPanel.getChildren().addAll(productsTitle, searchBox, productsScroll);
        
        // RIGHT: SHOPPING CART
        VBox rightPanel = new VBox(20);
        rightPanel.setPrefWidth(350);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: #E8DCC8;" +
            "-fx-border-color: #D4C5B9;" +
            "-fx-border-width: 0 0 0 2;"
        );
        
        Label cartTitle = new Label("🛒 Current Order");
        cartTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        cartTitle.setStyle("-fx-text-fill: #5D4E37;");
        
        // Cart items area
        cartArea = new VBox(10);
        cartArea.setPrefHeight(400);
        cartArea.setStyle("-fx-background-color: #FFF8F0; -fx-background-radius: 10; -fx-padding: 10;");
        
        Label emptyLabel = new Label("Cart is empty\nAdd items to start");
        emptyLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
        emptyLabel.setAlignment(Pos.CENTER);
        cartArea.getChildren().add(emptyLabel);
        
        // Total section
        VBox totalSection = new VBox(10);
        totalSection.setStyle(
            "-fx-background-color: #FFF8F0;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;"
        );
        
        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER);
        Label totalText = new Label("TOTAL:");
        totalText.setFont(Font.font("System", FontWeight.BOLD, 18));
        totalText.setStyle("-fx-text-fill: #5D4E37;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        totalLabel = new Label("$0.00");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #6B8E23;");
        
        totalBox.getChildren().addAll(totalText, spacer, totalLabel);
        
        // Complete sale button
        Button completeSaleBtn = new Button("💳 Complete Sale");
        completeSaleBtn.setPrefWidth(300);
        completeSaleBtn.setPrefHeight(50);
        completeSaleBtn.setStyle(
            "-fx-background-color: #6B8E23;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );
        
        completeSaleBtn.setOnAction(e -> completeSale());
        
        // Clear cart button
        Button clearBtn = new Button("🗑️ Clear Cart");
        clearBtn.setPrefWidth(300);
        clearBtn.setPrefHeight(40);
        clearBtn.setStyle(
            "-fx-background-color: #C87941;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );
        
        clearBtn.setOnAction(e -> clearCart());
        
        totalSection.getChildren().addAll(totalBox);
        
        rightPanel.getChildren().addAll(
            cartTitle,
            cartArea,
            totalSection,
            completeSaleBtn,
            clearBtn
        );
        
        // ADD TO LAYOUT
        posLayout.setLeft(leftPanel);
        posLayout.setRight(rightPanel);
        
        switchContent(posLayout);
    }
    
    /**
     * Helper: rebuilds product grid from a list (used by search)
     */
    private void refreshProductGrid(GridPane productGrid, List<Product> products) {
        productGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Product product : products) {
            VBox productCard = createProductCard(
                product.getImageIcon(),
                product.getName(),
                product.getFormattedPrice(),
                product
            );
            productGrid.add(productCard, col, row);
            animateProductCard(productCard, (row * 3 + col) * 50);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * CREATE PRODUCT CARD
     */
    private VBox createProductCard(String icon, String name, String priceStr, Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(190, 150);
        card.setStyle(
            "-fx-background-color: #FFF8F0;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #D4C5B9;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.2), 8, 0, 0, 2);"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 48px;");
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: #5D4E37;");
        
        Label priceLabel = new Label(priceStr);
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        priceLabel.setStyle("-fx-text-fill: #8B6F47;");
        
        card.getChildren().addAll(iconLabel, nameLabel, priceLabel);
        
        if (product.isLowStock()) {
            Label stockWarning = new Label("⚠️ Low Stock");
            stockWarning.setStyle("-fx-font-size: 10px; -fx-text-fill: #C87941;");
            card.getChildren().add(stockWarning);
        }
        
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: #FFFCF7;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #8B6F47;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.4), 12, 0, 0, 4);"
            );
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: #FFF8F0;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #D4C5B9;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.2), 8, 0, 0, 2);"
            );
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        card.setOnMouseClicked(e -> addToCart(product));
        
        return card;
    }
    
    private void animateProductCard(VBox card, int delay) {
        card.setOpacity(0);
        card.setScaleX(0.5);
        card.setScaleY(0.5);
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), card);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(delay));
        
        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.play();
    }
    
    private void addToCart(Product product) {
        if (product.isOutOfStock()) {
            showAlert("Out of Stock", product.getName() + " is currently out of stock!");
            return;
        }
        
        if (cartArea.getChildren().size() == 1 && 
            cartArea.getChildren().get(0) instanceof Label) {
            cartArea.getChildren().clear();
        }
        
        SaleItem saleItem = new SaleItem(
            product.getProductId(),
            product.getName(),
            1,
            product.getPrice()
        );
        currentSale.addItem(saleItem);
        
        HBox cartItem = createCartItemUI(saleItem);
        
        cartItem.setOpacity(0);
        cartItem.setTranslateX(-50);
        cartArea.getChildren().add(cartItem);
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), cartItem);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), cartItem);
        slide.setFromX(-50);
        slide.setToX(0);
        
        ParallelTransition parallel = new ParallelTransition(fade, slide);
        parallel.play();
        
        updateTotal();
    }
    
    private HBox createCartItemUI(SaleItem item) {
        HBox cartItem = new HBox(10);
        cartItem.setAlignment(Pos.CENTER_LEFT);
        cartItem.setPadding(new Insets(8));
        cartItem.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #E8DCC8;" +
            "-fx-border-radius: 8;"
        );
        
        Label itemLabel = new Label(item.getProductName());
        itemLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        itemLabel.setStyle("-fx-text-fill: #5D4E37;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label priceLabel = new Label(String.format("$%.2f", item.getPrice()));
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        priceLabel.setStyle("-fx-text-fill: #8B6F47;");
        
        Button removeBtn = new Button("✖");
        removeBtn.setStyle(
            "-fx-background-color: #C87941;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-background-radius: 50%;" +
            "-fx-min-width: 25;" +
            "-fx-min-height: 25;" +
            "-fx-max-width: 25;" +
            "-fx-max-height: 25;" +
            "-fx-cursor: hand;"
        );
        
        removeBtn.setOnAction(e -> {
            currentSale.removeItem(item);
            cartArea.getChildren().remove(cartItem);
            updateTotal();
            
            if (cartArea.getChildren().isEmpty()) {
                Label emptyLabel = new Label("Cart is empty\nAdd items to start");
                emptyLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
                emptyLabel.setAlignment(Pos.CENTER);
                cartArea.getChildren().add(emptyLabel);
            }
        });
        
        cartItem.getChildren().addAll(itemLabel, spacer, priceLabel, removeBtn);
        return cartItem;
    }
    
    private void updateTotal() {
        totalLabel.setText(currentSale.getFormattedTotal());
        
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), totalLabel);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }
    
    private void completeSale() {
        if (currentSale.getItems().isEmpty()) {
            showAlert("Empty Cart", "Please add items to cart before completing sale.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Complete Sale");
        alert.setHeaderText("Confirm Sale");
        alert.setContentText(String.format("Total: %s\n\nProcess payment?", currentSale.getFormattedTotal()));
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (saleService.completeSale(currentSale)) {
generateReceipt(currentSale);
                    clearCart();
                    currentSale = new Sale(getCurrentUser().getUserId(), getCurrentUser().getFullName());
                } else {
                    showAlert("Error", "Failed to complete sale. Check stock levels.");
                }
            }
        });
    }
    
    private void showSuccessMessage() {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Sale Complete");
        success.setHeaderText("✅ Transaction Successful!");
        success.setContentText("Receipt printed.\nThank you!");
        success.showAndWait();
    }
    
    private void clearCart() {
        cartArea.getChildren().clear();
        currentSale = new Sale(getCurrentUser().getUserId(), getCurrentUser().getFullName());
        totalLabel.setText("$0.00");
        
        Label emptyLabel = new Label("Cart is empty\nAdd items to start");
        emptyLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
        emptyLabel.setAlignment(Pos.CENTER);
        cartArea.getChildren().add(emptyLabel);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showProducts() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Product Catalog");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #5D4E37;");
        
        TableView<Product> table = new TableView<>();
        table.setPrefHeight(450);
        
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(60);
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);
        
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        stockCol.setPrefWidth(80);
        
        table.getColumns().addAll(idCol, nameCol, priceCol, stockCol);
        table.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        
        content.getChildren().addAll(title, table);
        switchContent(content);
    }
    
    private void showHistory() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Transaction History");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        TableView<Sale> table = new TableView<>();
        table.setPrefHeight(400);
        table.setItems(FXCollections.observableArrayList(
            saleService.getAllSales()
        ));
        
        TableColumn<Sale, Integer> idCol = new TableColumn<>("Sale ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        
        TableColumn<Sale, String> cashierCol = new TableColumn<>("Cashier");
        cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        
        TableColumn<Sale, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        
        TableColumn<Sale, String> dateCol = new TableColumn<>("Time");
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getSaleDate().toString()
        ));
        
        table.getColumns().addAll(idCol, cashierCol, amountCol, dateCol);
        
        table.setOnMouseClicked(e -> {
            Sale selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showSaleItemsDialog(selected);
            }
        });
        
        content.getChildren().addAll(title, table);
        switchContent(content);
    }
    
    private void showSaleItemsDialog(Sale sale) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Sale #" + sale.getSaleId() + " Details");
        
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        
        List<SaleItem> items = saleService.getSaleItems(sale.getSaleId());
        
        if (items.isEmpty()) {
            box.getChildren().add(new Label("No items found for this sale."));
        } else {
            for (SaleItem i : items) {
                Label label = new Label(
                    i.getProductName() + " — Qty: " + i.getQuantity() +
                    " — $" + i.getPrice() + " ea — Total: $" + i.getSubtotal()
                );
                box.getChildren().add(label);
            }
        }
        
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
    
    private void showDailySummary() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        
        Label title = new Label("Daily Summary");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        Label totalToday = new Label(
            "Total revenue today: $" + saleService.getTodayTotal()
        );
        totalToday.setFont(Font.font(18));
        
        Label countToday = new Label(
            "Transactions today: " + saleService.getTodaySalesCount()
        );
        countToday.setFont(Font.font(18));
        
        content.getChildren().addAll(title, totalToday, countToday);
        switchContent(content);
    }
    private void generateReceipt(Sale sale) {
    StringBuilder receipt = new StringBuilder();
    receipt.append("========== Coffee POS Receipt ==========\n");
    receipt.append("Sale ID: ").append(sale.getSaleId()).append("\n");
    receipt.append("Cashier: ").append(sale.getCashierName()).append("\n");
    receipt.append("Date: ").append(sale.getSaleDate()).append("\n");
    receipt.append("----------------------------------------\n");

    List<SaleItem> items = saleService.getSaleItems(sale.getSaleId());

    for (SaleItem item : items) {
        receipt.append(
            String.format("%-20s x%-2d  $%.2f\n",
                item.getProductName(),
                item.getQuantity(),
                item.getSubtotal())
        );
    }

    receipt.append("----------------------------------------\n");
    receipt.append(String.format("TOTAL: $%.2f\n", sale.getTotalAmount()));
    receipt.append("========================================\n");
    receipt.append("Thank you for your purchase!\n");

    showReceiptDialog(receipt.toString());
    saveReceiptToFile(sale.getSaleId(), receipt.toString());
}
private void showReceiptDialog(String receiptText) {
    TextArea textArea = new TextArea(receiptText);
    textArea.setEditable(false);
    textArea.setFont(Font.font("Monospaced", 12));
    textArea.setPrefSize(420, 380);

    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Receipt");
    dialog.getDialogPane().setContent(textArea);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    dialog.showAndWait();
}
private void saveReceiptToFile(int saleId, String content) {
    try {
        String filename = "receipt_" + saleId + ".txt";
        java.nio.file.Files.write(
            java.nio.file.Paths.get(filename),
            content.getBytes()
        );
    } catch (Exception e) {
        System.err.println("Error saving receipt: " + e.getMessage());
    }
}
}
