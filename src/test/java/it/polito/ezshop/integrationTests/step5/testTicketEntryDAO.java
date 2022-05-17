package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class testTicketEntryDAO {

    @Test
    public void testRemoveTicketEntry() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        DbConnection db = DbConnection.getInstance();
        assertThrows(MissingDAOParameterException.class, () -> tdao.removeTicketEntry(null, null));
        TicketEntry t = new TicketEntryModel();
        assertThrows(MissingDAOParameterException.class, () -> tdao.removeTicketEntry(t, null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.removeTicketEntry(t, 0));
        assertThrows(MissingDAOParameterException.class, () -> tdao.removeTicketEntry(t, 1));
        t.setBarCode("012345678901234");
        assertThrows(InvalidDAOParameterException.class, () -> tdao.removeTicketEntry(t, 1));

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='0246802468'";
        db.executeUpdate(query);

        t.setBarCode("0246802468");
        try {
            assert (!tdao.removeTicketEntry(t, 1));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '0246802468', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + ", " + product[0] + ", 10, 0);";
        db.executeUpdate(query);

        try {
            assert (tdao.removeTicketEntry(t, Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateTicketEntryDriver() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        DbConnection db = DbConnection.getInstance();
        assertThrows(MissingDAOParameterException.class, () -> tdao.updateTicketEntry(null, null));
        TicketEntry t = new TicketEntryModel();
        assertThrows(MissingDAOParameterException.class, () -> tdao.updateTicketEntry(t, null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.updateTicketEntry(t, 0));
        assertThrows(MissingDAOParameterException.class, () -> tdao.updateTicketEntry(t, 1));
        t.setBarCode("012345678901234");
        assertThrows(InvalidDAOParameterException.class, () -> tdao.updateTicketEntry(t, 1));


        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='1357913579'";
        db.executeUpdate(query);

        t.setBarCode("1357913579");
        try {
            assert (!tdao.updateTicketEntry(t, 1));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '1357913579', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        t.setDiscountRate(-1);
        assertThrows(MissingDAOParameterException.class, () -> tdao.updateTicketEntry(t, 1));

        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + ", " + product[0] + ", 10, 0);";
        db.executeUpdate(query);

        try {
            t.setAmount(7);
            assert (tdao.updateTicketEntry(t, Integer.parseInt(opquery[0])));
            t.setDiscountRate(0.5);
            assert (tdao.updateTicketEntry(t, Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSaleTicketEntries() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> tdao.getSaleTicketEntries(null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.getSaleTicketEntries(0));

        DbConnection db = DbConnection.getInstance();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='1234512345'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '1234512345', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (tdao.getSaleTicketEntries(Integer.parseInt(opquery[0])).size() == 0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + ", " + product[0] + ", 10, 0);";
        db.executeUpdate(query);

        try {
            assert (tdao.getSaleTicketEntries(Integer.parseInt(opquery[0])).size() != 0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTicketEntryDriver() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        assertThrows(MissingDAOParameterException.class, () -> tdao.getTicketEntry(null, null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.getTicketEntry("012345678901234", null));
        assertThrows(MissingDAOParameterException.class, () -> tdao.getTicketEntry("6789067890", null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.getTicketEntry("6789067890", 0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='6789067890'";
        db.executeUpdate(query);

        try {
            assert (tdao.getTicketEntry("6789067890", 1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '6789067890', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (tdao.getTicketEntry("6789067890", Integer.parseInt(opquery[0])) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + ", " + product[0] + ", 10, 0);";
        db.executeUpdate(query);

        try {
            assert (tdao.getTicketEntry("6789067890", Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
