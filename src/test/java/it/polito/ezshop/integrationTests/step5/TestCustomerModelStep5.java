package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.model.CustomerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCustomerModelStep5 {

    String query1 = "DELETE FROM customer";
    String query2 = "DELETE FROM loyalty_card";
    DbConnection dbConnection = DbConnection.getInstance();

    @Test
    public void testDefineCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query1);

        assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.defineCustomer(null));
        assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.defineCustomer(""));
        try {
            assert(CustomerModel.defineCustomer("qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjha") == -1);
            Integer id = CustomerModel.defineCustomer("testCustomerName");
            assertNotNull(cdao.getCustomer(id));
            assertEquals(cdao.getCustomer(id).getCustomerName(), "testCustomerName");
            assertNull(cdao.getCustomer(id).getCustomerCard());
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
    }

    @Test
    public void testGetCustomer(){
        dbConnection.executeUpdate(query1);
        try {
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.getCustomer(-1));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.getCustomer(null));
            assertNull(CustomerModel.getCustomer(123));
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL)");
            Customer result = CustomerModel.getCustomer(123);
            assertNotNull(result);
            assert(result.getId().equals(123));
            assertEquals(result.getCustomerName(), "testCustomerName");
            assertNull(result.getCustomerCard());
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
    }

    @Test
    public void testGetAllCustomers(){
        dbConnection.executeUpdate(query1);
        assert(CustomerModel.getAllCustomers().size() == 0);
        dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (124, 'testCustomerName2', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (125, 'testCustomerName3', NULL)");
        assert(CustomerModel.getAllCustomers().size() == 3);
        dbConnection.executeUpdate(query1);
    }

    @Test
    public void testDeleteCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query1);
        try {
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.deleteCustomer(null));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.deleteCustomer(-1));
            assertNotNull(cdao.getCustomer(123));
            assertTrue(CustomerModel.deleteCustomer(123));
            assertNull(cdao.getCustomer(123));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testModifyCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        try{
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.modifyCustomer(null, "testNewCustomerName", "0123456789"));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.modifyCustomer(-1, "testNewCustomerName", "0123456789"));
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.modifyCustomer(123, null, "0123456789"));
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.modifyCustomer(123, "", "0123456789"));
            assertFalse(CustomerModel.modifyCustomer(123, "qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjha", "0123456789"));
            assertThrows(InvalidCustomerCardException.class, () -> CustomerModel.modifyCustomer(123, "testNewCustomerName", "012345678a"));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0)");
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('9876543210', 0)");
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
            dbConnection.executeUpdate("INSERT INTO customer VALUES (124, 'testCustomerName2', '0123456789')");
            assertFalse(CustomerModel.modifyCustomer(123, "testCustomerName", "0123456789"));
            assertTrue(CustomerModel.modifyCustomer(123, "testNewCustomerName", null));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testNewCustomerName");
            assertNull(cdao.getCustomer(123).getCustomerCard());
            assertTrue(CustomerModel.modifyCustomer(123, "testDifferentCustomerName", "9876543210"));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testDifferentCustomerName");
            assertEquals(cdao.getCustomer(123).getCustomerCard(), "9876543210");
            assertTrue(CustomerModel.modifyCustomer(123, "testYetAnotherCustomerName", ""));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testYetAnotherCustomerName");
            assertNull(cdao.getCustomer(123).getCustomerCard());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
    }

}
