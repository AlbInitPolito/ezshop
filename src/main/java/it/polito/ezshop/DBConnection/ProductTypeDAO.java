package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ProductTypeModel;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeDAO extends mysqlDAO implements ProductTypeDAOInterface {

    public ProductTypeDAO() {
        super();
    }

    @Override
    public ProductType addProductType(ProductType product) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (product == null)
            throw new MissingDAOParameterException("ProductType object is required" +
                    " in addProductType in ProductTypeDAO");
        String barcode = product.getBarCode();
        Double price = product.getPricePerUnit();
        if (price == null)
            throw new MissingDAOParameterException("ProductType.pricePerUnit is required" +
                    " in addProductType in ProductTypeDAO");
        if (barcode == null)
            throw new MissingDAOParameterException("ProductType.barcode is required" +
                    " in addProductType in ProductTypeDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("ProductType.barcode length cannot be > 14" +
                    " in addProductType in ProductTypeDAO \n Given instead: " + barcode);
        String description = product.getProductDescription();
        Integer quantity = product.getQuantity();
        String note = product.getNote();
        String[] position = new String[3];
        if (product.getLocation() == null) {
            position[0] = null;
            position[1] = null;
            position[2] = null;
        } else
            position = product.getLocation().split("-");
        String query = "INSERT INTO product_type VALUES(null, '" + barcode + "', ";
        if (description == null)
            query = query + "null, ";
        else {
            if (description.length() > 200)
                throw new InvalidDAOParameterException("ProductType.description length cannot be > 200" +
                        " in addProductType in ProductTypeDAO \n Given instead: " + description);
            query = query + "'" + description + "', ";
        }
        query = query + price + ", ";
        if (quantity == null)
            query = query + "null, ";
        else {
            if (quantity < 0)
                throw new InvalidDAOParameterException("ProductType.quantity must be > 0" +
                        " in addProductType in ProductTypeDAO \n Given instead: " + quantity);
            query = query + quantity + ", ";
        }
        if (note == null)
            query = query + "null, ";
        else {
            if (note.length() > 200)
                throw new InvalidDAOParameterException("ProductType.note length cannot be > 200" +
                        " in addProductType in ProductTypeDAO \n Given instead: " + note);
            query = query + "'" + note + "', ";
        }
        if (product.getLocation() == null)
            query = query + "null, null, null";
        else
            query = query + position[0] + ", '" + position[1] + "', " + position[2];
        query = query + ");";
        ProductType p = null;
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            if (opquery == null) return null;
            opquery = (db.executeQuery("SELECT * FROM product_type WHERE id=" + opquery[0] + ";")).get(0);
            if (opquery == null) return null;
            int id = Integer.parseInt(opquery[0]);
            barcode = opquery[1];
            if (opquery[2] == null)
                description = null;
            else
                description = opquery[2];
            price = Double.parseDouble(opquery[3]);
            if (opquery[4] == null)
                quantity = null;
            else
                quantity = Integer.parseInt(opquery[4]);
            if (opquery[5] == null)
                note = null;
            else
                note = opquery[5];
            Integer aisle;
            if (opquery[6] == null)
                aisle = null;
            else
                aisle = Integer.parseInt(opquery[6]);
            String rack = opquery[7];
            Integer level;
            if (opquery[8] == null)
                level = null;
            else
                level = Integer.parseInt(opquery[8]);
            p = new ProductTypeModel(id, barcode, description, price, quantity, 0.0, note, aisle, rack, level);
        }
        return p;
    }

    @Override
    public boolean removeProductType(Integer productId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (productId == null)
            throw new MissingDAOParameterException("ProductType.pricePerUnit is required" +
                    " in addProductType in ProductTypeDAO");
        if (productId <= 0)
            throw new InvalidDAOParameterException("ProductType.pricePerUnit must be > 0" +
                    " in addProductType in ProductTypeDAO \n Given instead: " + productId);
        String query = "DELETE FROM product_type WHERE id=" + productId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateProductType(ProductType product) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (product == null)
            throw new MissingDAOParameterException("ProductType object is required" +
                    " in updateProductType in ProductTypeDAO");
        Integer pid = product.getId();
        if (pid == null)
            throw new MissingDAOParameterException("ProductType.ID is required" +
                    " in updateProductType in ProductTypeDAO");
        if (pid <= 0)
            throw new InvalidDAOParameterException("ProductType.ID must be > 0" +
                    " in updateProductType in ProductTypeDAO \n Given instead: " + pid);
        String barcode = product.getBarCode();
        Double price = product.getPricePerUnit();
        String description = product.getProductDescription();
        Integer quantity = product.getQuantity();
        String note = product.getNote();
        if (product.getLocation() != null) {
            if (!product.getLocation().matches("^[0-9]+-[a-zA-Z]-[0-9]+$"))
                throw new InvalidDAOParameterException("ProductType.location must be of format ^[0-9]+-[a-zA-Z]-[0-9]+$" +
                        " in updateProductType in ProductTypeDAO \n Given instead: " + product.getLocation());
        }
        //List<String> queries = new ArrayList<>();
        String query = "";
        if ((barcode == null) && (price == null) && (description == null) && (quantity == null) && (note == null) && (product.getLocation() == null))
            throw new MissingDAOParameterException("At least one parameter (ProductType.barcode " +
                    "or ProductType.price or ProductType.description or ProductType.quantity " +
                    "or ProductType.note or ProductType.rackID) is required in updateProductType in ProductDAO");
        if (barcode != null) {
            if (barcode.length() > 14)
                throw new InvalidDAOParameterException("ProductType.barcode length cannot be > 14" +
                        " in updateProductType in ProductTypeDAO \n Given instead: " + barcode);
            query = query + " barcode='" + barcode + "'";
        }
        if (price != null) {
            if (query.length() > 0)
                query = query + ", ";
            query = query + " pricePerUnit=" + price;
        }
        if (description != null) {
            if (description.length() > 200)
                throw new InvalidDAOParameterException("ProductType.description length cannot be > 200" +
                        " in updateProductType in ProductTypeDAO \n Given instead: " + description);
            if (query.length() > 0)
                query = query + ", ";
            query = query + " description='" + description + "'";
        }
        if (quantity != null) {
            if (quantity < 0)
                throw new InvalidDAOParameterException("ProductType.quantity must be >= 0" +
                        " in updateProductType in ProductTypeDAO \n Given instead: " + price);
            if (query.length() > 0)
                query = query + ", ";
            query = query + " quantity=" + quantity;
        }
        if (note != null) {
            if (note.length() > 200)
                throw new InvalidDAOParameterException("ProductType.note length cannot be > 200" +
                        " in updateProductType in ProductTypeDAO \n Given instead: " + note);
            if (query.length() > 0)
                query = query + ", ";
            query = query + " notes='" + note + "'";
        }
        if (product.getLocation() != null) {
            String[] position = product.getLocation().split("-");
            if (query.length() > 0)
                query = query + ", ";
            query = query + "aisleID=" + position[0] + ", rackID='" + position[1] + "', levelID=" + position[2];
        }
        query = "UPDATE product_type SET " + query + " WHERE id=" + pid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<ProductType> getProducts() {
        String query = "SELECT * FROM product_type";
        List<String[]> result = db.executeQuery(query);
        List<ProductType> prods = new ArrayList<>();
        ProductType prod;
        int id;
        String barcode;
        String description;
        double price;
        Integer quantity;
        String notes;
        Integer aisle;
        String rack;
        Integer level;
        for (String[] tuple : result) {
            id = Integer.parseInt(tuple[0]);
            barcode = tuple[1];
            description = tuple[2];
            price = Double.parseDouble(tuple[3]);
            if (tuple[4] == null)
                quantity = null;
            else
                quantity = Integer.parseInt(tuple[4]);
            notes = tuple[5];
            if (tuple[6] == null)
                aisle = null;
            else
                aisle = Integer.parseInt(tuple[6]);
            rack = tuple[7];
            if (tuple[8] == null)
                level = null;
            else
                level = Integer.parseInt(tuple[8]);
            prod = new ProductTypeModel(id, barcode, description, price, quantity, 0.0, notes, aisle, rack, level);
            prods.add(prod);
        }
        return prods;
    }

    @Override
    public ProductType getProductById(Integer productId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (productId == null)
            throw new MissingDAOParameterException("ProductType.pricePerUnit is required" +
                    " in getProductById in ProductTypeDAO");
        if (productId <= 0)
            throw new InvalidDAOParameterException("ProductType.pricePerUnit must be > 0" +
                    " in getProductById in ProductTypeDAO \n Given instead: " + productId);
        String query = "SELECT * FROM product_type WHERE id=" + productId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        String description;
        double price;
        Integer quantity;
        String notes;
        Integer aisle;
        String rack;
        Integer level;
        int id = Integer.parseInt(tuple[0]);
        String barcode = tuple[1];
        description = tuple[2];
        price = Double.parseDouble(tuple[3]);
        if (tuple[4] == null)
            quantity = null;
        else
            quantity = Integer.parseInt(tuple[4]);
        notes = tuple[5];
        if (tuple[6] == null)
            aisle = null;
        else
            aisle = Integer.parseInt(tuple[6]);
        rack = tuple[7];
        if (tuple[8] == null)
            level = null;
        else
            level = Integer.parseInt(tuple[8]);
        return new ProductTypeModel(id, barcode, description, price, quantity, 0.0, notes, aisle, rack, level);
    }

    @Override
    public ProductType getProductByBarcode(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (barcode == null)
            throw new MissingDAOParameterException("ProductType.barcode is required" +
                    " in getProductByBarcode in ProductTypeDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("ProductType.barcode length cannot be > 14" +
                    " in getProductByBarcode in ProductTypeDAO \n Given instead: " + barcode);
        String query = "SELECT * FROM product_type WHERE barcode='" + barcode + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        String description;
        double price;
        Integer quantity;
        String notes;
        Integer aisle;
        String rack;
        Integer level;
        int id = Integer.parseInt(tuple[0]);
        String barc = tuple[1];
        description = tuple[2];
        price = Double.parseDouble(tuple[3]);
        if (tuple[4] == null)
            quantity = null;
        else
            quantity = Integer.parseInt(tuple[4]);
        notes = tuple[5];
        if (tuple[6] == null)
            aisle = null;
        else
            aisle = Integer.parseInt(tuple[6]);
        rack = tuple[7];
        if (tuple[8] == null)
            level = null;
        else
            level = Integer.parseInt(tuple[8]);
        return new ProductTypeModel(id, barc, description, price, quantity, 0.0, notes, aisle, rack, level);
    }

    @Override
    public boolean resetProductTypes() {
        String query = "DELETE FROM product_type;";
        return db.executeUpdate(query);
    }

    /*
    @Override
    public ProductType getProduct(ProductType product) {
        Integer id = product.getId();
        String barcode;
        String query;
        if ( ( id == null ) || ( id <= 0 ) ) {
            barcode = product.getBarCode();
            if ( barcode == null )
                return null;
            query = "SELECT * FROM product_type WHERE barcode=" + barcode + ";";
        }
        else
            query = "SELECT * FROM product_type WHERE id=" + id + ";";
        List<String[]> result = db.executeQuery(query);
        if ( result.size() == 0 )
            return null;
        String[] tuple = result.get(0);
        String description;
        double price;
        int quantity;
        String notes;
        int aisle;
        String rack;
        int level;
        id = Integer.parseInt(tuple[0]);
        barcode = tuple[1];
        description = tuple[2];
        price = Double.parseDouble(tuple[3]);
        quantity = Integer.parseInt(tuple[4]);
        notes = tuple[5];
        aisle = Integer.parseInt(tuple[6]);
        rack = tuple[7];
        level = Integer.parseInt(tuple[8]);
        return new ProductTypeModel( id , barcode , description , price , quantity ,0.0 , notes , aisle , rack ,level );
    }
    */
}
