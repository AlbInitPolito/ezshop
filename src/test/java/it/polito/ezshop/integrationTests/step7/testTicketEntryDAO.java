package it.polito.ezshop.integrationTests.step7;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class testTicketEntryDAO {

    //TODO:

    /*
     * addTicketEntry
     */

    @Test
    public void testAddTicketEntryDriver() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        DbConnection db = DbConnection.getInstance();
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntry(null, null));
        TicketEntry t = new TicketEntryModel();
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntry(t, null));
        t.setBarCode("012345678901234");
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntry(t, null));
        assertThrows(InvalidDAOParameterException.class, () -> tdao.addTicketEntry(t, 0));
        t.setBarCode("5555555555");
        assertThrows(InvalidDAOParameterException.class, () -> tdao.addTicketEntry(t, 0));
        assertThrows(MissingDAOParameterException.class, () -> tdao.addTicketEntry(t,1));

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
        query = "DELETE FROM product_type WHERE barcode='5555555555'";
        db.executeUpdate(query);

        try {
            t.setAmount(5);
            assert(tdao.addTicketEntry(t, 1)==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '5555555555', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        TicketEntry te = null;
        try{
            t.setAmount(5);
            te=tdao.addTicketEntry(t, Integer.parseInt(opquery[0]));
        }catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(te!=null);

        try{
            t.setAmount(7);
            te=tdao.addTicketEntry(t, Integer.parseInt(opquery[0]));
        }catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(te!=null);

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
        query = "DELETE FROM product_type WHERE barcode='3333333333'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '3333333333', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try{
            t.setBarCode("3333333333");
            t.setAmount(5);
            t.setDiscountRate(-1);
            te=tdao.addTicketEntry(t, Integer.parseInt(opquery[0]));
        }catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(te!=null);

    }
}
