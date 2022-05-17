package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.TicketEntryRfidDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import static org.junit.Assert.*;

public class testTicketEntryRfidDAO {

    @Test
    public void addTicketEntryRfid() {
        TicketEntryRfidDAO tdao = new TicketEntryRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntryRfid(null, null));
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntryRfid("0123456789012", null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.addTicketEntryRfid("0123456789012", 0));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.addTicketEntryRfid("1", 0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
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
        try {
            assert (tdao.addTicketEntryRfid("1", Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void removeTicketEntryRfid() {
        TicketEntryRfidDAO tdao = new TicketEntryRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> tdao.removeTicketEntryRfid(null, null));
        assertThrows(MissingDAOParameterException.class, () -> tdao.removeTicketEntryRfid("0123456789012", null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.removeTicketEntryRfid("0123456789012", 0));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.removeTicketEntryRfid("1", 0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
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
        query = "INSERT INTO product_in_transaction_rfid VALUES(" + opquery[0] + ",'1');";
        db.executeUpdate(query);
        try {
            assert (tdao.removeTicketEntryRfid("1", Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void getSaleTicketEntriesRfid() {
        TicketEntryRfidDAO tdao = new TicketEntryRfidDAO();
        assertThrows(MissingDAOParameterException.class, () -> tdao.getSaleTicketEntriesRfid(null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.getSaleTicketEntriesRfid( 0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
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
        query = "INSERT INTO product_in_transaction_rfid VALUES(" + opquery[0] + ",'" + 1 + "');";
        db.executeUpdate(query);
        try {
            assert (tdao.getSaleTicketEntriesRfid(Integer.parseInt(opquery[0])).size() != 0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            fail();
        }
    }

    @Test
    public void resetTicketEntryRfid() {
        TicketEntryRfidDAO tdao = new TicketEntryRfidDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
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
        query = "INSERT INTO product_in_transaction_rfid VALUES(" + opquery[0] + ",'" + 1 + "');";
        db.executeUpdate(query);
        assert (tdao.resetTicketEntryRfid());
        query = "SELECT * FROM product_in_transaction_rfid;";
        assert (db.executeQuery(query).size()==0);
    }
}
