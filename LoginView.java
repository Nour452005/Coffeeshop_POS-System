/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * LoginView - SIMPLIFIED login screen
 */
public class LoginView {
    
    private StackPane rootPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private Label errorLabel;
    private LoginController controller;
    
    public LoginView() {
        controller = new LoginController(this);
        buildUI();
    }
    
    private void buildUI() {
        rootPane = new StackPane();
        rootPane.setPrefSize(1200, 800);
        rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #D4C5B9, #B8A088);");
        
        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40));
        
        Label coffeeIcon = new Label("☕");
        coffeeIcon.setStyle("-fx-font-size: 64px;");
        
        VBox loginBox = createLoginBox();
        
        Label footer = new Label("Coffee Shop POS v1.0");
        footer.setStyle("-fx-text-fill: #5D4E37; -fx-font-size: 12px; -fx-font-weight: bold;");
        StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);
        StackPane.setMargin(footer, new Insets(0, 0, 30, 0));
        
        mainContent.getChildren().addAll(coffeeIcon, loginBox);
        rootPane.getChildren().addAll(mainContent, footer);
    }
    
    private VBox createLoginBox() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(380);
        box.setMaxWidth(380);
        box.setMinWidth(380);

        box.setPadding(new Insets(40));
        box.setStyle(
            "-fx-background-color: #FFF8F0; " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: #D4C5B9; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15;"
        );
        
        Label title = new Label("Coffee Shop POS");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #5D4E37;");
        
        Label subtitle = new Label("Please login to continue");
        subtitle.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
        
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-color: #D4C5B9; -fx-border-radius: 5;");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-color: #D4C5B9; -fx-border-radius: 5;");
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Cashier");
        roleComboBox.setPromptText("Select role");
        roleComboBox.setPrefHeight(40);
        roleComboBox.setMaxWidth(Double.MAX_VALUE);
        
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #C87941; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        
        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(300, 45);
        loginBtn.setStyle(
            "-fx-background-color: #8B6F47; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        loginBtn.setOnAction(e -> controller.handleLogin());
        
        HBox registerLink = new HBox(5);
        registerLink.setAlignment(Pos.CENTER);
        Label noAccount = new Label("Don't have an account?");
        noAccount.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");
        Button registerBtn = new Button("Register");
        registerBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #8B6F47; " +
            "-fx-font-size: 12px; " +
            "-fx-underline: true;"
        );
        registerBtn.setOnAction(e -> controller.handleRegister());
        registerLink.getChildren().addAll(noAccount, registerBtn);
        
        box.getChildren().addAll(title, subtitle, usernameField, passwordField, roleComboBox, errorLabel, loginBtn, registerLink);
        return box;
    }
    
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public ComboBox<String> getRoleComboBox() { return roleComboBox; }
    public Label getErrorLabel() { return errorLabel; }
    public StackPane getView() { return rootPane; }
}