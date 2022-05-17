package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnTransactionDAO;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ReturnTransactionModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class testReturnTransactionDAO {

    @Test
    public void testAddReturnTransaction() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnTransaction(null));
        ReturnTransactionModel r = new ReturnTransactionModel();
        assertThrows(MissingDAOParameterException.class, () -> rdao.addReturnTransaction(r));
        r.setID(0);
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnTransaction(r));
        r.setID(null);
        r.setTransactionID(0);
        assertThrows(InvalidDAOParameterException.class, () -> rdao.addReturnTransaction(r));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 133.7, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            r.setTransactionID(Integer.parseInt(opquery[0]));
            assert (rdao.addReturnTransaction(r) != null);
            r.setID(rdao.getMaxReturnTransactionId()+1);
            assert (rdao.addReturnTransaction(r) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSaleBalanceOperation() {
        ReturnTransactionDAO rdao = new ReturnTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> rdao.getReturnBalanceOperation(null));
        assertThrows(InvalidDAOParameterException.class, () -> rdao.getReturnBalanceOperation(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        boolean result = db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String balance = opquery[0];
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + "," + balance + ");";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (rdao.getReturnBalanceOperation(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
