package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnTransactionDAO;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

public class testReturnTransactionDAO {
    @Test
    public void testAddReturnTransactionDriver() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        boolean result = db.executeUpdate(query);
        assert (result);
        int rid = rdao.getMaxReturnTransactionId();
        assert (rid != 0);
        opquery = db.executeQuery("SELECT * FROM return_transaction WHERE id=" + rid + ";").get(0);
        assert (opquery != null);
    }

    @Test
    public void testGetReturnBalanceOperationDriver() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        boolean result = db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String balance = opquery[0];
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + "," + balance+");";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        query = "SELECT balance_operation FROM return_transaction WHERE id=" + opquery[0] + ";";
        opquery = db.executeQuery(query).get(0);
        assert (opquery != null);

        try {
            assert (bdao.getBalanceOperation(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
