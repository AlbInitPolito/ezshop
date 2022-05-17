package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.LoyaltyCardDAO;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoyaltyCardModelDriversStep4 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query1 = "DELETE FROM customer";
    String query2 = "DELETE FROM loyalty_card";

    @Test
    public void testCreateCardDriver(){
        String serialNumber;
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        dbConnection.executeUpdate(query2);
        LoyaltyCardModel lc = new LoyaltyCardModel(0, null);
        try{
            do{
                serialNumber = LoyaltyCardModel.generateSerialNumber();
                lc.setSerialNumber(serialNumber);
            } while(lcdao.getCard(lc.getSerialNumber()) != null);

            assertTrue(lcdao.addCard(lc.getSerialNumber()));
            assertEquals(lcdao.getCard(serialNumber).getSerialNumber(), serialNumber);
            assert(lcdao.getCard(serialNumber).getPoints() == 0);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query2);
    }

    @Test
    public void testAttachCardToCustomer(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        CustomerDAO cdao = new CustomerDAO();
        try {
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL);");
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            Customer customer = cdao.getCustomer(123);
            assertNotNull(customer);
            LoyaltyCardModel lc = lcdao.getCard("0123456789");
            assertNotNull(lc);
            assertNull(cdao.getCustomerByCard("0123456789"));
            customer.setCustomerCard("0123456789");
            assertTrue(cdao.updateCustomer(customer));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testCustomerName");
            assertEquals(cdao.getCustomer(123).getCustomerCard(), "0123456789");

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
    }

    @Test
    public void testModifyPointsOnCardDriver(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        dbConnection.executeUpdate(query2);
        int pointsToBeAdded = 10;
        try{
            assertNull(lcdao.getCard("0123456789"));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            LoyaltyCardModel lc = lcdao.getCard("0123456789");
            assertNotNull(lc);
            assert(lc.getPoints() == 0);
            assert(lc.getPoints() + pointsToBeAdded >= 0);
            lc.setPoints(lc.getPoints() + pointsToBeAdded);
            lcdao.updateCard(lc);
            assert(lcdao.getCard("0123456789").getPoints() == 10);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query2);
    }
}
