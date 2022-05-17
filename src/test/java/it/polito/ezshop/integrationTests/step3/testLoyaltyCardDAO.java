package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.LoyaltyCardDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;


public class testLoyaltyCardDAO {

    @Test
    public void testAddCard() {
        LoyaltyCardDAO ldao = new LoyaltyCardDAO();
        assertThrows(MissingDAOParameterException.class, () -> ldao.addCard(null));
        assertThrows(InvalidDAOParameterException.class, () -> ldao.addCard("1234567890123"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM loyalty_card WHERE serial_number='addCardTst'";
        db.executeUpdate(query);

        try {
            assert (ldao.addCard("addCardTst"));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateCard() {
        LoyaltyCardDAO ldao = new LoyaltyCardDAO();
        assertThrows(MissingDAOParameterException.class, () -> ldao.updateCard(null));
        assertThrows(MissingDAOParameterException.class, () -> ldao.updateCard(new LoyaltyCardModel(null, null)));
        assertThrows(InvalidDAOParameterException.class, () -> ldao.updateCard(new LoyaltyCardModel(null, "1234567890123")));
        assertThrows(MissingDAOParameterException.class, () -> ldao.updateCard(new LoyaltyCardModel(null, "updCardTst")));
        assertThrows(InvalidDAOParameterException.class, () -> ldao.updateCard(new LoyaltyCardModel(-10, "updCardTst")));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM loyalty_card WHERE serial_number='updCardTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('updCardTst', 0);";
        db.executeUpdate(query);

        try {
            assert (ldao.updateCard(new LoyaltyCardModel(10, "updCardTst")));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCard(){
        LoyaltyCardDAO ldao = new LoyaltyCardDAO();
        assertThrows(MissingDAOParameterException.class, () -> ldao.getCard(null));
        assertThrows(InvalidDAOParameterException.class, () -> ldao.getCard("1234567890123"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM loyalty_card WHERE serial_number='getCardTst'";
        db.executeUpdate(query);
        query = "INSERT INTO loyalty_card VALUES('getCardTst', 0);";
        db.executeUpdate(query);

        try {
            assert(ldao.getCard("getCardTst")!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM loyalty_card WHERE serial_number='getCardTst'";
        db.executeUpdate(query);

        try {
            assert(ldao.getCard("getCardTst")==null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetLoyaltyCards() {
        LoyaltyCardDAO ldao = new LoyaltyCardDAO();
        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM customer";
        db.executeUpdate(query);

        assert (ldao.resetLoyaltyCards());

        query = "SELECT * FROM loyalty_card";
        List<String[]> result = db.executeQuery(query);

        assert (result.size() == 0);
    }
}
