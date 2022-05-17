package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import static org.junit.Assert.*;

public class testProductEntryDAO {

    @Test
    public void testUpdateProductEntry() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.updateProductEntry(null, true));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductEntry("0123456789012", true));
        String query = "DELETE FROM product_entry WHERE rfid=1";
        DbConnection db = DbConnection.getInstance();
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        try {
            assert (pdao.updateProductEntry("1", true));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testGetProductEntryAvailability() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.updateProductEntry(null, true));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductEntry("0123456789012", true));
        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 1);";
        db.executeUpdate(query);
        try {
            assert (pdao.getProductEntryAvailability("1")==1);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testResetProductEntries() {
        ProductEntryDAO pdao = new ProductEntryDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction_rfid";
        db.executeUpdate(query);
        assert(pdao.resetProductEntries());
    }
}
