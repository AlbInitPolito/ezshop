package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testReturnProductDAO {

    @Test
    public void testResetReturnProductsDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            db.DBconnect(url, username, password, DBname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "DELETE FROM return_product;";
        assert (db.executeUpdate(query));
    }
}
