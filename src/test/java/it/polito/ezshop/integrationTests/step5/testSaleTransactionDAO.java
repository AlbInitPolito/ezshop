package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.SaleTransactionModel;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class testSaleTransactionDAO {

    @Test
    public void testAddSaleTransaction() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        SaleTransaction s = new SaleTransactionModel();
        assertThrows(MissingDAOParameterException.class, () -> sdao.addSaleTransaction(s));
        s.setTicketNumber(0);
        assertThrows(InvalidDAOParameterException.class, () -> sdao.addSaleTransaction(s));
        s.setTicketNumber(1);
        s.setPaymentType(-1);
        assertThrows(InvalidDAOParameterException.class, () -> sdao.addSaleTransaction(s));
        s.setPaymentType(1);
        s.setDiscountRate(0.3);
        s.setTicketNumber(sdao.getMaxSaleTransactionId() + 1);
        try {
            assert (sdao.addSaleTransaction(s) != null);
            assert (sdao.addSaleTransaction(null) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        s.setPaymentType(null);
        s.setDiscountRate(-1);
        s.setTicketNumber(sdao.getMaxSaleTransactionId() + 1);
        try {
            assert (sdao.addSaleTransaction(s) != null);
            assert (sdao.addSaleTransaction(null) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSaleBalanceOperation() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> sdao.getSaleBalanceOperation(null));
        assertThrows(InvalidDAOParameterException.class, () -> sdao.getSaleBalanceOperation(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, 35, 1, 0.3, null, " + opquery[0] + ");";
        db.executeUpdate(query);
        opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        try {
            assert (sdao.getSaleBalanceOperation(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (sdao.getSaleBalanceOperation(Integer.parseInt(opquery[0])) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        try {
            assert (sdao.getSaleBalanceOperation(Integer.parseInt(opquery[0])) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
