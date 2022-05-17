package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

public class testSaleTransactionDAO {

    @Test
    public void testAddSaleTransactionDriver() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 133.7, 1, 0.0, sysdate(), null);";
        assert (db.executeUpdate(query));
        int sid = sdao.getMaxSaleTransactionId();
        assert (sid > 0);
        String[] opquery = db.executeQuery("SELECT * FROM sale_transaction WHERE id=" + sid + ";").get(0);
        assert (opquery != null);
    }

    @Test
    public void testGetSaleBalanceOperationDriver() {
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), " + opquery[0] + ");";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        query = "SELECT balance_operation FROM sale_transaction WHERE id=" + opquery[0] + ";";
        opquery = db.executeQuery(query).get(0);
        assert (opquery != null);

        try {
            assert (bdao.getBalanceOperation(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
