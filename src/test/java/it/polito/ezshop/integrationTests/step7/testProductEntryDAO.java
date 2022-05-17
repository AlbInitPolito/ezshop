package it.polito.ezshop.integrationTests.step7;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import static org.junit.Assert.*;

public class testProductEntryDAO {

    @Test
    public void testAddProductEntryDriver() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.addProductEntry(null, null));
        assertThrows(MissingDAOParameterException.class, () -> pdao.addProductEntry("012345678901234", null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductEntry("012345678901234", "0123456789012"));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductEntry("0123456789", "0123456789012"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE id=1";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        try {
            assert(pdao.addProductEntry("3336669990", "1"));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testGetProductEntries() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.getProductEntries(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.getProductEntries("012345678901234"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE id=1";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        try {
            assert(pdao.getProductEntries("3336669990").size()==0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        try {
            assert(pdao.getProductEntries("3336669990").size()!=0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testGetProductEntryBarcode() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.getProductEntryBarcode(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.getProductEntryBarcode("0123456789012"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE id=1";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        try {
            assert(pdao.getProductEntryBarcode("10")==null);
            assert(pdao.getProductEntryBarcode("1")!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testGetAvailableProductEntries() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.getProductEntryAvailability(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.getProductEntryAvailability("0123456789012"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE id=1";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 1);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(2, 1, 0);";
        db.executeUpdate(query);
        try {
            assert(pdao.getProductEntryAvailability("1")==1);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }


}
