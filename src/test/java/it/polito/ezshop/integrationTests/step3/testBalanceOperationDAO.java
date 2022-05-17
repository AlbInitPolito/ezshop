package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.BalanceOperationModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testBalanceOperationDAO {

    @Test
    public void testAddBalanceOperation() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        assertThrows(MissingDAOParameterException.class, () -> bdao.addBalanceOperation(null));
        //id, date, amount, type
        BalanceOperation bop = new BalanceOperationModel(0, null, 0, null);
        assertThrows(MissingDAOParameterException.class, () -> bdao.addBalanceOperation(bop));
        bop.setType("CREDIT");
        assertThrows(MissingDAOParameterException.class, () -> bdao.addBalanceOperation(bop));
        bop.setMoney(30);
        try {
            assert (bdao.addBalanceOperation(bop) != null);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveBalanceOperation() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        BalanceOperation bop;
        assertThrows(MissingDAOParameterException.class, () -> bdao.removeBalanceOperation(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        bop = new BalanceOperationModel(Integer.parseInt(opquery[0]), null, 0, null);

        try {
            assert (bdao.removeBalanceOperation(bop.getBalanceId()));
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM balance_operation WHERE id=" + bop.getBalanceId();
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

    @Test
    public void testUpdateBalanceOperation() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        BalanceOperation bop = new BalanceOperationModel(0, null, 0, null);
        assertThrows(MissingDAOParameterException.class, () -> bdao.updateBalanceOperation(null));
        BalanceOperation finalBop1 = bop;
        assertThrows(MissingDAOParameterException.class, () -> bdao.updateBalanceOperation(finalBop1));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        bop = new BalanceOperationModel(Integer.parseInt(opquery[0]), null, 0, null);

        BalanceOperation finalBop = bop;
        assertThrows(MissingDAOParameterException.class, () -> bdao.updateBalanceOperation(finalBop));
        bop.setMoney(50);
        try {
            assert (bdao.updateBalanceOperation(bop));
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
        bop.setMoney(0);
        bop.setType("CREDIT");
        try {
            assert (bdao.updateBalanceOperation(bop));
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
        bop.setMoney(70);
        bop.setType("SALE");
        try {
            assert (bdao.updateBalanceOperation(bop));
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBalanceOperations() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        List<BalanceOperation> bopl = bdao.getBalanceOperations();
        assert (bopl != null);

        DbConnection db = DbConnection.getInstance();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM balance_operation";
        db.executeUpdate(query);
        assert (bdao.getBalanceOperations().size() == 0);

        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        assert (bdao.getBalanceOperations().size() > 0);
    }

    @Test
    public void testGetBalanceOperation() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        BalanceOperation bop;
        assertThrows(MissingDAOParameterException.class, () -> bdao.getBalanceOperation(0));
        try {
            assert (bdao.getBalanceOperation(500) == null);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        bop = new BalanceOperationModel(Integer.parseInt(opquery[0]), null, 0, null);

        try {
            assert (bdao.getBalanceOperation(bop.getBalanceId()) != null);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetBalanceOperations() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);

        assert (bdao.resetBalanceOperations());

        query = "SELECT * FROM balance_operation";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

}
