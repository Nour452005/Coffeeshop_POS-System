/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coffee_pos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - SIMPLIFIED user database operations
 */

public class UserDAO {

    private DatabaseManager dbManager;

    public UserDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public User authenticateUser(String username, String password, User.Role role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role.name());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    User.Role.valueOf(rs.getString("role")),
                    rs.getString("full_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }

    public boolean addUser(User user) {
        if (usernameExists(user.getUsername()))
            return false;

        String query = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getRole().name());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
        return false;
    }

    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY username";

        try (Connection conn = dbManager.getConnection();
Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    User.Role.valueOf(rs.getString("role")),
                    rs.getString("full_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
        }
        return list;
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    public boolean updateRole(int userId, User.Role newRole) {
        String query = "UPDATE users SET role = ? WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newRole.name());
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating role: " + e.getMessage());
        }
        return false;
    }
    public boolean updateFullName(int userId, String newName) {
    String query = "UPDATE users SET full_name = ? WHERE user_id = ?";
    try (Connection conn = dbManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, newName);
        pstmt.setInt(2, userId);

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error updating name: " + e.getMessage());
    }
    return false;
}

public boolean updatePassword(int userId, String newPassword) {
    String query = "UPDATE users SET password = ? WHERE user_id = ?";
    try (Connection conn = dbManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, newPassword);
        pstmt.setInt(2, userId);

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error updating password: " + e.getMessage());
    }
    return false;
}

}
