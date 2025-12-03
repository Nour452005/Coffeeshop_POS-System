/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

/**
 *
 * @author USER
 */
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


public abstract class DashboardView {
    protected BorderPane rootPane;           // Main layout container
    protected VBox sidebar;                  // Left navigation menu
    protected StackPane contentArea;         // Center content area
    protected User currentUser;              // Logged-in user
    protected Label welcomeLabel;            // Welcome message
    
    // Animation-related
    protected Timeline fadeInTimeline;       // For fade-in animations
    protected ProductService productService;
protected SaleService saleService;

    public DashboardView(User user) {
    this.currentUser = user;

    // initialize services BEFORE UI builds
    this.productService = new ProductService();
    this.saleService = new SaleService();

    buildUI();
    applyEntryAnimation();
}
    private void buildUI() {
        rootPane = new BorderPane();
        rootPane.setStyle("-fx-background-color: #F5EDE0;"); // Beige background
        HBox header = createHeader();
        rootPane.setTop(header);
        sidebar = createSidebar();
        rootPane.setLeft(sidebar);
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #FFF8F0;");
        contentArea.setPadding(new Insets(20));
        
        // Call abstract method - child class provides specific content
        buildSpecificContent();
        
        rootPane.setCenter(contentArea);
    }
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B6F47, #6F5438);" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        Label logoLabel = new Label("☕ Coffee POS");
        logoLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: #FFF8F0;");
        
        // Spacer to push content apart
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        welcomeLabel = new Label("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        welcomeLabel.setStyle("-fx-text-fill: #FFF8F0;");
        Label roleBadge = new Label(currentUser.getRole().toString());
        roleBadge.setStyle(
            "-fx-background-color: #FFF8F0;" +
            "-fx-text-fill: #5D4E37;" +
            "-fx-padding: 5 15 5 15;" +
            "-fx-background-radius: 15;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
        );
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: #C87941;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        logoutBtn.setOnMouseEntered(e -> 
            logoutBtn.setStyle(
                "-fx-background-color: #D4956F;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            )
        );
        logoutBtn.setOnMouseExited(e -> 
            logoutBtn.setStyle(
                "-fx-background-color: #C87941;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            )
        );
        
        logoutBtn.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(logoLabel, spacer, welcomeLabel, roleBadge, logoutBtn);
        
        return header;
    }
    protected VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle(
            "-fx-background-color: #E8DCC8;" +
            "-fx-border-color: #D4C5B9;" +
            "-fx-border-width: 0 2 0 0;"
        );
        sidebar.setPadding(new Insets(20));
        Label sidebarTitle = new Label("📊 Dashboard");
        sidebarTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sidebarTitle.setStyle("-fx-text-fill: #5D4E37;");
        
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #D4C5B9;");
        
        sidebar.getChildren().addAll(sidebarTitle, separator);
        
        return sidebar;
    }
    protected Button createMenuButton(String text, String icon) {
        Button btn = new Button(icon + "  " + text);
        btn.setPrefWidth(210);
        btn.setPrefHeight(45);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(
            "-fx-background-color: #FFF8F0;" +
            "-fx-text-fill: #5D4E37;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #D4C5B9;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), btn);
        scaleIn.setToX(1.05);
        scaleIn.setToY(1.05);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), btn);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                "-fx-background-color: #E8DCC8;" +
                "-fx-text-fill: #5D4E37;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #B8956A;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;"
            );
            scaleIn.play();
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                "-fx-background-color: #FFF8F0;" +
                "-fx-text-fill: #5D4E37;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #D4C5B9;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;"
            );
            scaleOut.play();
        });
        
        return btn;
    }
    private void applyEntryAnimation() {
        // Start invisible
        rootPane.setOpacity(0);
        rootPane.setScaleX(0.95);
        rootPane.setScaleY(0.95);
        rootPane.setTranslateY(-20);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), rootPane);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1.0);
        scale.setToY(1.0);
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), rootPane);
        slide.setFromY(-20);
        slide.setToY(0);
        ParallelTransition parallel = new ParallelTransition(fadeIn, scale, slide);
        parallel.play();
    }
    protected void handleLogout() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), rootPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        
        fadeOut.setOnFinished(e -> {
            // Switch to login screen
            try {
                javafx.stage.Stage stage = (javafx.stage.Stage) rootPane.getScene().getWindow();
                LoginView loginView = new LoginView();
                javafx.scene.Scene scene = new javafx.scene.Scene(loginView.getView(), 1200, 800);
                
                try {
                    scene.getStylesheets().add(
                        getClass().getResource("/css/style.css").toExternalForm()
                    );
                } catch (Exception ex) {
                    System.out.println("CSS not found");
                }
                
                stage.setScene(scene);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        fadeOut.play();
    }
    protected void switchContent(javafx.scene.Node newContent) {
        // Fade out current content
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), contentArea);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        
        fadeOut.setOnFinished(e -> {
            // Replace content
            contentArea.getChildren().clear();
            contentArea.getChildren().add(newContent);
            
            // Fade in new content
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        
        fadeOut.play();
    }
    protected abstract void buildSpecificContent();
    public BorderPane getView() {
        return rootPane;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    protected void setTheme(String themeName) {
    Scene scene = rootPane.getScene();
    scene.getStylesheets().clear();

    if (themeName.equals("dark")) {
        scene.getStylesheets().add(getClass().getResource("/css/darktheme.css").toExternalForm());
    } else {
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
    }
}

}
