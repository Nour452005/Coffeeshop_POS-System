package com.mycompany.coffee_pos;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * LoginController - SIMPLIFIED login logic
 */
public class LoginController {
    
    private LoginView view;
    private UserService userService;
    
    public LoginController(LoginView view) {
        this.view = view;
        this.userService = new UserService();
    }
    
    public void handleLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();
        String selectedRole = view.getRoleComboBox().getValue();
        
        view.getErrorLabel().setVisible(false);
        
        if (username.isEmpty()) {
            showError("Please enter username");
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter password");
            return;
        }
        
        if (selectedRole == null) {
            showError("Please select a role");
            return;
        }
        
        User.Role role = selectedRole.equals("Admin") ? User.Role.ADMIN : User.Role.CASHIER;
        User user = userService.authenticateUser(username, password, role);
        
        if (user != null) {
            navigateToDashboard(user);
        } else {
            showError("Invalid credentials!");
        }
    }
    
    private void navigateToDashboard(User user) {
        try {
             ProductService productService = new ProductService();
            Stage stage = (Stage) view.getView().getScene().getWindow();
            
            DashboardView dashboard = user.isAdmin() ? 
                new AdminDashboardView(user) : 
                new CashierDashboardView(user);
            
            Scene scene = new Scene(dashboard.getView(), 1400, 900);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception ex) {}
            
            stage.setScene(scene);
            stage.setTitle("Coffee Shop POS - " + user.getRole());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleRegister() {
        try {
            Stage stage = (Stage) view.getView().getScene().getWindow();
            RegisterView registerView = new RegisterView();
            Scene scene = new Scene(registerView.getView(), 1200, 800);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception ex) {}
            
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showError(String message) {
        view.getErrorLabel().setText(message);
        view.getErrorLabel().setVisible(true);
    }
}