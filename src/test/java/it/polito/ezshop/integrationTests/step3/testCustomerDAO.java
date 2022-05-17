package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.CustomerModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;

public class testCustomerDAO {

    @Test
    public void testAddCustomer() {
        CustomerDAO cdao = new CustomerDAO();
        assertThrows(MissingDAOParameterException.class, () -> cdao.addCustomer(null));
        Customer c = new CustomerModel();
        assertThrows(MissingDAOParameterException.class, () -> cdao.addCustomer(c));
        c.setCustomerName("1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
        assertThrows(InvalidDAOParameterException.class, () -> cdao.addCustomer(c));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer WHERE name='nomeAddCustomer'";
        db.executeUpdate(query);

        c.setCustomerName("nomeAddCustomer");
        try {
            assert (cdao.addCustomer(c) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveCustomer() {
        CustomerDAO cdao = new CustomerDAO();
        assertThrows(MissingDAOParameterException.class, () -> cdao.removeCustomer(null));
        assertThrows(InvalidDAOParameterException.class, () -> cdao.removeCustomer(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer WHERE name='testnameremove'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'testnameremove', null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (cdao.removeCustomer(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM customer WHERE id=" + opquery[0];
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

    @Test
    public void testUpdateCustomer() {
        CustomerDAO cdao = new CustomerDAO();
        assertThrows(MissingDAOParameterException.class, () -> cdao.updateCustomer(null));
        Customer c = new CustomerModel();
        assertThrows(MissingDAOParameterException.class, () -> cdao.updateCustomer(c));
        c.setId(0);
        assertThrows(InvalidDAOParameterException.class, () -> cdao.updateCustomer(c));
        c.setId(1);
        assertThrows(MissingDAOParameterException.class, () -> cdao.updateCustomer(c));
        c.setCustomerCard("1234567890123");
        assertThrows(InvalidDAOParameterException.class, () -> cdao.updateCustomer(c));
        c.setCustomerCard(null);
        c.setCustomerName("1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
        assertThrows(InvalidDAOParameterException.class, () -> cdao.updateCustomer(c));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer WHERE name='nameUpdateCustomer'";
        db.executeUpdate(query);
        query = "DELETE FROM customer WHERE name='newNameUpdateCustomer'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameUpdateCustomer', null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        c.setId(Integer.parseInt(opquery[0]));
        c.setCustomerName("newNameUpdateCustomer");
        try {
            assert (cdao.updateCustomer(c));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM loyalty_card WHERE serial_number='updCustTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('updCustTst', 0);";
        db.executeUpdate(query);
        c.setCustomerName(null);
        c.setCustomerCard("updCustTst");
        try {
            assert (cdao.updateCustomer(c));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM loyalty_card WHERE serial_number='finCustTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('finCustTst', 0);";
        db.executeUpdate(query);
        c.setCustomerName("finalCustomerName");
        try {
            assert (cdao.updateCustomer(c));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCustomers() {
        CustomerDAO cdao = new CustomerDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer;";
        db.executeUpdate(query);
        assert (cdao.getCustomers().size() == 0);

        query = "DELETE FROM customer WHERE name='nameGetCustomers'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomers', null);";
        db.executeUpdate(query);

        query = "DELETE FROM loyalty_card WHERE serial_number='getCustTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('getCustTst', 0);";
        db.executeUpdate(query);
        query = "DELETE FROM customer WHERE name='nameGetCustomerWithCard'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomerWithCard', 'getCustTst');";
        db.executeUpdate(query);
        assert (cdao.getCustomers().size() != 0);
    }

    @Test
    public void testGetCustomer() {
        CustomerDAO cdao = new CustomerDAO();

        assertThrows(MissingDAOParameterException.class, () -> cdao.getCustomer(null));
        assertThrows(InvalidDAOParameterException.class, () -> cdao.getCustomer(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer";
        db.executeUpdate(query);

        try {
            assert (cdao.getCustomer(1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM loyalty_card WHERE serial_number='getCustTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('getCustTst', 0);";
        db.executeUpdate(query);
        query = "DELETE FROM customer WHERE name='nameGetCustomerWithCard'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomerWithCard', 'getCustTst');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (cdao.getCustomer(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM customer WHERE name='nameGetCustomer'";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomer', null);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (cdao.getCustomer(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCustomerByCard() {
        CustomerDAO cdao = new CustomerDAO();

        assertThrows(MissingDAOParameterException.class, () -> cdao.getCustomerByCard(null));
        assertThrows(InvalidDAOParameterException.class, () -> cdao.getCustomerByCard("1234567890123"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer";
        db.executeUpdate(query);
        try {
            assert (cdao.getCustomerByCard("nGetCust") == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM customer WHERE name='nameGetCustomerByCard'";
        db.executeUpdate(query);
        query = "DELETE FROM loyalty_card WHERE serial_number='ByCard'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('ByCard', 0)";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomerByCard', 'ByCard');";
        db.executeUpdate(query);

        try {
            assert (cdao.getCustomerByCard("ByCard") != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDetachCustomerCard() {
        CustomerDAO cdao = new CustomerDAO();

        assertThrows(MissingDAOParameterException.class, () -> cdao.detachCustomerCard(null));
        assertThrows(InvalidDAOParameterException.class, () -> cdao.detachCustomerCard(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer";
        db.executeUpdate(query);
        query = "DELETE FROM loyalty_card WHERE serial_number='ByCard'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('ByCard', 0)";
        db.executeUpdate(query);
        query = "INSERT INTO customer VALUES(null, 'nameGetCustomerByCard', 'ByCard');";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (cdao.detachCustomerCard(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetCustomers() {
        CustomerDAO cdao = new CustomerDAO();
        assert (cdao.resetCustomers());

        DbConnection db = DbConnection.getInstance();
        String query = "SELECT * FROM customer";
        List<String[]> result = db.executeQuery(query);

        assert (result.size() == 0);
    }
}
