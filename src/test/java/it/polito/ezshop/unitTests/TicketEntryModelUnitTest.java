package it.polito.ezshop.unitTests;

import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicketEntryModelUnitTest {

    @Test
    public void testRFID(){
        TicketEntryModel ticketEntryModel = new TicketEntryModel("123456789128", null, 0, 0.0, 0.50);
        assertEquals(0, ticketEntryModel.getAmount());
        ticketEntryModel.addRFID("012345678901");
        assertTrue(ticketEntryModel.contains("012345678901"));
        assertEquals(1, ticketEntryModel.getAmount());
        assertEquals(1, ticketEntryModel.getRFIDs().size());
        ticketEntryModel.removeRFID("012345678901");
        assertFalse(ticketEntryModel.contains("012345678901"));

    }
}
