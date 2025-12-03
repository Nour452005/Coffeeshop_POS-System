/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminDashboardView extends DashboardView {
    
    // Statistics labels (updated with live data)
    private Label totalUsersLabel;
    private Label totalProductsLabel;
    private Label totalSalesLabel;
    private Label lowStockLabel;
   
    public AdminDashboardView(User user) {
        super(user);
        this.productService = new ProductService();
        this.saleService = new SaleService();
    }
    
    @Override
    protected void buildSpecificContent() {
        buildAdminSidebar();
        
        showOverview();
    }
    
    private void buildAdminSidebar() {
        // Create menu buttons with icons
        Button overviewBtn = createMenuButton("Overview", "📊");
        Button usersBtn = createMenuButton("User Management", "👥");
        Button productsBtn = createMenuButton("Products", "📦");
        Button inventoryBtn = createMenuButton("Inventory", "📋");
        Button reportsBtn = createMenuButton("Reports", "📈");
        Button settingsBtn = createMenuButton("Settings", "⚙️");
       

        // Set button actions
        overviewBtn.setOnAction(e -> showOverview());
        usersBtn.setOnAction(e -> showUserManagement());
        productsBtn.setOnAction(e -> showProductManagement());
        inventoryBtn.setOnAction(e -> showInventoryManagement());
        reportsBtn.setOnAction(e -> showReports());
        settingsBtn.setOnAction(e -> showSettings());
        
        // Add buttons to sidebar
        sidebar.getChildren().addAll(
            overviewBtn,
            usersBtn,
            productsBtn,
            inventoryBtn,
            reportsBtn,
            new Separator(),
            settingsBtn
        );
    }
   
    private void showOverview() {
        VBox overview = new VBox(30);
        overview.setAlignment(Pos.TOP_CENTER);
        overview.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Admin Dashboard Overview");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #5D4E37;");
        
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);
        
        // Create 4 statistic cards with real data
        VBox usersCard = createStatCard("👥", "Total Users", String.valueOf(getUserCount()), "#8B6F47");
        VBox productsCard = createStatCard("📦", "Products", String.valueOf(productService.getAllProducts().size()), "#B8956A");
        VBox salesCard = createStatCard("💰", "Today's Sales", String.format("$%.2f", saleService.getTodayTotal()), "#6B8E23");
        VBox stockCard = createStatCard("⚠️", "Low Stock", String.valueOf(productService.getLowStockProducts().size()), "#C87941");
        
        // Add cards to grid
        statsGrid.add(usersCard, 0, 0);
        statsGrid.add(productsCard, 1, 0);
        statsGrid.add(salesCard, 0, 1);
        statsGrid.add(stockCard, 1, 1);
        
        // Animate each card with delay
        animateCard(usersCard, 0);
        animateCard(productsCard, 100);
        animateCard(salesCard, 200);
        animateCard(stockCard, 300);
        
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        actionsTitle.setStyle("-fx-text-fill: #5D4E37;");
        
        HBox quickActions = new HBox(15);
        quickActions.setAlignment(Pos.CENTER);
        
        Button addUserBtn = createActionButton("Add User", "👤");
        Button addProductBtn = createActionButton("Add Product", "➕");
        Button viewReportsBtn = createActionButton("View Reports", "📊");
        
        addUserBtn.setOnAction(e -> showUserManagement());
        addProductBtn.setOnAction(e -> showProductManagement());
        viewReportsBtn.setOnAction(e -> showReports());
        
        quickActions.getChildren().addAll(addUserBtn, addProductBtn, viewReportsBtn);
        
        // Add all to overview
        overview.getChildren().addAll(titleLabel, statsGrid, actionsTitle, quickActions);
        
        // Switch to this content with animation
        switchContent(overview);
    }
    
    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(200, 150);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: #FFF8F0;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.2), 10, 0, 0, 3);"
        );
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 40px;");
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        // Description
        Label descLabel = new Label(label);
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        descLabel.setStyle("-fx-text-fill: #8B7355;");
        
        card.getChildren().addAll(iconLabel, valueLabel, descLabel);
        
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
            
            card.setStyle(
                "-fx-background-color: #FFFCF7;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.4), 15, 0, 0, 5);"
            );
        });
        
        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
            
            card.setStyle(
                "-fx-background-color: #FFF8F0;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(93, 78, 55, 0.2), 10, 0, 0, 3);"
            );
        });
        
        return card;
    }
    
    private void animateCard(VBox card, int delay) {
        card.setOpacity(0);
        card.setTranslateY(20);
        card.setScaleX(0.8);
        card.setScaleY(0.8);
        
        // Fade in
        FadeTransition fade = new FadeTransition(Duration.millis(400), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));
        
        // Slide up
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), card);
        slide.setFromY(20);
        slide.setToY(0);
        slide.setDelay(Duration.millis(delay));
        
        // Scale up
        ScaleTransition scale = new ScaleTransition(Duration.millis(400), card);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setDelay(Duration.millis(delay));
        
        ParallelTransition parallel = new ParallelTransition(fade, slide, scale);
        parallel.play();
    }
    
    private Button createActionButton(String text, String icon) {
        Button btn = new Button(icon + " " + text);
        btn.setPrefSize(150, 50);
        btn.setStyle(
            "-fx-background-color: #8B6F47;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                "-fx-background-color: #A0856B;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                "-fx-background-color: #8B6F47;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });
        
        return btn;
    }
    
    private void showUserManagement() {
    UserManagementView userView = new UserManagementView();
    switchContent(userView.getView());
}
    private void showProductManagement() {
        ProductManagementView productView = new ProductManagementView();
        switchContent(productView.getView());
    }
    
    private void showInventoryManagement() {
        InventoryManagementView inventoryView = new InventoryManagementView();
        switchContent(inventoryView.getView());
    }
    
    private void showReports() {
    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(20));

    Label title = new Label("Sales Reports");
    title.setFont(Font.font("System", FontWeight.BOLD, 28));
    title.setStyle("-fx-text-fill: #5D4E37;");

    // Summary section
    HBox summary = new HBox(30);
    summary.setAlignment(Pos.CENTER);

    Label totalSalesToday = new Label("Today's Total: $" + saleService.getTodayTotal());
    totalSalesToday.setFont(Font.font("System", FontWeight.BOLD, 18));

    Label countToday = new Label("Transactions Today: " + saleService.getTodaySalesCount());
    countToday.setFont(Font.font("System", FontWeight.BOLD, 18));

    summary.getChildren().addAll(totalSalesToday, countToday);

    // Sales Table
    TableView<Sale> salesTable = new TableView<>();
    ObservableList<Sale> salesData = FXCollections.observableArrayList(saleService.getAllSales());
    salesTable.setItems(salesData);
    salesTable.setPrefHeight(350);

    TableColumn<Sale, Integer> idCol = new TableColumn<>("Sale ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
    idCol.setPrefWidth(80);

    TableColumn<Sale, String> cashierCol = new TableColumn<>("Cashier");
    cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
    cashierCol.setPrefWidth(140);

    TableColumn<Sale, Double> amountCol = new TableColumn<>("Amount");
    amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
    amountCol.setPrefWidth(80);

    TableColumn<Sale, String> dateCol = new TableColumn<>("Sale Time");
    dateCol.setCellValueFactory(d -> new SimpleStringProperty(
        d.getValue().getSaleDate().toString()
    ));
    dateCol.setPrefWidth(220);

    salesTable.getColumns().addAll(idCol, cashierCol, amountCol, dateCol);

    salesTable.setOnMouseClicked(e -> {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showSaleItemsDialog(selected);
        }
    });

    content.getChildren().addAll(title, summary, salesTable);
    switchContent(content);
}
    private void showSaleItemsDialog(Sale sale) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Sale #" + sale.getSaleId() + " Details");

    VBox box = new VBox(10);
    box.setPadding(new Insets(10));

    List<SaleItem> items = saleService.getSaleItems(sale.getSaleId());

    for (SaleItem i : items) {
        Label label = new Label(
            i.getProductName() + " — Qty: " + i.getQuantity() +
            " — $" + i.getPrice() + " ea — Total: $" + i.getSubtotal()
        );
        box.getChildren().add(label);
    }

    dialog.getDialogPane().setContent(box);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    dialog.showAndWait();
}


    
    private void showSettings() {
    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(30));
    
    Label title = new Label("⚙️ System Settings");
    title.setFont(Font.font("System", FontWeight.BOLD, 26));
    title.setStyle("-fx-text-fill: #5D4E37;");

    Button changeNameBtn = createSettingButton("✏️ Change Display Name");
    Button changePasswordBtn = createSettingButton("🔑 Change Password");
    Button themeBtn = createSettingButton("🎨 Change Theme");
