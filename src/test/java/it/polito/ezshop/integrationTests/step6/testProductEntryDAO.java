package it.polito.ezshop.integrationTests.step6;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class testProductEntryDAO {

    @Test
    public void testAddProductEntryDriver() {
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
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p=null;
        try {
            p = pdao.getProductByBarcode("3336669990");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
        query = "INSERT INTO product_entry VALUES(1, " + p.getId() + ", 0);";
        boolean result = db.executeUpdate(query);
        assert (result);
    }

    @Test
    public void testGetProductEntriesDriver() {
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
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p=null;
        try {
            p = pdao.getProductByBarcode("3336669990");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
        query = "SELECT rfid FROM product_entry WHERE product_type=" + p.getId() + ";";
        List<String[]> result = db.executeQuery(query);
        assert(result.size()==1);
    }

    @Test
    public void testGetProductEntryBarcodeDriver() {
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
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "SELECT product_type FROM product_entry WHERE rfid=1;";
        List<String[]> result = db.executeQuery(query);
        String pid = result.get(0)[0];
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p=null;
        try {
            p = pdao.getProductById(Integer.parseInt(pid));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
        assert(p.getBarCode().equals("3336669990"));
    }

    @Test
    public void testGetAvailableProductEntriesDriver() {
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
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 1);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(2, 1, 0);";
        db.executeUpdate(query);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p=null;
        try {
            p = pdao.getProductByBarcode("3336669990");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
        query = "SELECT rfid FROM product_entry WHERE product_type=" + p.getId() + " AND available=1;";
        List<String[]> result = db.executeQuery(query);
        assert(result.size()==1);
    }
}


