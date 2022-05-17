package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.TicketEntryModel;

import java.util.ArrayList;
import java.util.List;

public class TicketEntryDAO extends mysqlDAO implements TicketEntryDAOInterface {

    public TicketEntryDAO() {
        super();
    }

    @Override
    public TicketEntry addTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (entry == null)
            throw new MissingDAOParameterException("TicketEntry object is required" +
                    " in addTicketEntry in TicketEntryDAO");
        String barcode = entry.getBarCode();
        if (barcode == null)
            throw new MissingDAOParameterException("TicketEntry.barCode is required" +
                    " in addTicketEntry in TicketEntryDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in addTicketEntry in TicketEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("TicketEntry.barCode length cannot be > 14" +
                    " in addTicketEntry in TicketEntryDAO \n Given instead: " + barcode);
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in addTicketEntry in TicketEntryDAO \n Given instead: " + saleId);
        if (entry.getAmount() <= 0)
            throw new MissingDAOParameterException("entry.amount is required" +
                    " in addTicketEntry in ReturnProductDAO");
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
        double discount = entry.getDiscountRate();
        int quantity;
        String query;
        TicketEntry t = getTicketEntry(barcode, saleId);
        if (t == null) {
            quantity = entry.getAmount();
            query = "INSERT INTO product_in_transaction VALUES(" + saleId + "," + prod.getId() + "," + quantity;
            if (discount >= 0)
                query = query + "," + discount;
            else
                query = query + ", null";
            query = query + ");";
            t = null;
            boolean result = db.executeUpdate(query);
            String[] opquery;
            if (result) {
                opquery = (db.executeQuery("SELECT * FROM product_in_transaction WHERE sale_transaction=" + saleId +
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
                String description = prod.getProductDescription();
                double price = prod.getPricePerUnit();
                quantity = Integer.parseInt(opquery[2]);
                if (opquery[3] == null)
                    discount = 0;
                else
                    discount = Double.parseDouble(opquery[3]);
                t = new TicketEntryModel(barcode, description, quantity, discount, price);
            }
        } else {
            quantity = t.getAmount();
            if (entry.getAmount() > 0)
                quantity += entry.getAmount();
            t.setAmount(quantity);
            if (!updateTicketEntry(t, saleId))
                return null;
        }
        return t;
    }

    @Override
    public boolean removeTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (entry == null)
            throw new MissingDAOParameterException("TicketEntry object is required" +
                    " in removeTicketEntry in TicketEntryDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in removeTicketEntry in TicketEntryDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in removeTicketEntry in TicketEntryDAO \n Given instead: " + saleId);
        String barcode = entry.getBarCode();
        if (barcode == null)
            throw new MissingDAOParameterException("TicketEntry.barCode is required" +
                    " in removeTicketEntry in TicketEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("TicketEntry.barCode length cannot be > 14" +
                    " in removeTicketEntry in TicketEntryDAO \n Given instead: " + barcode);
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
        String query = "DELETE FROM product_in_transaction WHERE sale_transaction=" + saleId + " AND product_type=" + prod.getId() + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (entry == null)
            throw new MissingDAOParameterException("TicketEntry object is required" +
                    " in updateTicketEntry in TicketEntryDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in updateTicketEntry in TicketEntryDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in updateTicketEntry in TicketEntryDAO \n Given instead: " + saleId);
        String barcode = entry.getBarCode();
        if (barcode == null)
            throw new MissingDAOParameterException("TicketEntry.barCode is required" +
                    " in updateTicketEntry in TicketEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("TicketEntry.barCode length cannot be > 14" +
                    " in updateTicketEntry in TicketEntryDAO \n Given instead: " + barcode);
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
        double discount = entry.getDiscountRate();
        if ((amount <= 0) && (discount < 0))
            throw new MissingDAOParameterException("At least one parameter (TicketEntry.amount or TicketEntry.discountRate" +
                    " is required in updateTicketEntry in TicketEntryDAO");
        String query = "";
        if (amount > 0)
            query = query + "amount=" + amount;
        if (discount >= 0) {
            if (query.length() > 0)
                query = query + ",";
            query = query + " discount_rate=" + discount;
        }
        query = "UPDATE product_in_transaction SET " + query + " WHERE sale_transaction=" + saleId + " AND product_type=" + prod.getId() + ";";
        return db.executeUpdate(query);
    }

    @Override
    public TicketEntry getTicketEntry(String barcode, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("product is required" +
                    " in getTicketEntry in TicketEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("product length cannot be > 14" +
                    " in getTicketEntry in TicketEntryDAO \n Given instead: " + barcode);
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in getTicketEntry in TicketEntryDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in getTicketEntry in TicketEntryDAO \n Given instead: " + saleId);
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
        String query = "SELECT * FROM product_in_transaction WHERE sale_transaction=" + saleId + " AND product_type=" + prod.getId() + ";";
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
        double discount;
        if (tuple[3] == null)
            discount = 0;
        else
            discount = Double.parseDouble(tuple[3]);
        return new TicketEntryModel(prod.getBarCode(), prod.getProductDescription(), amount, discount, prod.getPricePerUnit());
    }

    /*
    @Override
    public List<TicketEntry> getTicketEntries() {
        String query = "SELECT * FROM product_in_transaction";
        List<String[]> result = db.executeQuery(query);
        List<TicketEntry> entries = new ArrayList<>();
        for(String[] tuple : result){
            ProductTypeDAOInterface pdao = new ProductTypeDAO();
            ProductType prod = new ProductTypeModel(Integer.parseInt(tuple[1]), null, null, null, null, null, null, null, null, null);
            prod = pdao.getProduct(prod);
            int amount = Integer.parseInt(tuple[2]);
            double discount = Double.parseDouble(tuple[3]);
            entries.add(new TicketEntryModel(prod.getBarCode(), prod.getProductDescription(), amount, discount, prod.getPricePerUnit()));

        }
        return entries;
    }
     */

    @Override
    public List<TicketEntry> getSaleTicketEntries(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in getTicketEntry in TicketEntryDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in getTicketEntry in TicketEntryDAO \n Given instead: " + saleId);
        String query = "SELECT * FROM product_in_transaction WHERE sale_transaction=" + saleId;
        List<String[]> presult = db.executeQuery(query);
        List<TicketEntry> prods = new ArrayList<>();
        Integer pid;
        int amount;
        double pdiscount;
        for (String[] t : presult) {
            pid = Integer.parseInt(t[1]);
            amount = Integer.parseInt(t[2]);
            if (t[3] == null)
                pdiscount = 0;
            else
                pdiscount = Double.parseDouble(t[3]);
            ProductTypeDAO pdao = new ProductTypeDAO();
            ProductType prod = null;
            try {
                prod = pdao.getProductById(pid);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                //e.printStackTrace();
                //continue;
            }
            prods.add(new TicketEntryModel(prod.getBarCode(), prod.getProductDescription(), amount, pdiscount, prod.getPricePerUnit()));
        }
        return prods;
    }

    @Override
    public boolean resetTicketEntries() {
        String query = "DELETE FROM product_in_transaction;";
        return db.executeUpdate(query);
    }
}
