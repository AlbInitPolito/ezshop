package it.polito.ezshop.unitTests;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.model.CreditCardModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestCreditCardModel {

    @Test
    public void testHasEnoughMoney() {
        CreditCardModel cc = new CreditCardModel("8100844239540563", 15.00);
        assert (cc.hasEnoughMoney(14.2));
        assert (!cc.hasEnoughMoney(15.1));
    }

    @Test
    public void testGenerateValidCreditCardNumber() {
        CreditCardModel cc = new CreditCardModel();
        cc.setCreditCardNumber(CreditCardModel.generateValidCreditCardNumber());
        try {
            assert (CreditCardModel.luhnAlgorithm(cc.getCreditCardNumber()));
        } catch (InvalidCreditCardException iccp) {
            fail("InvalidCreditCardException");
        }
    }

    @Test
    public void testLoadCreditCardsFromFile() {
        List<CreditCardModel> list;
        /* T1 */
        list = CreditCardModel.loadCreditCardsFromFile("files\\creditcardsFORTESTING.txt");
        assert (list != null);

        /* T2 */
        list = CreditCardModel.loadCreditCardsFromFile("files\\FAKEcreditcards1.txt");
        assert (list == null);

        /* T3 */
        list = CreditCardModel.loadCreditCardsFromFile("files\\FAKEcreditcards2.txt");
        assert (list == null);

        /* T4 */
        list = CreditCardModel.loadCreditCardsFromFile("files\\FAKEcreditcards3.txt");
        assert (list == null);

    }

    @Test
    public void testExecutePayment()
    {
        String pathname = "files\\creditcardsFORTESTING.txt";
        CreditCardModel a = new CreditCardModel("3017786004753256", 150.00);
        CreditCardModel b = new CreditCardModel("5100293991053009", 10.00);
        CreditCardModel c = new CreditCardModel("4716258050958645", 0.00);
        CreditCardModel d = new CreditCardModel("6532720076163432", -15.5);
        CreditCardModel e = new CreditCardModel("3203664602202618", 8.5);
        CreditCardModel f = new CreditCardModel("1877374334133169", 4.1);

        /* T1 */
        assert (a.executePayment(pathname, 2.0));
        /* T2 */
        assert (!b.executePayment(pathname, 11.00));
        /* T3 */
        assert (!f.executePayment(pathname, -4.0));
        /* T4 */
        assert (!f.executePayment(pathname, null));
        /* T5 */
        assert (!f.executePayment(pathname+"t", 3.0));
        /* T6 */
        assert (!f.executePayment(pathname+"t", 5.0));
        /* T7 */
        assert (!f.executePayment(pathname+"t", null));
    }

    @Test
    public void testLuhnAlgorithm() {
        /*
         * Correct Credit Card Numbers - T1 test
         */
        try {
            assert (CreditCardModel.luhnAlgorithm("8100844239540563"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }
        try {
            assert (CreditCardModel.luhnAlgorithm("0724052221241694"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }
        try {
            assert (CreditCardModel.luhnAlgorithm("4551306325660666"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }

        /*
         * Wrong Credit Card Numbers - T2 test
         */
        try {
            assert (!CreditCardModel.luhnAlgorithm("8100844239540560"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }
        try {
            assert (!CreditCardModel.luhnAlgorithm("0724052221241690"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }
        try {
            assert (!CreditCardModel.luhnAlgorithm("4551306325660660"));
        } catch (InvalidCreditCardException icce) {
            fail("Invalid Credit Card Number!");
        }
        assertThrows(InvalidCreditCardException.class, () -> CreditCardModel.luhnAlgorithm("455130632566066"));
        assertThrows(InvalidCreditCardException.class, () -> CreditCardModel.luhnAlgorithm("vincenzo00000000"));

        /*
         * Null string as input - T3 test
         */
        assertThrows(InvalidCreditCardException.class, () -> CreditCardModel.luhnAlgorithm(null));


    }

}