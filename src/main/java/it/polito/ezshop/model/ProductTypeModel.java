package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;

import java.util.Locale;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeModel implements it.polito.ezshop.data.ProductType {
    private Integer ID;
    private String barcode;
    private String productDescription;
    private Double pricePerUnit;
    private Integer quantity;
    private Double discountRate;
    private String note;
    private Integer aisleID;
    private String rackID;
    private Integer levelID;

    /**
     * Constructor
     *
     * @param ID                 ID
     * @param barcode            barcode
     * @param productDescription productDescription
     * @param pricePerUnit       pricePerUnit
     * @param quantity           quantity
     * @param discountRate       discountRate
     * @param note               note
     * @param aisleID            aisleID
     * @param rackID             rackID
     * @param levelID            levelID
     */
    public ProductTypeModel(Integer ID, String barcode, String productDescription, Double pricePerUnit, Integer quantity, Double discountRate, String note, Integer aisleID, String rackID, Integer levelID) {
        this.ID = ID;
        this.barcode = barcode;
        this.productDescription = productDescription;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.note = note;
        this.aisleID = aisleID;
        this.rackID = rackID;
        this.levelID = levelID;
    }

    /**
     * Constructor
     *
     * @param barcode            barcode
     * @param productDescription productDescription
     * @param pricePerUnit       pricePerUnit
     * @param quantity           quantity
     * @param discountRate       discountRate
     * @param note               note
     * @param aisleID            aisleID
     * @param rackID             rackID
     * @param levelID            levelID
     */
    public ProductTypeModel(String barcode, String productDescription, Double pricePerUnit, Integer quantity, Double discountRate, String note, Integer aisleID, String rackID, Integer levelID) {
        this.ID = null;
        this.barcode = barcode;
        this.productDescription = productDescription;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.note = note;
        this.aisleID = aisleID;
        this.rackID = rackID;
        this.levelID = levelID;
    }

    /**
     * Constructor which sets all attributes to "null" values
     */
    public ProductTypeModel() {
        this.ID = 0;
        this.barcode = null;
        this.productDescription = null;
        this.pricePerUnit = 0.0;
        this.quantity = 0;
        this.discountRate = 0.0;
        this.note = null;
        this.aisleID = -1;
        this.rackID = null;
        this.levelID = -1;
    }

    /**
     * Getter: discountRate
     *
     * @return discountRate
     */
    public Double getDiscountRate() {
        return this.discountRate;
    }

    /**
     * Setter: discountRate
     *
     * @param discountRate discountRate
     */
    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * Getter: aisleID
     *
     * @return aisleID
     */
    public Integer getAisleID() {
        return this.aisleID;
    }

    /**
     * Setter: aisleID
     *
     * @param aisleID aisleID
     */
    public void setAisleID(Integer aisleID) {
        this.aisleID = aisleID;
    }

    /**
     * Getter: rackID
     *
     * @return rackID
     */
    public String getRackID() {
        return this.rackID;
    }

    /**
     * Setter: rackID
     *
     * @param rackID rackID
     */
    public void setRackID(String rackID) {
        this.rackID = rackID;
    }

    /**
     * Getter: levelID
     *
     * @return levelID
     */
    public Integer getLevelID() {
        return this.levelID;
    }

    /**
     * Setter: levelID
     *
     * @param levelID levelID
     */
    public void setLevelID(Integer levelID) {
        this.levelID = levelID;
    }

    /**
     * Get the Product Type's quantity
     *
     * @return quantity
     */
    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Set the Product Type's quantity
     *
     * @param quantity quantity
     */
    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the Product Type's location
     *
     * @return location
     */
    @Override
    public String getLocation() {
        if (this.rackID == null || this.levelID == null || this.aisleID == null)
            return null;
        return this.aisleID + "-" + this.rackID + "-" + this.levelID;
    }

    /**
     * Set the Product Type's location
     * Format: <aisleNumber>-<rackAlphabeticIdentifier>-<levelNumber>
     *
     * @param location location
     */
    @Override
    public void setLocation(String location) {
        if (location == null) {
            this.aisleID = null;
            this.rackID = null;
            this.levelID = null;
        } else {
            this.aisleID = Integer.valueOf(location.split("-")[0]);
            this.rackID = location.split("-")[1].toUpperCase(Locale.ROOT);
            this.levelID = Integer.valueOf(location.split("-")[2]);
        }
    }

    /**
     * Get the notes related to the Product Type
     *
     * @return note
     */
    @Override
    public String getNote() {
        return this.note;
    }

    /**
     * Set the notes related to this Product Type
     *
     * @param note note
     */
    @Override
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Get the description of this Product Type
     *
     * @return description
     */
    @Override
    public String getProductDescription() {
        return this.productDescription;
    }

    /**
     * Set the description of this Product Type
     *
     * @param productDescription productDescription
     */
    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Get the Product Type's Barcode
     *
     * @return barcode
     */
    @Override
    public String getBarCode() {
        return this.barcode;
    }

    /**
     * Set the Product Type's Barcode
     *
     * @param barCode barCode
     */
    @Override
    public void setBarCode(String barCode) {
        this.barcode = barCode;
    }

    /**
     * Get the pricePerUnit value for this Product Type
     *
     * @return pricePerUnit
     */
    @Override
    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    /**
     * Set the pricePerUnit value for this Product Type
     *
     * @param pricePerUnit pricePerUnit
     */
    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Get the Product Type's ID
     *
     * @return ID
     */
    @Override
    public Integer getId() {
        return this.ID;
    }

    /**
     * Set the Product Type ID
     *
     * @param id id
     */
    @Override
    public void setId(Integer id) {
        this.ID = id;
    }

    /**
     * Create a new Product Type inside the ProductTypeDB
     *
     * @param description  description
     * @param productCode  productCode
     * @param pricePerUnit pricePerUnit
     * @param note         note
     * @return The ID of the new ProductType (or -1 in case of failure)
     * @throws InvalidProductDescriptionException if the product description is null or empty.
     * @throws InvalidProductCodeException        if the product id is less than or equal to 0 or if it is null.
     * @throws InvalidPricePerUnitException       if the price per unit si less than or equal to 0.
     */
    public static Integer createProductType(String description, String productCode, double pricePerUnit, String note)
            throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {

        /* InvalidProductDescriptionException */
        if (description == null || description.equals(""))
            throw new InvalidProductDescriptionException();

        /* InvalidProductCodeException */
        if (productCode == null || productCode.equals("")) {
            throw new InvalidProductCodeException();
        }
        try {
            Double.parseDouble(productCode);
        } catch (NumberFormatException nfe) {
            throw new InvalidProductCodeException();
        }

        /* InvalidPricePerUnitException */
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        ProductTypeModel product = new ProductTypeModel();
        ProductTypeDAO prodDao = new ProductTypeDAO();
        product.setProductDescription(description);
        product.setBarCode(productCode);

        if (!GTIN13Check(product.getBarCode()))
            throw new InvalidProductCodeException();

        product.setPricePerUnit(pricePerUnit);
        product.setNote(note);
        product.setQuantity(0);
        product.setLocation(null);
        try {
            ProductTypeModel tmp = (ProductTypeModel) prodDao.getProductByBarcode(product.getBarCode());

            if (tmp == null) {
                product = (ProductTypeModel) prodDao.addProductType(product);
                return product.getId();
            }

            return -1;
        } catch (InvalidDAOParameterException | MissingDAOParameterException mpe) {
            mpe.printStackTrace();
            return -1;
        }
    }

    /**
     * Update an existing Product inside the ProductTypeDB
     *
     * @param id             id
     * @param newDescription newDescription
     * @param newCode        newCode
     * @param newPrice       newPrice
     * @param newNote        newNote
     * @return whether the operation has been completed or not
     * @throws InvalidProductIdException          if the product id is less than or equal to 0 or if it is null.
     * @throws InvalidProductDescriptionException if the product description is null or empty.
     * @throws InvalidProductCodeException        if the product code is null or empty, if it is not a number or if it is not a valid barcode.
     * @throws InvalidPricePerUnitException       if the price per unit si less than or equal to 0.
     */
    public static boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
            throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {

        /* InvalidProductIdException */
        if (id == null || id <= 0)
            throw new InvalidProductIdException();

        /* InvalidProductDescriptionException */
        if (newDescription == null || newDescription.equals(""))
            throw new InvalidProductDescriptionException();

        /* InvalidProductCodeException */
        if (newCode == null || newCode.equals("") || newCode.length() < 12 || newCode.length() > 14)
            throw new InvalidProductCodeException();
        try {
            Double.parseDouble(newCode);
        } catch (NumberFormatException nfe) {
            throw new InvalidProductCodeException();
        }

        /* InvalidPricePerUnitException */
        if (newPrice <= 0)
            throw new InvalidPricePerUnitException();

        ProductTypeDAO prodDao = new ProductTypeDAO();
        ProductTypeModel product;
        try {
            product = (ProductTypeModel) prodDao.getProductById(id);
            if (product == null)
                return false;
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }

        /* Filling just the necessary fields of product */
        product.setProductDescription(newDescription);
        product.setBarCode(newCode);
        if (!GTIN13Check(product.getBarCode()))
            throw new InvalidProductCodeException();
        product.setPricePerUnit(newPrice);
        product.setNote(newNote);

        try {
            return prodDao.updateProductType(product);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * Remove an existing Product from the ProductTypeDB
     *
     * @param id id
     * @return whether the operation has been completed or not
     * @throws InvalidProductIdException if the product id is less than or equal to 0 or if it is null.
     */
    public static boolean deleteProductType(Integer id)
            throws InvalidProductIdException {
        /* InvalidProductIdException */
        if (id == null || id <= 0)
            throw new InvalidProductIdException();

        ProductTypeDAO prodDao = new ProductTypeDAO();

        try {
            if (prodDao.getProductById(id) == null)
                return false;
            return prodDao.removeProductType(id);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * Get a List of ProductType
     *
     * @return the complete list of ProductType
     */
    public static List<ProductType> getAllProductTypes() {
        ProductTypeDAO prodDao = new ProductTypeDAO();
        return prodDao.getProducts();
    }

    /**
     * Get a Product by a given barCode
     *
     * @param barCode barCode
     * @return the product which barCode correspond to the passed one
     * @throws InvalidProductCodeException if the product id is less than or equal to 0 or if it is null.
     */
    public static ProductType getProductTypeByBarCode(String barCode)
            throws InvalidProductCodeException {
        /* InvalidProductCodeException */
        if (barCode == null || barCode.equals(""))
            throw new InvalidProductCodeException();
        try {
            Double.parseDouble(barCode);
        } catch (NumberFormatException nfe) {
            throw new InvalidProductCodeException();
        }

        ProductTypeDAO prodDao = new ProductTypeDAO();
        ProductTypeModel product = new ProductTypeModel();
        product.setBarCode(barCode);

        if (!GTIN13Check(product.getBarCode()))
            throw new InvalidProductCodeException();

        List<ProductType> prodList = prodDao.getProducts();
        for (ProductType p : prodList)
            if (p.getBarCode().equals(barCode))
                return p;
        return null;
    }

    /**
     * Get all the ProductType which descriptions contain the string received as parameter
     *
     * @param description description
     * @return the list of ProductType containing all the Products which description contains
     * the string passed as parameter
     */
    public static List<ProductType> getProductTypesByDescription(String description) {
        ProductTypeDAO prodDao = new ProductTypeDAO();
        List<ProductType> prodList = prodDao.getProducts();
        List<ProductType> out = new ArrayList<>();

        if (description == null)
            return prodList;

        for (ProductType p : prodList)
            if (p.getProductDescription().contains(description))
                out.add(p);
        return out;
    }

    /**
     * Updates the quantity of a given Product identified by its ID.
     * <toBeAdded> can be negative but the final updated quantity cannot be negative.
     * The product should have a location assigned to it.
     *
     * @param productId productId
     * @param toBeAdded toBeAdded
     * @return whether the operation has been completed or not
     * @throws InvalidProductIdException if the product id is less than or equal to 0 or if it is null.
     */
    public static boolean updateQuantity(Integer productId, int toBeAdded)
            throws InvalidProductIdException {
        /* InvalidProductIdException */
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();

        if (toBeAdded == 0)
            return true;

        ProductTypeDAO prodDao = new ProductTypeDAO();
        ProductTypeModel product;
        try {
            product = (ProductTypeModel) prodDao.getProductById(productId);
            if (product == null)
                return false;
            if (product.getQuantity() + toBeAdded < 0)
                return false;
            product.setQuantity(product.getQuantity() + toBeAdded);

            /* Filling just the necessary fields of product */
            product.setProductDescription(null);
            product.setBarCode(null);
            product.setPricePerUnit(null);
            product.setNote(null);
            product.setDiscountRate(null);

            return prodDao.updateProductType(product);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * @param productId productId
     * @param newPos    newPos
     * @return whether the operation has been completed or not
     * @throws InvalidProductIdException if the product id is less than or equal to 0 or if it is null.
     * @throws InvalidLocationException  if the product location is in an invalid format (not --).
     */
    public static boolean updatePosition(Integer productId, String newPos)
            throws InvalidProductIdException, InvalidLocationException {
        /* InvalidProductIdException */
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();

        /* InvalidLocationException */
        if (newPos == null)
            throw new InvalidLocationException();
        Pattern pattern = Pattern.compile("^[0-9]+-[a-zA-Z]-[0-9]+$");
        Matcher matcher = pattern.matcher(newPos);
        if (!matcher.matches())
            throw new InvalidLocationException();

        ProductTypeDAO prodDao = new ProductTypeDAO();
        ProductTypeModel product;
        try {
            product = (ProductTypeModel) prodDao.getProductById(productId);
            if (product.getLocation() != null && product.getLocation().equals(newPos))
                return true;
            product.setLocation(newPos);

            /* Filling just the necessary fields of product */
            product.setProductDescription(null);
            product.setBarCode(null);
            product.setPricePerUnit(null);
            product.setNote(null);
            product.setDiscountRate(null);
            product.setQuantity(null);

            return prodDao.updateProductType(product);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * The GTIN-13 Check Algorithm is used to know
     * whether a barcode is valid or not.
     *
     * @param barCode barCode
     * @return whether the barcode is valid or not.
     * @throws InvalidProductCodeException if the product code is null or empty, if it is not a number or if it is not a valid barcode.
     */
    public static boolean GTIN13Check(String barCode) throws InvalidProductCodeException {
        if (barCode == null)
            throw new InvalidProductCodeException();

        Integer[] barCodeArray = new Integer[barCode.length()];
        Integer[] GTIN13CheckResult = new Integer[barCodeArray.length];
        Integer GTIN13CheckAccumulator = 0;
        int higherMT;
        long tmpAccumulator;
        long barCodeInt;
        int j = 0;

        try {
            barCodeInt = Long.parseLong(barCode);
        } catch (NumberFormatException nfe) {
            throw new InvalidProductCodeException();
        }

        if (barCode.length() < 12 || barCode.length() > 14)
            throw new InvalidProductCodeException();

        for (int i = barCode.length() - 1; i >= 0; i--) {
            tmpAccumulator = barCodeInt % 10;
            barCodeArray[i] = (int) tmpAccumulator;
            barCodeInt /= 10;
        }

        for (int i = barCodeArray.length - 2; i >= 0; i--, j++)
            if (j % 2 == 0)
                GTIN13CheckResult[i] = barCodeArray[i] * 3;
            else
                GTIN13CheckResult[i] = barCodeArray[i];

        for (int i = 0; i < barCodeArray.length - 1; i++)
            GTIN13CheckAccumulator += GTIN13CheckResult[i];

        if (GTIN13CheckAccumulator % 10 == 0)
            return barCodeArray[barCodeArray.length - 1] == 0;
        higherMT = GTIN13CheckAccumulator / 10;
        higherMT++;
        higherMT *= 10;

        return (higherMT - GTIN13CheckAccumulator) == barCodeArray[barCodeArray.length - 1];
    }
}
