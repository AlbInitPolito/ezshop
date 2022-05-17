package it.polito.ezshop.integrationTests.step7;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnProductDAO;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class testReturnProductDAO {

    @Test
    public void testAddReturnProductDriver() {
        ReturnProductDAO rdao = new ReturnProductDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProduct(null, null));
        TicketEntry t = new TicketEntryModel();
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProduct(t, null));
        t.setBarCode("012345678901234");
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProduct(t, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnProduct(t, 0));
        t.setBarCode("0987609876");
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnProduct(t, 0));
        t.setAmount(0);
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnProduct(t, 1));

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
        query = "DELETE FROM product_type WHERE barcode='0987609876'";
        db.executeUpdate(query);

        t.setAmount(5);
        try {
            assert (rdao.addReturnProduct(t, 1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '0987609876', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);
        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (rdao.addReturnProduct(t, Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        t.setAmount(7);
        try {
            assert (rdao.addReturnProduct(t, Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
