package it.polito.ezshop.integrationTests.step6;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.ReturnProductDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

public class testReturnProductDAO {

    @Test
    public void testAddReturnProductDriver() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        ReturnProductDAO rdao = new ReturnProductDAO();
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
        query = "DELETE FROM product_type WHERE barcode='111222333444'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, '111222333444', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        String[] product = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + product[0] + ",10,0);";
        db.executeUpdate(query);
        query = "INSERT INTO return_transaction VALUES(null,0," + opquery[0] + ",null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode("111222333444");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert (prod != null);

        TicketEntry t = null;
        try {
            t = rdao.getReturnProduct("111222333444", Integer.parseInt(opquery[0]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(t==null);

        query = "INSERT INTO return_product VALUES(" + opquery[0] + "," + prod.getId() + ", 5);";
        assert(db.executeUpdate(query));
        String[] r = (db.executeQuery("SELECT last_insert_id()")).get(0);
        assert(r!=null);
        opquery = (db.executeQuery("SELECT * FROM return_product WHERE return_transaction=" + r[0] +
                " AND product_type=" + prod.getId() + ";")).get(0);
        assert(opquery!=null);

        try {
            prod = pdao.getProductById(Integer.parseInt(opquery[1]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(prod!=null);

        try {
            t = rdao.getReturnProduct("111222333444", Integer.parseInt(opquery[0]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(t!=null);
        t.setAmount(7);
        try{
        assert(rdao.updateReturnProduct(t,Integer.parseInt(r[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
