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
 * RegisterView - SIMPLIFIED registration screen
 */
public class RegisterView {
    
    private StackPane rootPane;
    private TextField fullNameField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> roleComboBox;
    private Label messageLabel;
    private RegisterController controller;
    
    public RegisterView() {
        controller = new RegisterController(this);
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
        
        VBox registerBox = createRegisterBox();
        
        Label footer = new Label("Coffee Shop POS v1.0");
        footer.setStyle("-fx-text-fill: #5D4E37; -fx-font-size: 12px; -fx-font-weight: bold;");
        StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);
        StackPane.setMargin(footer, new Insets(0, 0, 30, 0));
        
        mainContent.getChildren().addAll(coffeeIcon, registerBox);
        rootPane.getChildren().addAll(mainContent, footer);
    }
    
    private VBox createRegisterBox() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(400);
        box.setPadding(new Insets(40));
        box.setStyle(
            "-fx-background-color: #FFF8F0; " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: #D4C5B9; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15;"
        );
        
        Label title = new Label("Create Account");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #5D4E37;");
        
        Label subtitle = new Label("Register to get started");
        subtitle.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
        
        String fieldStyle = "-fx-background-radius: 5; -fx-border-color: #D4C5B9; -fx-border-radius: 5;";
        
        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.setPrefHeight(40);
        fullNameField.setStyle(fieldStyle);
        
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle(fieldStyle);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle(fieldStyle);
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setPrefHeight(40);
        confirmPasswordField.setStyle(fieldStyle);
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Cashier", "Admin");
        roleComboBox.setValue("Cashier");
        roleComboBox.setPrefHeight(40);
        roleComboBox.setMaxWidth(Double.MAX_VALUE);
        
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #C87941; -fx-font-size: 12px;");
        messageLabel.setWrapText(true);
        messageLabel.setVisible(false);
        
        Button registerBtn = new Button("Create Account");
        registerBtn.setPrefSize(320, 45);
        registerBtn.setStyle(
            "-fx-background-color: #8B6F47; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        registerBtn.setOnAction(e -> controller.handleRegister());
        
        HBox loginLink = new HBox(5);
        loginLink.setAlignment(Pos.CENTER);
        Label hasAccount = new Label("Already have an account?");
        hasAccount.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #8B6F47; " +
            "-fx-font-size: 12px; " +
            "-fx-underline: true;"
        );
        loginBtn.setOnAction(e -> controller.handleBackToLogin());
        loginLink.getChildren().addAll(hasAccount, loginBtn);
        
        box.getChildren().addAll(title, subtitle, fullNameField, usernameField, passwordField, confirmPasswordField, roleComboBox, messageLabel, registerBtn, loginLink);
        return box;
    }
    
    public TextField getFullNameField() { return fullNameField; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public PasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public ComboBox<String> getRoleComboBox() { return roleComboBox; }
    public Label getMessageLabel() { return messageLabel; }
    public StackPane getView() { return rootPane; }
}