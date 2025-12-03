/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.coffee_pos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Coffee_pos - SIMPLIFIED main application
 */
public class Coffee_pos extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            LoginView loginView = new LoginView();
            Scene scene = new Scene(loginView.getView(), 1200, 800);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception ex) {
                System.out.println("CSS not found, using default styles");
            }
            
            primaryStage.setTitle("Coffee Shop POS System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}