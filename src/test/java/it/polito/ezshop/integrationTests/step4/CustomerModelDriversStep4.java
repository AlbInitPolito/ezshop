package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.model.CustomerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerModelDriversStep4 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query = "DELETE FROM customer";

    @Test
    public void testDefineCustomerDriver(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query);
        CustomerModel customer = new CustomerModel();

        customer.setCustomerName("testCustomerName");
        try {
            Customer result = cdao.addCustomer(customer);
            assertNotNull(result);
            assertEquals(result.getCustomerName(), "testCustomerName");
            assertNull(result.getCustomerCard());
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetCustomerDriver(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query);
        try{
            assertNull(cdao.getCustomer(123));
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL);");
            Customer result = cdao.getCustomer(123);
            assertNotNull(result);
            assert(result.getId() == 123);
            assertEquals(result.getCustomerName(), "testCustomerName");
            assertNull(result.getCustomerCard());
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetAllCustomersDriver(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query);
        assert(cdao.getCustomers().size() == 0);
        dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (124, 'testCustomerName2', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (125, 'testCustomerName3', NULL)");
        assert(cdao.getCustomers().size() == 3);
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testDeleteCustomerDriver(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL)");
        try{
            assert(dbConnection.executeQuery("SELECT * FROM customer WHERE id=123").size() == 1);
            assertTrue(cdao.removeCustomer(123));
            assert(dbConnection.executeQuery("SELECT * FROM customer WHERE id=123").size() == 0);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testModifyCustomerDriver(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
        dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0)");
        try {
            Customer cust = cdao.getCustomer(123);
            assertNotNull(cust);
            assertEquals(cust.getCustomerName(), "testCustomerName1");
            assertNull(cust.getCustomerCard());

            /* Change name, no update on loyalty card */
            Customer customer = new CustomerModel();
            customer.setId(123);
            customer.setCustomerName("newCustomerName");
            customer.setCustomerCard(null);

            assertTrue(cdao.updateCustomer(customer));
            Customer result = cdao.getCustomer(123);
            assertNotNull(result);
            assertEquals(result.getCustomerName(), "newCustomerName");
            assertNull(result.getCustomerCard());

            /* Change loyalty card, no update on customer name */
            customer.setCustomerCard("0123456789");
            assertTrue(cdao.updateCustomer(customer));
            result = cdao.getCustomer(123);
            assertNotNull(result);
            assertEquals(result.getCustomerName(), "newCustomerName");
            assertEquals(result.getCustomerCard(), "0123456789");

            /* Detach loyalty card and change name*/
            cdao.detachCustomerCard(customer.getId());
            customer = cdao.getCustomer(customer.getId());
            customer.setCustomerName("testCustomerName");
            assertTrue(cdao.updateCustomer(customer));
            result = cdao.getCustomer(123);
            assertNotNull(result);
            assertEquals(result.getCustomerName(), "testCustomerName");
            assertNull(result.getCustomerCard());

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

}
