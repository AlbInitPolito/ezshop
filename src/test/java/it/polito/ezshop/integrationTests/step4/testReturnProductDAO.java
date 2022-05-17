package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.util.List;

public class testReturnProductDAO {

    @Test
    public void testRemoveReturnProductDriver() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        DbConnection db = DbConnection.getInstance();

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
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);

        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + prod.getId() + ", 5);";
        db.executeUpdate(query);

        query = "DELETE FROM return_product WHERE return_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testUpdateReturnProductDriver() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        DbConnection db = DbConnection.getInstance();

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
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);

        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + prod.getId() + ", 5);";
        db.executeUpdate(query);

        query = "UPDATE return_product SET quantity=55 WHERE return_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        assert (db.executeUpdate(query));
    }

    @Test
    public void testGetReturnProductDriver() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        DbConnection db = DbConnection.getInstance();

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
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);

        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + prod.getId() + ", 5);";
        db.executeUpdate(query);

        query = "SELECT * FROM return_product WHERE return_transaction=" + opquery[0] + " AND product_type=" + prod.getId() + ";";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() != 0);

        String[] tuple = result.get(0);
        int pid = Integer.parseInt(tuple[1]);
        try {
            prod = pdao.getProductById(pid);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);
    }

    @Test
    public void testGetReturnProductsDriver() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        DbConnection db = DbConnection.getInstance();

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
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("12345678901234");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);

        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + prod.getId() + ", 5);";
        db.executeUpdate(query);

        query = "SELECT * FROM return_product;";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() != 0);

        int pid;
        for (String[] t : result) {
            pid = Integer.parseInt(t[1]);
            try {
                prod = pdao.getProductById(pid);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                e.printStackTrace();
            }
            assert (prod != null);
        }
    }
}
