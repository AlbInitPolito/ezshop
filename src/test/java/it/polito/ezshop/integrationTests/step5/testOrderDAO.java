package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.OrderDAO;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.OrderModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class testOrderDAO {

    @Test
    public void testAddOrder() {
        OrderDAO odao = new OrderDAO();
        assertThrows(MissingDAOParameterException.class, () -> odao.addOrder(null));
        Order o = new OrderModel();
        assertThrows(MissingDAOParameterException.class, () -> odao.addOrder(o));
        o.setProductCode("012345678901234");
        assertThrows(InvalidDAOParameterException.class, () -> odao.addOrder(o));
        o.setProductCode("098765432109");
        assertThrows(MissingDAOParameterException.class, () -> odao.addOrder(o));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='098765432109'";
        db.executeUpdate(query);

        o.setPricePerUnit(13);
        try {
            assert (odao.addOrder(o) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO product_type VALUES(null, '098765432109', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);

        assertThrows(MissingDAOParameterException.class, () -> odao.addOrder(o));
        o.setQuantity(29);
        try {
            assert (odao.addOrder(o) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOrder() {
        OrderDAO odao = new OrderDAO();
        assertThrows(MissingDAOParameterException.class, () -> odao.getOrder(null));
        assertThrows(InvalidDAOParameterException.class, () -> odao.getOrder(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        try {
            assert (odao.getOrder(1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_type WHERE barcode='112233445566'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '112233445566', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', null, " + opquery[0] + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (odao.getOrder(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery2 = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE product_order SET balance_operation=" + opquery2[0] + " WHERE id=" + opquery[0];
        db.executeUpdate(query);

        try {
            assert (odao.getOrder(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOrders() {
        OrderDAO odao = new OrderDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        assert (odao.getOrders().size() == 0);

        query = "DELETE FROM product_type WHERE barcode='112233445500'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '112233445500', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', null, " + opquery[0] + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        assert (odao.getOrders().size() != 0);

        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery2 = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE product_order SET balance_operation=" + opquery2[0] + " WHERE id=" + opquery[0];
        db.executeUpdate(query);

        assert (odao.getOrders().size() != 0);
    }
}
