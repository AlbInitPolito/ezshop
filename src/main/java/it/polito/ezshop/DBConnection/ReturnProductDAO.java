package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.data.ReturnProductDAOInterface;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;

import java.util.ArrayList;
import java.util.List;

public class ReturnProductDAO extends mysqlDAO implements ReturnProductDAOInterface {

    @Override
    public TicketEntry addReturnProduct(TicketEntry entry, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (entry == null)
            throw new MissingDAOParameterException("TicketEntry object is required" +
                    " in addReturnProduct in ReturnProductDAO");
        String barcode = entry.getBarCode();
        if (barcode == null)
            throw new MissingDAOParameterException("TicketEntry.barCode is required" +
                    " in addReturnProduct in ReturnProductDAO");
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in addReturnProduct in ReturnProductDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("TicketEntry.barCode length cannot be > 14" +
                    " in addReturnProduct in ReturnProductDAO \n Given instead: " + barcode);
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in addReturnProduct in ReturnProductDAO \n Given instead: " + returnId);
        if (entry.getAmount() <= 0)
            throw new MissingDAOParameterException("entry.amount is required" +
                    " in addReturnProduct in ReturnProductDAO");
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
           //e.printStackTrace();
            //return null;
        }
        if (prod == null)
            return null;
        int quantity;
        String query;
        TicketEntry t = getReturnProduct(barcode, returnId);
        if (t == null) {
            quantity = entry.getAmount();
            query = "INSERT INTO return_product VALUES(" + returnId + "," + prod.getId();
            query = query + "," + quantity;
            query = query + ");";
            t = null;
            boolean result = db.executeUpdate(query);
            String[] opquery;
            if (result) {
                opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
                if (opquery == null) return null;
                opquery = (db.executeQuery("SELECT * FROM return_product WHERE return_transaction=" + returnId +
                        " AND product_type=" + prod.getId() + ";")).get(0);
                if (opquery == null) return null;
                try {
                    prod = pdao.getProductById(Integer.parseInt(opquery[1]));
                } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                    //e.printStackTrace();
                    //return null;
                }
                //if (prod == null)
                //    return null;
                barcode = prod.getBarCode();
                quantity = Integer.parseInt(opquery[2]);
                t = new TicketEntryModel(barcode, null, quantity, 0, 0);
            }
        } else {
            quantity = t.getAmount();
            if (entry.getAmount() > 0)
                quantity += entry.getAmount();
            t.setAmount(quantity);
            if (!updateReturnProduct(t, returnId))
                return null;
        }
        return t;
    }

    @Override
    public boolean removeReturnProduct(String barcode, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in removeReturnProduct in ReturnProductDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in removeReturnProduct in ReturnProductDAO \n Given instead: " + returnId);
        if (barcode == null)
            throw new MissingDAOParameterException("barcode is required" +
                    " in removeReturnProduct in ReturnProductDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("barcode length cannot be > 14" +
                    " in removeReturnProduct in ReturnProductDAO \n Given instead: " + barcode);
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return false;
        }
        if (prod == null)
            return false;
        String query = "DELETE FROM return_product WHERE return_transaction=" + returnId + " AND product_type=" + prod.getId() + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateReturnProduct(TicketEntry entry, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (entry == null)
            throw new MissingDAOParameterException("TicketEntry object is required" +
                    " in updateReturnProduct in ReturnProductDAO");
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in updateReturnProduct in ReturnProductDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in updateReturnProduct in ReturnProductDAO \n Given instead: " + returnId);
        String barcode = entry.getBarCode();
        if (barcode == null)
            throw new MissingDAOParameterException("TicketEntry.barCode is required" +
                    " in updateReturnProduct in ReturnProductDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("TicketEntry.barCode length cannot be > 14" +
                    " in updateReturnProduct in ReturnProductDAO \n Given instead: " + barcode);
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return false;
        }
        if (prod == null)
            return false;
        int amount = entry.getAmount();
        if (amount <= 0)
            throw new MissingDAOParameterException("TicketEntry.amount is required" +
                    " in updateReturnProduct in ReturnProductDAO");
        String query = "UPDATE return_product SET quantity=" + amount + " WHERE return_transaction=" + returnId + " AND product_type=" + prod.getId() + ";";
        return db.executeUpdate(query);
    }

    @Override
    public TicketEntry getReturnProduct(String barcode, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("barcode is required" +
                    " in getReturnProduct in TicketEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("barcode length cannot be > 14" +
                    " in getReturnProduct in TicketEntryDAO \n Given instead: " + barcode);
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in getReturnProduct in TicketEntryDAOO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in getReturnProduct in TicketEntryDAO \n Given instead: " + returnId);
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return null;
        }
        if (prod == null)
            return null;
        String query = "SELECT * FROM return_product WHERE return_transaction=" + returnId + " AND product_type=" + prod.getId() + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        int pid = Integer.parseInt(tuple[1]);
        try {
            prod = pdao.getProductById(pid);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return null;
        }
        //if (prod == null)
        //    return null;
        int amount = Integer.parseInt(tuple[2]);
        return new TicketEntryModel(prod.getBarCode(), null, amount, 0, 0);
    }

    @Override
    public List<TicketEntry> getReturnProducts(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in getReturnProducts in ReturnProductDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in getReturnProducts in ReturnProductDAO \n Given instead: " + returnId);
        String query = "SELECT * FROM return_product WHERE return_transaction=" + returnId;
        List<String[]> presult = db.executeQuery(query);
        List<TicketEntry> prods = new ArrayList<>();
        int pid;
        int amount;
        ProductType prod = null;
        for (String[] t : presult) {
            amount = Integer.parseInt(t[2]);
            ProductTypeDAO pdao = new ProductTypeDAO();
            pid = Integer.parseInt(t[1]);
            try {
                prod = pdao.getProductById(pid);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                //e.printStackTrace();
                //return null;
            }
            //if (prod == null)
            //    continue;
            prods.add(new TicketEntryModel(prod.getBarCode(), null, amount, 0, 0));
        }
        return prods;
    }

    @Override
    public boolean resetReturnProducts() {
        String query = "DELETE FROM return_product;";
        return db.executeUpdate(query);
    }
}
