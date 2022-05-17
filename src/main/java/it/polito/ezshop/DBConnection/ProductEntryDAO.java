package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.ProductEntryDAOInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.ArrayList;
import java.util.List;

public class ProductEntryDAO extends mysqlDAO implements ProductEntryDAOInterface {

    public ProductEntryDAO() {
        super();
    }

    @Override
    public boolean addProductEntry(String barcode, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("barcode is required" +
                    " in addProductEntry in ProductEntryDAO");
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in addProductEntry in ProductEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("barcode length cannot be > 14" +
                    " in addProductEntry in ProductEntryDAO \n Given instead: " + barcode);
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in addProductEntry in ProductEntryDAO \n Given instead: " + rfid);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p;
        try {
            p = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            return false;
        }
        String query = "INSERT INTO product_entry VALUES('" + rfid + "', '" + p.getId() + "', 0);";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateProductEntry(String rfid, boolean available) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in updateProductEntry in ProductEntryDAO");
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in updateProductEntry in ProductEntryDAO \n Given instead: " + rfid);
        String query = "UPDATE product_entry SET available=" + available + " WHERE rfid=" + rfid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<String> getProductEntries(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("barcode is required" +
                    " in getProductEntries in ProductEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("barcode length cannot be > 14" +
                    " in getProductEntries in ProductEntryDAO \n Given instead: " + barcode);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p;
        try {
            p = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            return null;
        }
        String query = "SELECT rfid FROM product_entry WHERE product_type='" + p.getId() + "';";
        List<String[]> result = db.executeQuery(query);
        List<String> prods = new ArrayList<>();
        for (String[] prod : result)
            prods.add(prod[0]);
        return prods;
    }

    @Override
    public String getProductEntryBarcode(String rfid) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in getProductEntryBarcode in ProductEntryDAO");
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in getProductEntryBarcode in ProductEntryDAO \n Given instead: " + rfid);
        String query = "SELECT product_type FROM product_entry WHERE rfid='" + rfid + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String pid = result.get(0)[0];
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p;
        try {
            p = pdao.getProductById(Integer.parseInt(pid));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            return null;
        }
        return p.getBarCode();
    }

    @Override
    public Integer getProductEntryAvailability(String rfid) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (rfid == null)
            throw new MissingDAOParameterException("rfid is required" +
                    " in getProductEntryAvailability in ProductEntryDAO");
        if (rfid.length() > 12)
            throw new InvalidDAOParameterException("rfid length cannot be > 12" +
                    " in getProductEntryAvailability in ProductEntryDAO \n Given instead: " + rfid);
        String query = "SELECT available FROM product_entry WHERE rfid='" + rfid + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        return Integer.parseInt(result.get(0)[0]);
    }

    @Override
    public List<String> getAvailableProductEntries(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("barcode is required" +
                    " in getAvailableProductEntries in ProductEntryDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("barcode length cannot be > 14" +
                    " in getAvailableProductEntries in ProductEntryDAO \n Given instead: " + barcode);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType p;
        try {
            p = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            return null;
        }
        String query = "SELECT rfid FROM product_entry WHERE product_type='" + p.getId() + "' AND available=1;";
        List<String[]> result = db.executeQuery(query);
        List<String> prods = new ArrayList<>();
        for (String[] prod : result)
            prods.add(prod[0]);
        return prods;
    }

    @Override
    public boolean resetProductEntries() {
        String query = "DELETE FROM product_entry;";
        return db.executeUpdate(query);
    }
}
