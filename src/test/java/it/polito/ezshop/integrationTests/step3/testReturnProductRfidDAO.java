package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnProductRfidDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import static org.junit.Assert.*;

public class testReturnProductRfidDAO {

    @Test
    public void testAddReturnProductRfid() {
        ReturnProductRfidDAO rdao = new ReturnProductRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProductRfid(null, null));
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProductRfid(0, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnProductRfid(0, "0123456789012"));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnProductRfid(1, "0123456789012"));

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
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        try {
            assert (rdao.addReturnProductRfid(Integer.parseInt(opquery[0]), "1"));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testGetReturnProductRfids() {
        ReturnProductRfidDAO rdao = new ReturnProductRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnProductRfids(null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnProductRfids(0));

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
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        try {
            assert (rdao.getReturnProductRfids(Integer.parseInt(opquery[0])).size()!=0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testRemoveReturnProductRfid() {
        ReturnProductRfidDAO rdao = new ReturnProductRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.removeReturnProductRfid(null, null));
        assertThrows(MissingDAOParameterException.class, () -> rdao.removeReturnProductRfid(0, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.removeReturnProductRfid(0, "0123456789012"));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.removeReturnProductRfid(1, "0123456789012"));

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
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        try {
            assert (rdao.removeReturnProductRfid(Integer.parseInt(opquery[0]),"1"));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void testResetReturnProductRfid() {
        ReturnProductRfidDAO rdao = new ReturnProductRfidDAO();
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
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        assert (rdao.resetReturnProductRfid());
        query = "SELECT * FROM return_product_rfid";
        assert(db.executeQuery(query).size()==0);
    }
}
