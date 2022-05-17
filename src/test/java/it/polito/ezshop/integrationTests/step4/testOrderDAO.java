package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.OrderModel;
import org.junit.Test;

import java.util.List;

public class testOrderDAO {

    @Test
    public void testAddOrderDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAO pdao = new ProductTypeDAO();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='99887766554433'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '99887766554433', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        ProductType p = null;
        try {
            p = pdao.getProductByBarcode("99887766554433");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (p != null);
        query = "INSERT INTO product_order VALUES(null, 12, 27, '" + OrderModel.OrderStatus.ISSUED + "', null, " + p.getId() + ");";
        assert(db.executeUpdate(query));
        assert(db.executeQuery("SELECT last_insert_id()").get(0)!=null);
        String[] opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
        assert(opquery!=null);
        opquery = (db.executeQuery("SELECT * FROM product_order WHERE id=" + opquery[0] + ";")).get(0);
        assert(opquery!=null);
    }

    @Test
    public void testGetOrderDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAO pdao = new ProductTypeDAO();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String product = opquery[0];
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', null, " + product + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        query = "SELECT * FROM product_order WHERE id=" + opquery[0];
        List<String[]> result = db.executeQuery(query);
        assert (result != null);
        try {
            assert (pdao.getProductById(Integer.parseInt(result.get(0)[5])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOrdersDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAO pdao = new ProductTypeDAO();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_type WHERE barcode='12345678901234'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '12345678901234', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        String product = opquery[0];
        query = "INSERT INTO product_order VALUES(null, 10.0, 20, 'ISSUED', null, " + product + ")";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        query = "SELECT * FROM product_order";
        List<String[]> result = db.executeQuery(query);
        assert (result != null);
        for (String[] tuple : result) {
            try {
                assert (pdao.getProductById(Integer.parseInt(tuple[5])) != null);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                e.printStackTrace();
            }
        }
    }
}
