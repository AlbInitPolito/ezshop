package it.polito.ezshop.integrationTests.step7;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;

public class testSaleTransactionDAO {

    @Test
    public void testGetSaleTransaction() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        assertThrows(MissingDAOParameterException.class, () -> sdao.getSaleTransaction(null));
        assertThrows(InvalidDAOParameterException.class, () -> sdao.getSaleTransaction(0));

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        try {
            assert (sdao.getSaleTransaction(Integer.parseInt(opquery[0])) != null);

            query = "INSERT INTO sale_transaction VALUES(null, 35, 1, 0.3, null, null);";
            db.executeUpdate(query);
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);

            assert (sdao.getSaleTransaction(Integer.parseInt(opquery[0])) != null);

            query = "DELETE FROM return_product_rfid";
            db.executeUpdate(query);
            query="DELETE FROM product_in_transaction_rfid";
            db.executeUpdate(query);
            query = "DELETE FROM product_entry";
            db.executeUpdate(query);
            query = "DELETE FROM product_order";
            db.executeUpdate(query);
            query = "DELETE FROM return_product";
            db.executeUpdate(query);
            query = "DELETE FROM return_transaction";
            db.executeUpdate(query);
            query = "DELETE FROM product_in_transaction";
            db.executeUpdate(query);
            query = "DELETE FROM sale_transaction";
            db.executeUpdate(query);
            assert (sdao.getSaleTransaction(1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSaleTransactions() {
        SaleTransactionDAO sdao = new SaleTransactionDAO();
        List<SaleTransaction> stl = sdao.getSaleTransactions();
        assert (stl != null);

        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 35, 1, 0.3, null, null);";
        db.executeUpdate(query);

        assert (sdao.getSaleTransactions().size() > 0);

        query = "DELETE FROM return_product_rfid";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry";
        db.executeUpdate(query);
        query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);

        assert (sdao.getSaleTransactions().size() == 0);
    }
}
