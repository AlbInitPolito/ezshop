package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.UserDAO;
import it.polito.ezshop.data.User;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserModelDriversStep4 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query = "DELETE FROM user";
    UserModel user = new UserModel("testUsername", "testPassword", "Cashier");


    @Test
    public void testCreateUserDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try{
            User result = udao.addUser(user);
            assertNotNull(result);
            assertEquals(result.getUsername(), "testUsername");
            assertEquals(result.getPassword(), "testPassword");
            assertEquals(result.getRole(), "Cashier");
            assertNull(udao.addUser(user));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetUserDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try {
            assertNull(udao.getUserById(123));
            dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername', 'testPassword', 'Administrator')");
            User result = udao.getUserById(123);
            assertEquals(result.getUsername(), "testUsername");
            assertEquals(result.getPassword(), "testPassword");
            assertEquals(result.getRole(), "Administrator");
            assertNull(udao.getUserById(122));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetAllUsersDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        assert(udao.getUsers().size() == 0);
        dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword', 'Administrator')");
        dbConnection.executeUpdate("INSERT INTO user VALUES (124, 'testUsername2', 'testPassword', 'Administrator')");
        dbConnection.executeUpdate("INSERT INTO user VALUES (125, 'testUsername3', 'testPassword', 'Administrator')");
        assert(udao.getUsers().size() == 3);
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testDeleteUserDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword', 'Administrator')");
        try {
            assert(dbConnection.executeQuery("SELECT * FROM user WHERE id=123").size() == 1);
            assertTrue(udao.removeUser(123));
            assert(dbConnection.executeQuery("SELECT * FROM user WHERE id=123").size() == 0);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateUserRightsDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword', 'Cashier')");
        try{
            User user = udao.getUserById(122);
            assertNull(user);
            user = udao.getUserById(123);
            assertNotNull(user);
            user.setUsername(null);
            user.setPassword(null);
            user.setRole("ShopManager");
            assertTrue(udao.updateUser(user));
            user = udao.getUserById(123);
            assertNotNull(user);
            assertEquals(user.getUsername(), "testUsername1");
            assertEquals(user.getPassword(), "testPassword");
            assertEquals(user.getRole(), "ShopManager");

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testLoginDriver(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername', 'testPassword', 'Cashier')");
        try {
            User user = udao.getUserByUsername("wrongUsername");
            assertNull(user);
            user = udao.getUserByUsername("testUsername");
            assertNotNull(user);
            assert(user.getId() == 123);
            assertEquals(user.getUsername(), "testUsername");
            assertEquals(user.getPassword(), "testPassword");
            assertEquals(user.getRole(), "Cashier");
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }
}

