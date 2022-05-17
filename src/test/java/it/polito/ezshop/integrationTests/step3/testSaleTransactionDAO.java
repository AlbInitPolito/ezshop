package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.SaleTransactionModel;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.Assert.assertThrows;

public class testSaleTransactionDAO {

    @Test
    public void testGetMaxSaleTransactionId() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);

        assert (sdao.getMaxSaleTransactionId() != null);

        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);

        assert (sdao.getMaxSaleTransactionId() == 0);
    }

    @Test
    public void testRemoveSaleTransaction() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> sdao.removeSaleTransaction(null));
        assertThrows(InvalidDAOParameterException.class, () -> sdao.removeSaleTransaction(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);

        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        try {
            assert (sdao.removeSaleTransaction(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateSaleTransaction() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> sdao.updateSaleTransaction(null));
        SaleTransaction s = new SaleTransactionModel();
        assertThrows(MissingDAOParameterException.class, () -> sdao.updateSaleTransaction(s));
        s.setTicketNumber(0);
        assertThrows(InvalidDAOParameterException.class, () -> sdao.updateSaleTransaction(s));
        s.setTicketNumber(10);
        s.setDiscountRate(-1);
        s.setPaymentType(null);
        assertThrows(MissingDAOParameterException.class, () -> sdao.updateSaleTransaction(s));
        s.setPaymentType(-10);
        assertThrows(InvalidDAOParameterException.class, () -> sdao.updateSaleTransaction(s));
        s.setPaymentType(null);
        s.setDate(new Date(0));
        assertThrows(MissingDAOParameterException.class, () -> sdao.updateSaleTransaction(s));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        s.setTicketNumber(Integer.parseInt(opquery[0]));

        try {
            s.setTime(new Time(0));
            assert (sdao.updateSaleTransaction(s));
            s.setDate(null);
            s.setTime(null);
            s.setPrice(30);
            assert (sdao.updateSaleTransaction(s));
            s.setPrice(0);
            s.setPaymentType(1);
            assert (sdao.updateSaleTransaction(s));
            s.setPaymentType(null);
            s.setDiscountRate(15);
            assert (sdao.updateSaleTransaction(s));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetSaleBalanceOperation() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> sdao.setSaleBalanceOperation(null, 0));
        assertThrows(MissingDAOParameterException.class, () -> sdao.setSaleBalanceOperation(null, 1));
        assertThrows(InvalidDAOParameterException.class, () -> sdao.setSaleBalanceOperation(0, 1));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 35, 1, 0.3, null, null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        db.executeUpdate(query);
        String[] opquery2 = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (sdao.setSaleBalanceOperation(Integer.parseInt(opquery[0]), Integer.parseInt(opquery2[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetSaleTransactions() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);

        assert (sdao.resetSaleTransactions());

        query = "SELECT * FROM sale_transaction";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }
}