//    Button backupBtn = createSettingButton("💾 Backup Database");
    Button logoutBtn = createSettingButton("🚪 Logout");

    changeNameBtn.setOnAction(e -> handleChangeName());
    changePasswordBtn.setOnAction(e -> handleChangePassword());
    themeBtn.setOnAction(e -> handleThemeChange());
//    backupBtn.setOnAction(e -> handleBackupDB());
    logoutBtn.setOnAction(e -> handleLogout());

    content.getChildren().addAll(
        title,
        changeNameBtn,
        changePasswordBtn,
        themeBtn,
//        backupBtn,
        logoutBtn
    );

    switchContent(content);
}
    
    private Button createSettingButton(String text) {
    Button btn = new Button(text);
    btn.setPrefSize(240, 45);
    btn.setStyle(
        "-fx-background-color: #8B6F47;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    );

    btn.setOnMouseEntered(e -> btn.setStyle(
        "-fx-background-color: #A0856B;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    ));
    btn.setOnMouseExited(e -> btn.setStyle(
        "-fx-background-color: #8B6F47;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;" +
        "-fx-cursor: hand;"
    ));

    return btn;
}
private void handleChangeName() {
    TextInputDialog dialog = new TextInputDialog(getCurrentUser().getFullName());
    dialog.setTitle("Change Name");
    dialog.setHeaderText("Enter new display name:");

    dialog.showAndWait().ifPresent(newName -> {
        newName = newName.trim();
        if (newName.isEmpty()) {
            showAlert("Invalid", "Name cannot be empty");
            return;
        }
       UserService us = new UserService();
if (us.updateName(getCurrentUser().getUserId(), newName)) {
    getCurrentUser().setFullName(newName);
    welcomeLabel.setText("Welcome, " + newName + "!");
    showAlert("Success", "Name updated in database.");
} else {
    showAlert("Error", "Failed to update name.");
}
    });
}
private void handleChangePassword() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Change Password");
    dialog.setHeaderText("Enter your new password:");

    PasswordField pwdField = new PasswordField();

    VBox box = new VBox(10, new Label("New Password:"), pwdField);
    dialog.getDialogPane().setContent(box);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialog.setResultConverter(btn -> {
        if (btn == ButtonType.OK) {
            return pwdField.getText();
        }
        return null;
    });

    dialog.showAndWait().ifPresent(newPwd -> {
        if (newPwd.trim().isEmpty()) {
            showAlert("Invalid", "Password cannot be empty");
            return;
        }
       UserService us = new UserService();
if (us.updatePassword(getCurrentUser().getUserId(), newPwd)) {
    getCurrentUser().setPassword(newPwd);
    showAlert("Success", "Password updated in database.");
} else {
    showAlert("Error", "Failed to update password.");
}

    });
}
private void handleThemeChange() {
    ChoiceDialog<String> dialog = new ChoiceDialog<>("Default", "Default", "Dark");
    dialog.setTitle("Select Theme");
    dialog.setHeaderText("Choose application theme:");
    dialog.setContentText("Theme:");

    dialog.showAndWait().ifPresent(choice -> {
        if (choice.equals("Dark")) {
            setTheme("dark");
            showAlert("Theme Applied", "Dark mode activated.");
        } else {
            setTheme("light");
            showAlert("Theme Applied", "Default theme restored.");
        }
    });
}

private void handleBackupDB() {
    showAlert("Backup Complete", "Database backup created successfully.");
}

private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}


    
   private int getUserCount() {
    UserService us = new UserService();
    return us.getAllUsers().size();
}

}