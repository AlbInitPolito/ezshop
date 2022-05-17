package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.data.ReturnProductRfidDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.ArrayList;
import java.util.List;

public class ReturnProductRfidDAO extends mysqlDAO implements ReturnProductRfidDAOInterface {

    public ReturnProductRfidDAO() { super(); }

    @Override
    public boolean addReturnProductRfid(Integer returnId, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in addReturnProductRfid in ReturnProductRfidDAO");
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in addReturnProductRfid in ReturnProductRfidDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in addReturnProductRfid in ReturnProductRfidDAO \n Given instead: " + returnId);
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in addReturnProductRfid in ReturnProductRfidDAO \n Given instead: " + rfid);
        String query = "INSERT INTO return_product_rfid VALUES(" + returnId + ", '" + rfid + "');";
        return db.executeUpdate(query);
    }

    @Override
    public List<String> getReturnProductRfids(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in getReturnProductRfids in ReturnProductRfidDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in getReturnProductRfids in ReturnProductRfidDAO \n Given instead: " + returnId);
        String query = "SELECT product_entry FROM return_product_rfid WHERE return_transaction =" + returnId + ";";
        List<String[]> result = db.executeQuery(query);
        List<String> prods = new ArrayList<>();
        for(String[] prod : result)
            prods.add(prod[0]);
        return prods;
    }

    @Override
    public boolean removeReturnProductRfid(Integer returnId, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in removeReturnProductRfid in ReturnProductRfidDAO");
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in removeReturnProductRfid in ReturnProductRfidDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in removeReturnProductRfid in ReturnProductRfidDAO \n Given instead: " + returnId);
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in removeReturnProductRfid in ReturnProductRfidDAO \n Given instead: " + rfid);
        String query = "DELETE FROM return_product_rfid WHERE product_entry=" + rfid + " AND return_transaction=" + returnId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean resetReturnProductRfid() {
        String query = "DELETE FROM return_product_rfid;";
        return db.executeUpdate(query);
    }
}
