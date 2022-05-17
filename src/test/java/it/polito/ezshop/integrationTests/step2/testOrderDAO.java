package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testOrderDAO {

    @Test
    public void testUpdateOrderDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String product = opquery[0];
        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate())";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String balance = opquery[0];
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', " + balance + ", " + product + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE product_order SET status='PAYED' WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testResetOrdersDriver() {
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
        String query = "DELETE FROM product_order;";
        assert (db.executeUpdate(query));
    }
}
