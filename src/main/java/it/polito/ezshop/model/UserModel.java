package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.UserDAO;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import java.util.List;

public class UserModel implements it.polito.ezshop.data.User {

    public enum Role {
        Cashier,
        ShopManager,
        Administrator
    }

    private String username;
    private String password;
    private Role role;
    private Integer id;

    public UserModel(String username, String password, String role) {
        this.username = username;
        this.password = password;
        if (role != null)
            this.role = Role.valueOf(role);
        else
            this.role = null;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        if (this.role == null)
            return null;
        else
            return this.role.name();
    }

    /**
     * This method sets the role of the User to the specified parameter
     *
     * @param role is a String stating the role to be set. The method is case-insensitive, as it converts the parameter
     *             to uppercase in order to refer to the correct enum value
     */
    @Override
    public void setRole(String role) {

        if (role != null)
            this.role = Role.valueOf(role);
        else
            this.role = null;
    }

    /**
     * This method creates a new user with given username, password and role. The returned value is a unique identifier
     * for the new user.
     *
     * @param username the username of the new user. This value should be unique and not empty.
     * @param password the password of the new user. This value should not be empty.
     * @param role     the role of the new user. This value should not be empty and it should assume
     *                 one of the following values : "Administrator", "Cashier", "ShopManager"
     * @return The id of the new user ( > 0 ).
     * -1 if there is an error while saving the user or if another user with the same username exists
     * @throws InvalidUsernameException If the username has an invalid value (empty or null)
     * @throws InvalidPasswordException If the password has an invalid value (empty or null)
     * @throws InvalidRoleException     If the role has an invalid value (empty, null or not among the set of admissible values)
     */
    public static Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        boolean validRole = false;
        if (role == null) {
            System.err.println("Null role");
            throw new InvalidRoleException();
        }

        if (username == null || username.equals("")) {
            System.err.println("Invalid username");
            throw new InvalidUsernameException();
        }
        if(username.length() > 30)
            return -1;

        if (password == null || password.equals("")) {
            System.err.println("Invalid password");
            throw new InvalidPasswordException();
        }
        if(password.length() > 30)
            return -1;

        if (role.equals("")) {
            System.err.println("Invalid role");
            throw new InvalidRoleException();
        }

        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                validRole = true;
                break;
            }
        }

        if (!validRole) {
            System.err.println("Invalid role, must be either Cashier, ShopManager or Administrator");
            throw new InvalidRoleException();
        }

        UserModel user = new UserModel(username, password, role);
        UserDAO udao = new UserDAO();
        try {
            User result = udao.addUser(user);
            if (result == null)
                return -1;
            else
                return result.getId();
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method returns a User object with given id. It can be invoked only after a user with role "Administrator" is
     * logged in.
     *
     * @param id the id of the user
     * @return the requested user if it exists, null otherwise
     * @throws InvalidUserIdException if id is less than or equal to zero or if it is null
     */
    public static User getUser(Integer id) throws InvalidUserIdException {
        if (id == null || id <= 0) {
            System.err.println("Invalid ID");
            throw new InvalidUserIdException();
        }

        UserModel user = new UserModel(" ", " ", "Cashier"); // dummy user to use for query, random parameters, only thing that matters is ID
        user.setId(id);
        UserDAO udao = new UserDAO();
        try {
            return udao.getUserById(user.getId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the list of all registered users. It can be invoked only after a user with role "Administrator" is
     * logged in.
     *
     * @return a list of all registered users. If there are no users the list should be empty
     */
    public static List<User> getAllUsers() {
        UserDAO udao = new UserDAO();
        return udao.getUsers();
    }

    /**
     * This method deletes the user with given id. It can be invoked only after a user with role "Administrator" is
     * logged in.
     *
     * @param id the user id, this value should not be less than or equal to 0 or null.
     * @return true if the user was deleted
     * false if the user cannot be deleted
     * @throws InvalidUserIdException if id is less than or equal to 0 or if it is null.
     */
    public static boolean deleteUser(Integer id) throws InvalidUserIdException {
        if (id == null || id <= 0) {
            System.err.println("Invalid ID");
            throw new InvalidUserIdException();
        }

        UserDAO udao = new UserDAO();
        try {
            User user = udao.getUserById(id);
            if(user == null)
                return false;
            return udao.removeUser(user.getId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method updates the role of a user with given id. It can be invoked only after a user with role "Administrator" is
     * logged in.
     *
     * @param id   the id of the user
     * @param role the new role the user should be assigned to
     * @return true if the update was successful, false if the user does not exist
     * @throws InvalidUserIdException if the user Id is less than or equal to 0 or if it is null
     * @throws InvalidRoleException   if the new role is empty, null or not among one of the following : {"Administrator", "Cashier", "ShopManager"}
     */
    public static boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException {

        boolean validRole = false;

        if (id == null || id <= 0) {
            System.err.println("Invalid ID");
            throw new InvalidUserIdException();
        }

        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                validRole = true;
                break;
            }
        }

        if (!validRole) {
            System.err.println("Invalid role, must be either Cashier, ShopManager or Administrator");
            throw new InvalidRoleException();
        }

        UserDAO udao = new UserDAO();
        try {
            User user = udao.getUserById(id);
            if(user == null)
                return false;
            user.setUsername(null);
            user.setPassword(null);
            user.setRole(role);
            return udao.updateUser(user);
        } catch(MissingDAOParameterException | InvalidDAOParameterException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method lets a user with given username and password login into the system
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return an object of class User filled with the logged user's data if login is successful, null otherwise ( wrong credentials or db problems)
     * @throws InvalidUsernameException if the username is empty or null
     * @throws InvalidPasswordException if the password is empty or null
     */
    public static User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (username == null || username.equals("")) {
            System.err.println("Invalid username");
            throw new InvalidUsernameException();
        }
        if(username.length() > 30)
            return null;

        if (password == null || password.equals("")) {
            System.err.println("Invalid password");
            throw new InvalidPasswordException();
        }
        if(password.length() > 30)
            return null;

        User user = new UserModel(username, " ", "Cashier"); // Random role, not important
        UserDAO udao = new UserDAO();
        try {
            user = udao.getUserByUsername(user.getUsername());
            if(user == null)
                return null;
            else{
                if (user.getPassword().equals(password))
                    return user;
                else
                    return null;
            }
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

}
