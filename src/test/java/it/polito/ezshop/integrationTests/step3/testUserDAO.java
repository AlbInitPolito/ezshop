package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.UserDAO;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testUserDAO {

    @Test
    public void testAddUser() {
        UserDAO udao = new UserDAO();
        assertThrows(MissingDAOParameterException.class, () -> udao.addUser(null));
        User user = new UserModel(null, null, null);
        assertThrows(MissingDAOParameterException.class, () -> udao.addUser(user));
        user.setUsername("12345 12345 12345 12345 12345 12345 12345 12345");
        assertThrows(MissingDAOParameterException.class, () -> udao.addUser(user));
        user.setPassword("12345 12345 12345 12345 12345 12345 12345 12345");
        assertThrows(MissingDAOParameterException.class, () -> udao.addUser(user));
        user.setRole("Administrator");
        assertThrows(InvalidDAOParameterException.class, () -> udao.addUser(user));
        user.setUsername("testadduser");
        assertThrows(InvalidDAOParameterException.class, () -> udao.addUser(user));
        user.setPassword("testadduser");

        try {
            DbConnection db = DbConnection.getInstance();
            String query = "DELETE FROM user WHERE username='testadduser'";
            db.executeUpdate(query);

            user.setRole("Administrator");
            assert (udao.addUser(user) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveUser() {
        UserDAO udao = new UserDAO();
        assertThrows(MissingDAOParameterException.class, () -> udao.removeUser(null));
        assertThrows(InvalidDAOParameterException.class, () -> udao.removeUser(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM user WHERE username='testremuser'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testremuser', 'testremuser', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (udao.removeUser(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM user WHERE username='testremuser'";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

    @Test
    public void testGetUsers() {
        UserDAO udao = new UserDAO();
        List<User> users = udao.getUsers();
        assert (users != null);

        DbConnection db = DbConnection.getInstance();

        String query = "DELETE FROM user";
        db.executeUpdate(query);
        assert (udao.getUsers().size() == 0);

        query = "DELETE FROM user WHERE username='testgetusers'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetusers', 'testgetusers', 'Administrator');";
        db.executeUpdate(query);
        assert (udao.getUsers().size() > 0);
    }

    @Test
    public void testGetUsersById() {
        UserDAO udao = new UserDAO();
        assertThrows(MissingDAOParameterException.class, () -> udao.getUserById(null));
        assertThrows(InvalidDAOParameterException.class, () -> udao.getUserById(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM user";
        db.executeUpdate(query);
        try {
            assert (udao.getUserById(1)==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM user WHERE username='testgetuserbyid'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetuserbyid', 'testgetuserbyid', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (udao.getUserById(Integer.parseInt(opquery[0]))!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserByUsername(){
        UserDAO udao = new UserDAO();
        assertThrows(MissingDAOParameterException.class, () -> udao.getUserByUsername(null));
        assertThrows(InvalidDAOParameterException.class, () -> udao.getUserByUsername("12345 12345 12345 12345 12345 12345 12345 12345"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM user";
        db.executeUpdate(query);
        try {
            assert (udao.getUserByUsername("any")==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM user WHERE username='testgetuserbyname'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetuserbyname', 'testgetuserbyname', 'Administrator');";
        db.executeUpdate(query);

        try {
            assert (udao.getUserByUsername("testgetuserbyname")!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateUser(){
        UserDAO udao = new UserDAO();
        assertThrows(MissingDAOParameterException.class, () -> udao.updateUser(null));
        User u = new UserModel(null, null, null);
        assertThrows(MissingDAOParameterException.class, () -> udao.updateUser(u));
        u.setId(0);
        assertThrows(InvalidDAOParameterException.class, () -> udao.updateUser(u));
        u.setId(1);
        assertThrows(MissingDAOParameterException.class, () -> udao.updateUser(u));
        u.setRole("Administrator");

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM user WHERE username='testupdateuser'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testupdateuser', 'testupdateuser', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            u.setId(Integer.parseInt(opquery[0]));
            u.setRole("Cashier");
            assert (udao.updateUser(u));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetCustomers() {
        UserDAO udao = new UserDAO();
        assert (udao.resetUsers());

        DbConnection db = DbConnection.getInstance();
        String query = "SELECT * FROM user";
        List<String[]> result = db.executeQuery(query);

        assert (result.size() == 0);
    }
}
