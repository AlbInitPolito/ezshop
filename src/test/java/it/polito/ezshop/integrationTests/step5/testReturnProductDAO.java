package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnProductDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class testReturnProductDAO {

    @Test
    public void testRemoveReturnProduct() {
        ReturnProductDAO rdao = new ReturnProductDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.removeReturnProduct(null, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.removeReturnProduct(null, 0));
        assertThrows(MissingDAOParameterException.class, () -> rdao.removeReturnProduct(null, 1));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.removeReturnProduct("012345678901234", 1));

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
        query = "DELETE FROM product_type WHERE barcode='testremret'";
        db.executeUpdate(query);

        try {
            assert (!rdao.removeReturnProduct("testremret", 1));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, 'testremret', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);

        try {
            assert (rdao.removeReturnProduct("testremret", Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateReturnProduct() {
        ReturnProductDAO rdao = new ReturnProductDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnProduct(null, null));
        TicketEntryModel t = new TicketEntryModel();
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnProduct(t, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.updateReturnProduct(t, 0));
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnProduct(t, 1));
        t.setBarCode("012345678901234");
        assertThrows(InvalidDAOParameterException.class, () -> rdao.updateReturnProduct(t, 1));

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
        query = "DELETE FROM product_type WHERE barcode='testupdret'";
        db.executeUpdate(query);

        t.setBarCode("testupdret");
        try {
            assert (!rdao.updateReturnProduct(t, 1));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, 'testupdret', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);

        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnProduct(t, 1));

        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);
        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + product[0] + ", 5);";
        db.executeUpdate(query);

        t.setAmount(77);
        try{
        assert(rdao.updateReturnProduct(t,Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetReturnProduct(){
        ReturnProductDAO rdao = new ReturnProductDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnProduct(null, null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnProduct("012345678901234", null));
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnProduct("0123456789", null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnProduct("0123456789", 0));

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
        query = "DELETE FROM product_type WHERE barcode='testgetret'";
        db.executeUpdate(query);

        try {
            assert (rdao.getReturnProduct("testgetret", 1)==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, 'testgetret', null, 10, null, null, null, null, null);";
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
            assert (rdao.getReturnProduct("testgetret", Integer.parseInt(opquery[0]))==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + product[0] + ", 5);";
        db.executeUpdate(query);

        try {
            assert (rdao.getReturnProduct("testgetret", Integer.parseInt(opquery[0]))!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetReturnProducts(){
        ReturnProductDAO rdao = new ReturnProductDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnProducts(null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnProducts(0));

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
        query = "DELETE FROM product_type WHERE barcode='testgetret'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetret', null, 10, null, null, null, null, null);";
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
            assert (rdao.getReturnProducts(Integer.parseInt(opquery[0])).size()==0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + product[0] + ", 5);";
        db.executeUpdate(query);

        try {
            assert (rdao.getReturnProducts(Integer.parseInt(opquery[0])).size()!=0);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

}
