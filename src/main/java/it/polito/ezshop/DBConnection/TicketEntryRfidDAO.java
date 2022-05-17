package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.TicketEntryRfidDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.ArrayList;
import java.util.List;

public class TicketEntryRfidDAO extends mysqlDAO implements TicketEntryRfidDAOInterface {

    public TicketEntryRfidDAO() { super(); }

    @Override
    public boolean addTicketEntryRfid(String rfid, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in addTicketEntryRfid in TicketEntryRfidDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in addTicketEntryRfid in TicketEntryRfidDAO");
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in addTicketEntryRfid in TicketEntryRfidDAO \n Given instead: " + rfid);
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in addTicketEntryRfid in TicketEntryRfidDAO \n Given instead: " + saleId);
        String query = "INSERT INTO product_in_transaction_rfid VALUES(" + saleId + ",'" + rfid + "');";
        return db.executeUpdate(query);
    }

    @Override
    public boolean removeTicketEntryRfid(String rfid, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in removeTicketEntryRfid in TicketEntryRfidDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in removeTicketEntryRfid in TicketEntryRfidDAO");
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in removeTicketEntryRfid in TicketEntryRfidDAO \n Given instead: " + rfid);
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in removeTicketEntryRfid in TicketEntryRfidDAO \n Given instead: " + saleId);
        String query = "DELETE FROM product_in_transaction_rfid WHERE sale_transaction=" + saleId +
                " AND product_entry='" + rfid + "';";
        return db.executeUpdate(query);
    }

    @Override
    public List<String> getSaleTicketEntriesRfid(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (saleId == null)
        throw new MissingDAOParameterException("saleId is required" +
                " in getSaleTicketEntriesRfid in TicketEntryRfidDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in getSaleTicketEntriesRfid in TicketEntryRfidDAO \n Given instead: " + saleId);
        String query = "SELECT * FROM product_in_transaction_rfid WHERE sale_transaction=" + saleId;
        List<String[]> result = db.executeQuery(query);
        List<String> prods = new ArrayList<>();
        for(String[] prod : result)
            prods.add(prod[1]);
        return prods;
    }

    @Override
    public boolean resetTicketEntryRfid() {
        String query = "DELETE FROM product_in_transaction_rfid;";
        return db.executeUpdate(query);
    }
}
