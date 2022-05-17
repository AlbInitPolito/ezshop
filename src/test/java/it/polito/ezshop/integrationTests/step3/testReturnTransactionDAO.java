package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnTransactionDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ReturnTransactionModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testReturnTransactionDAO {

    @Test
    public void testGetMaxReturnTransactionId() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);

        assert (rdao.getMaxReturnTransactionId() != null);

        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);

        assert (rdao.getMaxReturnTransactionId() == 0);
    }

    @Test
    public void testRemoveReturnTransaction() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.removeReturnTransaction(null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.removeReturnTransaction(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);

        try {
            assert (rdao.removeReturnTransaction(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateReturnTransaction() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnTransaction(null));
        ReturnTransactionModel r = new ReturnTransactionModel();
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnTransaction(r));
        r.setID(0);
        assertThrows(InvalidDAOParameterException.class, () -> rdao.updateReturnTransaction(r));
        r.setID(10);
        r.setReturnedValue(null);
        assertThrows(MissingDAOParameterException.class, () -> rdao.updateReturnTransaction(r));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        r.setID(Integer.parseInt(opquery[0]));
        r.setReturnedValue(30.5);
        try {
            assert (rdao.updateReturnTransaction(r));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetReturnTransaction() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnTransaction(null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnTransaction(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        try {
            assert (rdao.getReturnTransaction(Integer.parseInt(opquery[0])) != null);
            query = "DELETE FROM return_product";
            db.executeUpdate(query);
            query = "DELETE FROM return_transaction";
            db.executeUpdate(query);
            assert (rdao.getReturnTransaction(1) == null);

            query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
            db.executeUpdate(query);
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            query = "INSERT INTO return_transaction VALUES(null, 60, " + opquery[0] + ",null);";
            db.executeUpdate(query);
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);

            assert (rdao.getReturnTransaction(Integer.parseInt(opquery[0])) != null);
            query = "DELETE FROM return_product";
            db.executeUpdate(query);
            query = "DELETE FROM return_transaction";
            db.executeUpdate(query);
            assert (rdao.getReturnTransaction(1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetReturnBalanceOperation() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.setReturnBalanceOperation(null, 0));
        assertThrows(MissingDAOParameterException.class, () -> rdao.setReturnBalanceOperation(null, 1));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.setReturnBalanceOperation(0, 1));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 35, 1, 0.3, null, null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery2 = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        String[] opquery3 = db.executeQuery("SELECT last_insert_id();").get(0);
        try {
            assert (rdao.setReturnBalanceOperation(Integer.parseInt(opquery3[0]), Integer.parseInt(opquery2[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetReturnTransactions() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM return_product";
        db.executeUpdate(query);

        assert (rdao.resetReturnTransactions());

        query = "SELECT * FROM return_product";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

}
