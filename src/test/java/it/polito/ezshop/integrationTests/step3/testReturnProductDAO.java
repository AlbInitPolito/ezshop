package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ReturnProductDAO;
import org.junit.Test;

import java.util.List;

public class testReturnProductDAO {

    @Test
    public void testResetReturnProducts() {
        ReturnProductDAO rdao = new ReturnProductDAO();
        assert (rdao.resetReturnProducts());

        DbConnection db = DbConnection.getInstance();
        String query = "SELECT * FROM return_product";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }
}
