package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testMysqlDAO {

    @Test
    public void testDbConnectionDBconnectDriver() {
        String url = "jdbc:mysql://localhost/";
        String username = "root";
        String password = "EZShop2021";
        String DBname = "ezshopdb";
        DbConnection db = DbConnection.getInstance();
        try {
            assert (db.DBconnect(url, username, password, DBname));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
