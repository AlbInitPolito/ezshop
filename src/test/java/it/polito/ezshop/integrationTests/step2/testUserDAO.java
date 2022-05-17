package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testUserDAO {

    @Test
    public void testAddUserDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testusername', 'testpassword', 'Administrator');";
        boolean result = db.executeUpdate(query);
        assert (result);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        assert (opquery != null);
        opquery = db.executeQuery("SELECT * FROM user WHERE id=" + opquery[0] + ";").get(0);
        assert (opquery != null);
    }

    @Test
    public void testRemoveUserDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testdelusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testdelusername', 'testpassword', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "DELETE FROM user WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testUpdateUserDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testupdusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testupdusername', 'testpassword', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE user SET role='Administrator' WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetUserByIdDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testgetusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetusername', 'testpassword', 'Administrator');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "SELECT * FROM user WHERE id=" + opquery[0];
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetUserByUsernameDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testgetusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetusername', 'testpassword', 'Administrator');";
        db.executeUpdate(query);
        query = "SELECT * FROM user WHERE username='testgetusername'";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetUsersDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user WHERE username='testgetusername'";
        db.executeUpdate(query);
        query = "INSERT INTO user VALUES(null, 'testgetusername', 'testpassword', 'Administrator');";
        db.executeUpdate(query);
        query = "SELECT * FROM user";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testResetUsersDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM user;";
        assert (db.executeUpdate(query));
    }
}
