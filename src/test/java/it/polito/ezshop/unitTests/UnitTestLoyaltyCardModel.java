package it.polito.ezshop.unitTests;

import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTestLoyaltyCardModel {

    @Test
    public void testCheckSerialNumberFormat(){
        assertTrue(LoyaltyCardModel.checkSerialNumberFormat(LoyaltyCardModel.generateSerialNumber()));
        assertThrows(NullPointerException.class, () -> {LoyaltyCardModel.checkSerialNumberFormat(null);});
        assertFalse(LoyaltyCardModel.checkSerialNumberFormat("123456789"));
        assertFalse(LoyaltyCardModel.checkSerialNumberFormat("NonNumeric"));
        assertFalse(LoyaltyCardModel.checkSerialNumberFormat("123456789a"));
    }
}

