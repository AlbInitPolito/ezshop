package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.util.List;

public class testTicketEntryDAO {

    @Test
    public void testRemoveTicketEntryDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAOInterface pdao = new ProductTypeDAO();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);

        query = "INSERT INTO product_in_transaction VALUES("+opquery[0]+", "+prod.getId()+", 10, 0);";
        db.executeUpdate(query);

        query = "DELETE FROM product_in_transaction WHERE sale_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        assert(db.executeUpdate(query));


    }

    @Test
    public void testUpdateTicketEntryDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAOInterface pdao = new ProductTypeDAO();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);

        query = "INSERT INTO product_in_transaction VALUES("+opquery[0]+", "+prod.getId()+", 10, 0);";
        db.executeUpdate(query);

        query = "UPDATE product_in_transaction SET amount=3, discount_rate=0.2 WHERE sale_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        assert(db.executeUpdate(query));
    }

    @Test
    public void testGetSaleTicketEntriesDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAOInterface pdao = new ProductTypeDAO();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);

        query = "INSERT INTO product_in_transaction VALUES("+opquery[0]+", "+prod.getId()+", 10, 0);";
        db.executeUpdate(query);

        query = "SELECT * FROM product_in_transaction WHERE sale_transaction=" + opquery[0];
        List<String[]> presult = db.executeQuery(query);
        Integer pid;
        for (String[] t : presult) {
            pid = Integer.parseInt(t[1]);
            try {
                prod = pdao.getProductById(pid);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                e.printStackTrace();
            }
            assert(prod!=null);
        }
    }

    @Test
    public void testGetTicketEntryDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAOInterface pdao = new ProductTypeDAO();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);
        query = "DELETE FROM return_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM sale_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);

        query = "INSERT INTO product_in_transaction VALUES("+opquery[0]+", "+prod.getId()+", 10, 0);";
        db.executeUpdate(query);

        query = "SELECT * FROM product_in_transaction WHERE sale_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        List<String[]> result = db.executeQuery(query);
        assert(result.size()!=0);
        String[] tuple = result.get(0);
        int pid = Integer.parseInt(tuple[1]);
        try {
            prod = pdao.getProductById(pid);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);
    }
}
