package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.OrderDAO;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.OrderModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testOrderDAO {

    @Test
    public void testUpdateOrder() {
        OrderDAO odao = new OrderDAO();
        assertThrows(MissingDAOParameterException.class, () -> odao.updateOrder(null));
        Order o = new OrderModel();
        assertThrows(MissingDAOParameterException.class, () -> odao.updateOrder(o));
        o.setOrderId(0);
        assertThrows(InvalidDAOParameterException.class, () -> odao.updateOrder(o));
        o.setOrderId(10);
        assertThrows(MissingDAOParameterException.class, () -> odao.updateOrder(o));
        o.setBalanceId(0);
        assertThrows(InvalidDAOParameterException.class, () -> odao.updateOrder(o));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String product = opquery[0];
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', null, " + product + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        o.setOrderId(Integer.parseInt(opquery[0]));

        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        o.setBalanceId(Integer.parseInt(opquery[0]));
        try {
            assert (odao.updateOrder(o));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        o.setBalanceId(Integer.parseInt(opquery[0]));
        o.setStatus("ISSUED");
        try {
            assert (odao.updateOrder(o));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        o.setBalanceId(null);
        o.setStatus("PAYED");
        try {
            assert (odao.updateOrder(o));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetOrders() {
        OrderDAO odao = new OrderDAO();
        assert (odao.resetOrders());

        DbConnection db = DbConnection.getInstance();
        String query = "SELECT * FROM product_order";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }
}
