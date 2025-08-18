package com.pahanaEdu.dao;

import com.pahanaEdu.model.UserModel;
import com.pahanaEdu.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    public boolean validate(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Direct password comparison

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a row is returned, credentials are valid
            }
        } catch (SQLException e) {
            System.err.println("Database error during login for user: " + username);
            e.printStackTrace();
            return false;
        }
    }

    public UserModel getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserModel user = new UserModel();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
