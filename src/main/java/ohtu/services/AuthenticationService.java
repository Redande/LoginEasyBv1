package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;

public class AuthenticationService {

    private UserDao userDao;
    private boolean userInList;

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
        this.userInList = false;
    }

    public boolean logIn(String username, String password) {
        userInList = false;
        for (User user : userDao.listAll()) {
            userFound(user, username, password);
        }
        return userInList;
    }
    
    public void userFound(User user, String username, String password) {
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            userInList = true;
        }
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null || invalid(username, password)) {
            return false;
        }
        userDao.add(new User(username, password));
        return true;
    }

    private boolean invalid(String username, String password) {
        return invalidUsername(username) || invalidPassword(password);
    }
    
    private boolean invalidUsername(String username) {
        return username.matches(".*[^a-z].*") || username.length() < 3;
    }
    
    private boolean invalidPassword(String password) {
        return !password.matches(".*[^a-zA-Z].*") || password.length() < 8;
    }
}
