package it.polito.ezshop.unitTests;

import it.polito.ezshop.model.OrderModel;
import org.junit.Test;

public class TestOrderModel {

    @Test
    public void generateRFID() {
        String baseRFID = "000000010000";
        int quantity = 5;
        assert (OrderModel.generateRFID(baseRFID, quantity).size() == quantity);
        assert (OrderModel.generateRFID(baseRFID, 0) == null);
        assert (OrderModel.generateRFID(null, quantity) == null);
        assert (OrderModel.generateRFID(baseRFID, -5) == null);
        assert (OrderModel.generateRFID(baseRFID + "0123", quantity) == null);
        assert (OrderModel.generateRFID("abc", quantity) == null);
        assert (OrderModel.generateRFID(null, quantity) == null);
    }
}
