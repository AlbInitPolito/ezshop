package it.polito.ezshop.integrationTests.step6;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

public class testTicketEntryDAO {

    @Test
    public void testAddTicketEntryDriver() {
        DbConnection db = DbConnection.getInstance();
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        TicketEntryDAO tdao = new TicketEntryDAO();

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
        query = "DELETE FROM product_type WHERE barcode='132413241324'";
        db.executeUpdate(query);

        ProductType p = null;
        try {
            p = pdao.getProductByBarcode("132413241324");
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(p==null);

        query = "INSERT INTO product_type VALUES(null, '132413241324', 'description', 30.0, 50, null, null, null, null);";
        db.executeUpdate(query);
        query = "INSERT INTO sale_transaction VALUES(null, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            p = pdao.getProductByBarcode("132413241324");
            assert (p != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        TicketEntry t = null;
        try{
        t = tdao.getTicketEntry("132413241324", Integer.parseInt(opquery[0]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(t==null);

        query = "INSERT INTO product_in_transaction VALUES(" + opquery[0] + "," + p.getId() + ", 5, null);";
        assert(db.executeUpdate(query));
        opquery = (db.executeQuery("SELECT * FROM product_in_transaction WHERE sale_transaction=" + opquery[0] +
                " AND product_type=" + p.getId() + ";")).get(0);
        assert(opquery!=null);

        try {
            p = pdao.getProductById(Integer.parseInt(opquery[1]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(p!=null);

        try{
            t = tdao.getTicketEntry("132413241324", Integer.parseInt(opquery[0]));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(t!=null);
        t.setAmount(7);
        try{
        assert(tdao.updateTicketEntry(t, Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }
}
