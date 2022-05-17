package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import org.junit.Test;

import java.util.List;

public class testTicketEntryDAO {

    @Test
    public void testResetTicketEntries(){
        TicketEntryDAO tdao = new TicketEntryDAO();
        assert(tdao.resetTicketEntries());

        DbConnection db = DbConnection.getInstance();
        String query = "SELECT * FROM product_in_transaction";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }
}
