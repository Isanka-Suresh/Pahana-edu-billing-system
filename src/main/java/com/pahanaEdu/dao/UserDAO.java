package com.pahanaEdu.dao;

import java.sql.*;

public class UserDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/pahana_db?useSSL=false&serverTimezone=UTC";
    private final String jdbcUser = "root";
    private final String jdbcPass = "2002I$@n";

    // Load the MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public boolean validate(String username, String password) {
        boolean status = false;
        String sql = "SELECT * FROM users WHERE username=? AND password_hash=MD5(?)";
        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            status = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
