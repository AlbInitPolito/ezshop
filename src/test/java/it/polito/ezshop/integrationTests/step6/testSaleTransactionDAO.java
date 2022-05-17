package it.polito.ezshop.integrationTests.step6;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.util.List;

public class testSaleTransactionDAO {

    @Test
    public void testGetSaleTransactionDriver() {
        TicketEntryDAO tdao = new TicketEntryDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "SELECT * FROM sale_transaction WHERE id=" + opquery[0];
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
        try {
            assert (tdao.getSaleTicketEntries(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSaleTransactionsDriver() {
TicketEntryDAO tdao = new TicketEntryDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        query = "SELECT * FROM sale_transaction";
        List<String[]> result = db.executeQuery(query);
        assert (result != null);
        for (String[] tuple : result) {
            try {
                assert (tdao.getSaleTicketEntries(Integer.parseInt(tuple[0])) != null);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                e.printStackTrace();
            }
        }
    }
}
