package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testProductTypeDAO {

    @Test
    public void testAddProductTypeDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='98765432109876'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '98765432109876', 'description', 15.5, 23, 'note', null, null, null);";
        boolean result = db.executeUpdate(query);
        assert (result);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        assert (opquery != null);
        opquery = db.executeQuery("SELECT * FROM product_type WHERE id=" + opquery[0] + ";").get(0);
        assert (opquery != null);
    }

    @Test
    public void testRemoveProductTypeDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='98765432109876'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '98765432109876', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "DELETE FROM product_type WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testUpdateProductTypeDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='11112222333344'";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='98765432109876'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '98765432109876', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE product_type SET barcode='11112222333344', pricePerUnit=22.0, description='newdesc', quantity=80, " +
                "notes='newnote', aisleID=4, rackID='c', levelID=5 WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetProductsDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='12344321123443'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12344321123443', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "SELECT * FROM product_type";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetProductByIdDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='12344321123443'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12344321123443', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "SELECT * FROM product_type WHERE id=" + opquery[0];
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testGetProductByBarcodeDriver() {
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
        String query = "DELETE FROM product_type WHERE barcode='12344321123443'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12344321123443', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "SELECT * FROM product_type WHERE barcode='12344321123443'";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testResetProductTypesDriver() {
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

        String query = "DELETE FROM return_product_rfid";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry";
        db.executeUpdate(query);
        query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type;";
        assert (db.executeUpdate(query));
    }
}
