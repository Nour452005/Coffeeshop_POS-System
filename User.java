/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

public class User {

    public enum Role {
        ADMIN, CASHIER
    }

    private int userId;
    private String username;
    private String password; // plaintext for now — future: hash
    private Role role;
    private String fullName;

    // Full constructor
    public User(int userId, String username, String password, Role role, String fullName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    // Constructor for new user
    public User(String username, String password, Role role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    // Getters & setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getFullName() { return fullName; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
