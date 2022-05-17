package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testCustomerDAO {

    @Test
    public void testAddCustomerDriver() {
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
        String query = "DELETE FROM customer WHERE name='testname'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testname', null);";
        boolean result = db.executeUpdate(query);
        assert (result);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        assert (opquery != null);
        opquery = db.executeQuery("SELECT * FROM customer WHERE id=" + opquery[0] + ";").get(0);
        assert (opquery != null);
    }

    @Test
    public void testRemoveCustomerDriver() {
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
        String query = "DELETE FROM customer WHERE name='testnameremove'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testnameremove', null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "DELETE FROM customer WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testUpdateCustomerDriver() {
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
        String query = "DELETE FROM customer WHERE name='testnameupdate'";
        db.executeUpdate(query);
        query = "DELETE FROM customer WHERE name='newtestname'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testnameupdate', null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE customer SET name='newtestname' WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetCustomersDriver() {
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
        String query = "DELETE FROM customer WHERE name='testnamegetcustomers'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testnamegetcustomers', null);";
        db.executeUpdate(query);
        query = "SELECT * FROM customer";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetCustomerDriver() {
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
        String query = "DELETE FROM customer WHERE name='testnamegetcustomer'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testnamegetcustomer', null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "SELECT * FROM customer WHERE id=" + opquery[0];
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetCustomerByCardDriver() {
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
        String query = "DELETE FROM customer WHERE name='testnamegetcustomerbycard'";
        db.executeUpdate(query);
        query = "DELETE FROM loyalty_card WHERE serial_number='provacard'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('provacard', 0)";
        db.executeUpdate(query);

        query = "INSERT INTO customer VALUES(null, 'testnamegetcustomerbycard', 'provacard');";
        db.executeUpdate(query);
        query = "SELECT * FROM customer WHERE loyalty_card='provacard'";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testResetCustomersDriver() {
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
        assert (db.executeUpdate(query));
    }
}
