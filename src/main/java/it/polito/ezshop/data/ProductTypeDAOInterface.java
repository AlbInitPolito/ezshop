package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface ProductTypeDAOInterface {

    /** --------------------------------------------------------------------------------------------------
     * This method creates a new ProductType entry in DB
     *
     * @param product .barcode the product barcode, must be not null and of length <= 14,
     *                .price the product price, must be not null and > 0,
     *                .description the product description, must be of length <= 200 if passed,
     *                .quantity the product quantity, must be >= 0 if passed,
     *                .note the product notes, must be of length <= 200 if passed,
     *                .location the product location, must be of format ^[0-9]+-[a-zA-Z]-[0-9]+$
     *
     * @return ProductType object with new id and specified barcode and price and description if passed,
     *                                 quantity if passed note if passed location if passed,
     *         null if insert goes wrong
     *
     * @throws MissingDAOParameterException if product is null
     * @throws MissingDAOParameterException if product.price is null
     * @throws MissingDAOParameterException if product.barcode is null
     * @throws InvalidDAOParameterException if product.barcode length is > 14
     * @throws InvalidDAOParameterException if product.quantity is not null and < 0
     * @throws InvalidDAOParameterException if product.description is not null and of length > 200
     * @throws InvalidDAOParameterException if product.note is not null of length > 200
     * --------------------------------------------------------------------------------------------------
     */
    ProductType addProductType(ProductType product) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method removes a ProductType entry from DB
     *
     * @param productId the id of ProductType to remove, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if productId is null
     * @throws InvalidDAOParameterException if productId is <= 0
     * --------------------------------------------------------------
     */
    boolean removeProductType(Integer productId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------------------------------------------
     * This method changes a ProductType entry for given parameters only
     *
     * @param product .ID the product id, must be not null and > 0,
     *                .barcode the product barcode, must be of length <= 14 if passed (not null),
     *                .pricePerUnit the new product price, must be > 0 if passed (not null),
     *                .description the new product description, must be of length <= 200 if passed,
     *                .quantity the new product quantity, must be >= 0 if passed (not null),
     *                .note the new product note, must be of length <= 200 if passed,
     *                .location location the product location, must be of format ^[0-9]+-[a-zA-Z]-[0-9]+$ if passed
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if product is null
     * @throws MissingDAOParameterException if product.ID is null
     * @throws MissingDAOParameterException if no other parameter than ID is passed
     * @throws InvalidDAOParameterException if product.barcode is of length > 14
     * @throws InvalidDAOParameterException if product.ID is <= 0
     * @throws InvalidDAOParameterException if product.quantity is not null and < 0
     * @throws InvalidDAOParameterException if product.location is not of right format
     * @throws InvalidDAOParameterException if product.description is not null and of length > 200
     * @throws InvalidDAOParameterException if product.note is not null and of length > 200
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean updateProductType(ProductType product) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of ProductType in DB
     *
     * @return list of all ProductType in DB. List can be empty
     * --------------------------------------------------------------
     */
    List<ProductType> getProducts();


    /* -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given id
     *
     * @param product.ID the id of the product, must be not null and > 0
     *
     * @return ProductType with corresponding id in DB
     *         null if id is null or <= 0
     * -----------------------------------------------------------------------
     */
    //ProductType getProduct(ProductType product);

    /** -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given id
     *
     * @param productId the id of the product, must be not null and > 0
     *
     * @return ProductType with corresponding id in DB,
     *         null if product doesn't exist
     *
     * @throws MissingDAOParameterException if productId is null
     * @throws InvalidDAOParameterException if productIt is <= 0
     * -----------------------------------------------------------------------
     */
    ProductType getProductById(Integer productId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given Barcode
     *
     * @param barcode the barcode of the product, must be not null and of length <= 14
     *
     * @return ProductType with corresponding barcode in DB,
     *         null if product doesn't exist
     *
     * @throws MissingDAOParameterException if barcode is null
     * @throws InvalidDAOParameterException if barcode length is > 14
     * -----------------------------------------------------------------------
     */
    ProductType getProductByBarcode(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method removes all ProductType entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * --------------------------------------------------------------
     */
    boolean resetProductTypes();
}
