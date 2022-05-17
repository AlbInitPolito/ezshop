package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.LoyaltyCardDAO;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestLoyaltyCardModelStep5 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query1 = "DELETE FROM customer";
    String query2 = "DELETE FROM loyalty_card";

    @Test
    public void testCreateCard(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        dbConnection.executeUpdate(query2);

        String result = LoyaltyCardModel.createCard();
        assertNotNull(result);
        assertTrue(LoyaltyCardModel.checkSerialNumberFormat(result));
        try {
            assertNotNull(lcdao.getCard(result));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query2);
    }

    @Test
    public void testAttachCardToCustomer(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        try{
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL);");
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            assertTrue(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            Customer result = cdao.getCustomerByCard("0123456789");
            assertNotNull(result);
            assertEquals(result.getCustomerCard(), "0123456789");

            assertThrows(InvalidCustomerIdException.class, () -> LoyaltyCardModel.attachCardToCustomer("0123456789", null));
            assertThrows(InvalidCustomerIdException.class, () -> LoyaltyCardModel.attachCardToCustomer("0123456789", -1));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer(null, 123));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer("", 123));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer("012345678a", 123));
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
    }

    @Test
    public void testModifyPointsOnCard(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        try{
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard(null, 100));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard("", 100));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard("012345678", 100));

            assertFalse(LoyaltyCardModel.modifyPointsOnCard("0123456789", 100));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            assertFalse(LoyaltyCardModel.modifyPointsOnCard("0123456789", -1));
            assertTrue(LoyaltyCardModel.modifyPointsOnCard("0123456789", 100));
            assert(lcdao.getCard("0123456789").getPoints() == 100);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
    }

}
