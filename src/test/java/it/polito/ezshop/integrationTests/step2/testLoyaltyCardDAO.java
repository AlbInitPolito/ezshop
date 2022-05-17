package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testLoyaltyCardDAO {

    @Test
    public void testAddCardDriver() {
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
        String query = "DELETE FROM loyalty_card WHERE serial_number='testaddcar'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('testaddcar', 0);";
        boolean result = db.executeUpdate(query);
        assert (result);
    }

    @Test
    public void testUpdateCardDriver() {
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
        String query = "DELETE FROM loyalty_card WHERE serial_number='testcarupd'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('testcarupd', 0);";
        db.executeUpdate(query);
        query = "UPDATE loyalty_card SET points=50 WHERE serial_number='testcarup';";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetCardDriver() {
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
        String query = "DELETE FROM loyalty_card WHERE serial_number='testgetcar'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('testgetcar', 0);";
        db.executeUpdate(query);
        query = "SELECT * FROM loyalty_card WHERE serial_number='testgetcar'";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testResetLoyaltyCardsDriver() {
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
        String query = "DELETE FROM customer;";
        db.executeUpdate(query);
        query = "DELETE FROM loyalty_card";
        assert (db.executeUpdate(query));
    }
}
