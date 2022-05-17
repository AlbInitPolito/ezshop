package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.UserDAO;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUserModelStep5 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query = "DELETE FROM user";

    @Test
    public void testCreateUser(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try {
            Integer id1 = UserModel.createUser("testUsername1", "testPassword1", "Cashier");
            Integer id2 = UserModel.createUser("testUsername2", "testPassword2", "ShopManager");
            Integer id3 = UserModel.createUser("testUsername3", "testPassword3", "Administrator");

            User result = udao.getUserById(id1);
            assertNotNull(result);
            assert(result.getId().equals(id1));
            assertEquals(result.getUsername(), "testUsername1");
            assertEquals(result.getPassword(), "testPassword1");
            assertEquals(result.getRole(), "Cashier");

            result = udao.getUserById(id2);
            assertNotNull(result);
            assert(result.getId().equals(id2));
            assertEquals(result.getUsername(), "testUsername2");
            assertEquals(result.getPassword(), "testPassword2");
            assertEquals(result.getRole(), "ShopManager");

            result = udao.getUserById(id3);
            assertNotNull(result);
            assert(result.getId().equals(id3));
            assertEquals(result.getUsername(), "testUsername3");
            assertEquals(result.getPassword(), "testPassword3");
            assertEquals(result.getRole(), "Administrator");

            assertThrows(InvalidRoleException.class, () -> UserModel.createUser("testUsername", "testPassword", null));
            assertThrows(InvalidUsernameException.class, () -> UserModel.createUser(null, "testPassword", "Cashier"));
            assertThrows(InvalidUsernameException.class, () -> UserModel.createUser("", "testPassword", "Cashier"));
            assertThrows(InvalidPasswordException.class, () -> UserModel.createUser("testUsername", null, "Cashier"));
            assertThrows(InvalidPasswordException.class, () -> UserModel.createUser("testUsername", "", "Cashier"));
            assertThrows(InvalidRoleException.class, () -> UserModel.createUser("testUsername", "testPassword", ""));
            assertThrows(InvalidRoleException.class, () -> UserModel.createUser("testUsername", "testPassword", null));
            assertThrows(InvalidRoleException.class, () -> UserModel.createUser("testUsername", "testPassword", "invalidRole"));
            assert(UserModel.createUser("qwertyuioplkjhgfdsazxcvbnmqwert", "testPassword", "Cashier") == -1);
            assert(UserModel.createUser("testUsername", "qwertyuioplkjhgfdsazxcvbnmqwert", "Cashier") == -1);
            assert(UserModel.createUser("testUsername1", "testPassword1", "Cashier") == -1);

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetUser(){
        dbConnection.executeUpdate(query);
        try {
            assertThrows(InvalidUserIdException.class, () -> UserModel.getUser(-1));
            assertThrows(InvalidUserIdException.class, () -> UserModel.getUser(null));
            assertNull(UserModel.getUser(123));
            dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername', 'testPassword', 'Cashier')");
            User result = UserModel.getUser(123);
            assertNotNull(result);
            assert(result.getId().equals(123));
            assertEquals(result.getUsername(), "testUsername");
            assertEquals(result.getPassword(), "testPassword");
            assertEquals(result.getRole(), "Cashier");
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetAllUsers(){
        dbConnection.executeUpdate(query);
        assert(UserModel.getAllUsers().size() == 0);
        dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword1', 'Cashier')");
        dbConnection.executeUpdate("INSERT INTO user VALUES (124, 'testUsername2', 'testPassword2', 'ShopManager')");
        dbConnection.executeUpdate("INSERT INTO user VALUES (125, 'testUsername3', 'testPassword3', 'Administrator')");
        assert(UserModel.getAllUsers().size() == 3);

        dbConnection.executeUpdate(query);
    }

    @Test
    public void testDeleteUser(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try {
            dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword1', 'Cashier')");
            assertThrows(InvalidUserIdException.class, () -> UserModel.deleteUser(null));
            assertThrows(InvalidUserIdException.class, () -> UserModel.deleteUser(-1));
            assertNotNull(udao.getUserById(123));
            assertTrue(UserModel.deleteUser(123));
            assertNull(udao.getUserById(123));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateUserRights(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try {
            dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword1', 'Cashier')");
            User result = udao.getUserById(123);
            assertNotNull(result);
            assertEquals(result.getRole(), "Cashier");
            assertThrows(InvalidUserIdException.class, () -> UserModel.updateUserRights(null, "Administrator"));
            assertThrows(InvalidUserIdException.class, () -> UserModel.updateUserRights(-1, "Administrator"));
            assertThrows(InvalidRoleException.class, () -> UserModel.updateUserRights(123, "invalidRole"));
            assertFalse(UserModel.updateUserRights(122, "Administrator"));
            assertTrue(UserModel.updateUserRights(123, "Administrator"));
            result = udao.getUserById(123);
            assertEquals(result.getRole(), "Administrator");
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testLogin(){
        UserDAO udao = new UserDAO();
        dbConnection.executeUpdate(query);
        try{
            dbConnection.executeUpdate("INSERT INTO user VALUES (123, 'testUsername1', 'testPassword1', 'Cashier')");
            assertNull(UserModel.login("wrongUsername", "testPassword"));
            assertNull(UserModel.login("testUsername1", "wrongPassword"));
            User result = UserModel.login("testUsername1", "testPassword1");
            assertNotNull(result);
            assertEquals(result.getUsername(), "testUsername1");
            assertEquals(result.getPassword(), "testPassword1");
            assertEquals(result.getRole(), "Cashier");

            assertThrows(InvalidUsernameException.class, () -> UserModel.login(null, "testPassword1"));
            assertThrows(InvalidUsernameException.class, () -> UserModel.login("", "testPassword1"));
            assertNull(UserModel.login("qwertyuioplkjhgfdsazxcvbnmqwert", "testPassword1"));
            assertThrows(InvalidPasswordException.class, () -> UserModel.login("testUsername1", null));
            assertThrows(InvalidPasswordException.class, () -> UserModel.login("testUsername1", ""));
            assertNull(UserModel.login("testUsername1", "qwertyuioplkjhgfdsazxcvbnmqwert"));

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }


}
