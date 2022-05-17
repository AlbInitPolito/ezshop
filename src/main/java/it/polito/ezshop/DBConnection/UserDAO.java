package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.User;
import it.polito.ezshop.data.UserDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends mysqlDAO implements UserDAOInterface {

    public UserDAO() {
        super();
    }

    @Override
    public User addUser(User user) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (user == null)
            throw new MissingDAOParameterException("User object is required" +
                    " in addUser in UserDAO");
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();
        if (username == null)
            throw new MissingDAOParameterException("User.username is required" +
                    " in addUser in UserDAO");
        if (password == null)
            throw new MissingDAOParameterException("User.password is required" +
                    " in addUser in UserDAO");
        if (role == null)
            throw new MissingDAOParameterException("User.role is required" +
                    " in addUser in UserDAO");
        if (username.length() > 30)
            throw new InvalidDAOParameterException("User.username length cannot be > 30" +
                    " in addUser in UserDAO \n Given instead: " + username);
        if (password.length() > 30)
            throw new InvalidDAOParameterException("User.password length cannot be > 30" +
                    " in addUser in UserDAO \n Given instead: " + password);
        if (role.length() > 13)
            throw new InvalidDAOParameterException("User.role length cannot be > 13" +
                    " in addUser in UserDAO \n Given instead: " + password);
        User u = null;
        String query = "INSERT INTO user VALUES(null, '" + username + "', '" + password + "', '" + role + "');";
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            if (opquery == null) return null;
            opquery = (db.executeQuery("SELECT * FROM user WHERE id=" + opquery[0])).get(0);
            if (opquery == null) return null;
            u = new UserModel(opquery[1], opquery[2], opquery[3]);
            u.setId(Integer.valueOf(opquery[0]));
        }

        return u;
    }

    @Override
    public boolean removeUser(Integer userId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (userId == null)
            throw new MissingDAOParameterException("userId is required" +
                    " in removeUser in UserDAO");
        if (userId <= 0)
            throw new InvalidDAOParameterException("userId must be > 0" +
                    " in removeUser in UserDAO \n Given instead: " + userId);
        String query = "DELETE FROM user WHERE id=" + userId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<User> getUsers() {
        String query = "SELECT * FROM user";
        List<String[]> result = db.executeQuery(query);
        List<User> users = new ArrayList<>();
        User u;
        int id;
        String username;
        String password;
        String role;
        for (String[] tuple : result) {
            id = Integer.parseInt(tuple[0]);
            username = tuple[1];
            password = tuple[2];
            role = tuple[3];
            u = new UserModel(username, password, role);
            u.setId(id);
            users.add(u);
        }
        return users;
    }

    @Override
    public User getUserById(Integer userId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (userId == null)
            throw new MissingDAOParameterException("userId is required" +
                    " in getUserById in UserDAO");
        if (userId <= 0)
            throw new InvalidDAOParameterException("userId must be > 0" +
                    " in getUserById in UserDAO \n Given instead: " + userId);
        String query = "SELECT * FROM user WHERE id=" + userId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        int uid = Integer.parseInt(tuple[0]);
        String username = tuple[1];
        String password = tuple[2];
        String role = tuple[3];
        User u = new UserModel(username, password, UserModel.Role.valueOf(role).name());
        u.setId(uid);
        return u;
    }

    @Override
    public User getUserByUsername(String username) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (username == null)
            throw new MissingDAOParameterException("username is required" +
                    " in getUserByUsername in UserDAO");
        if (username.length() > 30)
            throw new InvalidDAOParameterException("userId length cannot be > 30" +
                    " in getUserByUsername in UserDAO \n Given instead: " + username);
        String query = "SELECT * FROM user WHERE username='" + username + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        int uid = Integer.parseInt(tuple[0]);
        String usern = tuple[1];
        String password = tuple[2];
        String role = tuple[3];
        User u = new UserModel(usern, password, role);
        u.setId(uid);
        return u;
    }

    @Override
    public boolean updateUser(User user) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (user == null)
            throw new MissingDAOParameterException("User object is required" +
                    " in updateUser in UserDAO");
        Integer uid = user.getId();
        String role = user.getRole();
        if (uid == null)
            throw new MissingDAOParameterException("user.id is required" +
                    " in updateUser in UserDAO");
        if (uid <= 0)
            throw new InvalidDAOParameterException("user.id must be > 0" +
                    " in updateUser in UserDAO \n Given instead: " + uid);
        if (role == null)
            throw new MissingDAOParameterException("user.role is required" +
                    " in updateUser in UserDAO");
        if (role.length() > 13)
            throw new InvalidDAOParameterException("user.role length cannot be > 13" +
                    " in updateUser in UserDAO \n Given instead: " + uid);
        String query = "UPDATE user SET role='" + user.getRole() + "' WHERE id=" + user.getId() + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean resetUsers() {
        String query = "DELETE FROM user;";
        return db.executeUpdate(query);
    }
}
