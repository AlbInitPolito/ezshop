package it.polito.ezshop.integrationTests.step2;

import it.polito.ezshop.DBConnection.DbConnection;
import org.junit.Test;

public class testReturnProductRfidDAO {

    @Test
    public void testAddReturnProductRfidDriver () {
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
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        assert(db.executeUpdate(query));
    }

    @Test
    public void testGetReturnProductRfidsDriver () {
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
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        query = "SELECT product_entry FROM return_product_rfid WHERE return_transaction =" + opquery[0] + ";";
        assert(db.executeQuery(query).size()!=0);
    }

    @Test
    public void testRemoveReturnProductRfidDriver () {
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
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        query = "DELETE FROM return_product_rfid WHERE product_entry=1 AND return_transaction=" + opquery[0] + ";";
        assert(db.executeUpdate(query));
    }

    @Test
    public void testResetReturnProductRfidDriver () {
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
        String query = "DELETE FROM return_product_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query="DELETE FROM product_in_transaction_rfid WHERE product_entry=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=1";
        db.executeUpdate(query);
        query = "DELETE FROM product_entry WHERE rfid=2";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='3336669990'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(1, '3336669990', 'description', 15.5, 23, 'note', null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO product_entry VALUES(1, 1, 0);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_transaction VALUES(null, null, " + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product_rfid VALUES(" + opquery[0] + ", '1');";
        db.executeUpdate(query);
        query = "DELETE FROM return_product_rfid;";
        assert(db.executeUpdate(query));
    }
}
