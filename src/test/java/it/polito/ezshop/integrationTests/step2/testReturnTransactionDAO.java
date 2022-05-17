package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testReturnTransactionDAO {

    @Test
    public void testGetMaxReturnTransactionIdDriver() {
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
        String query = "SELECT MAX(id) FROM return_transaction";
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testRemoveReturnTransactionDriver() {
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
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "DELETE FROM return_transaction WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testUpdateReturnTransactionDriver() {
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
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE return_transaction SET returned_value=53.7 WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetReturnTransactionDriver() {
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
        String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "SELECT * FROM return_transaction WHERE id=" + opquery[0];
        String[] result = db.executeQuery(query).get(0);
        assert (result != null);
    }

    @Test
    public void testResetReturnTransactionsDriver() {
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
        String query = "DELETE FROM return_product_rfid";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry";
        db.executeUpdate(query);
        query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction;";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testSetReturnBalanceOperationDriver() {
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
        String query = "INSERT INTO balance_operation VALUES(null, 'CREDIT', 30.0, sysdate());";
        boolean result = db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String balance = opquery[0];
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "UPDATE return_transaction SET balance_operation=" + balance + " WHERE id=" + opquery[0] + ";";
        assert (db.executeUpdate(query));
    }
}
