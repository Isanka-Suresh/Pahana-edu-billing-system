package com.pahanaEdu.service;

import com.pahanaEdu.dao.UserDAO;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        return userDAO.validate(username, password);
    }


    public boolean validateCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.length() < 6) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    public com.pahanaEdu.model.User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.getUserByUsername(username);
    }
}
