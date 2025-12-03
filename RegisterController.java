/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * RegisterController - SIMPLIFIED registration logic
 */
public class RegisterController {
    
    private RegisterView view;
    private UserService userService;
    
    public RegisterController(RegisterView view) {
        this.view = view;
        this.userService = new UserService();
    }
    
    public void handleRegister() {
        String fullName = view.getFullNameField().getText().trim();
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();
        String confirmPassword = view.getConfirmPasswordField().getText();
        String selectedRole = view.getRoleComboBox().getValue();
        
        view.getMessageLabel().setVisible(false);
        
        if (fullName.isEmpty()) {
            showMessage("Please enter your full name", false);
            return;
        }
        
        if (username.isEmpty() || username.length() < 3) {
            showMessage("Username must be at least 3 characters", false);
            return;
        }
        
        if (password.isEmpty() || password.length() < 6) {
            showMessage("Password must be at least 6 characters", false);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!", false);
            return;
        }
        
        User.Role role = selectedRole.equals("Admin") ? User.Role.ADMIN : User.Role.CASHIER;
        User newUser = new User(username, password, role, fullName);
        
        if (userService.addUser(newUser)) {
            showMessage("Account created! Redirecting to login...", true);
            
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> handleBackToLogin());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showMessage("Username already exists!", false);
        }
    }
    
    public void handleBackToLogin() {
        try {
            Stage stage = (Stage) view.getView().getScene().getWindow();
            LoginView loginView = new LoginView();
            Scene scene = new Scene(loginView.getView(), 1200, 800);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception ex) {}
            
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showMessage(String message, boolean isSuccess) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().setVisible(true);
        
        if (isSuccess) {
            view.getMessageLabel().setStyle("-fx-text-fill: #6B8E23; -fx-font-size: 12px;");
        } else {
            view.getMessageLabel().setStyle("-fx-text-fill: #C87941; -fx-font-size: 12px;");
        }
    }
}
