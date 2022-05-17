package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface ProductEntryDAOInterface {

    /** --------------------------------------------------------------------------------------------------
     * This method creates a new product_entry entry in DB
     *
     * @param barcode the product barcode, must be not null and of length <= 14,
     * @param rfid the product rfid, must be not null and of length <= 12
     *
     * @return true if insert succeeds
     *         false if insert goes wrong or if barcode does not exists or if rfid already exists
     *
     * @throws MissingDAOParameterException if barcode is null
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if barcode length is > 14
     * @throws InvalidDAOParameterException if rfid length is > 12
     * --------------------------------------------------------------------------------------------------
     */
    boolean addProductEntry(String barcode, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------------------------------------------
     * This method changes a product_entry entry for given parameters only
     *
     * @param rfid the product rfid, must be not null and of length <= 12
     * @param available the product availability
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if rfid is of length > 12
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean updateProductEntry(String rfid, boolean available) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of product_entry in DB for given barcode
     *
     * @param barcode the product barcode, must be not null and of length <= 14
     *
     * @return list of all product_entries rfids in DB. List can be empty
     *         null if barcode does not exist
     *
     * @throws MissingDAOParameterException if barcode is null
     * @throws InvalidDAOParameterException if barcode length is > 14
     * --------------------------------------------------------------
     */
    List<String> getProductEntries(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException;;

    /** -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given id
     *
     * @param rfid the rfid of the product, must be not null and of length <= 12
     *
     * @return barcode of corresponding rfid in DB,
     *         null if rfid doesn't exist
     *
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if rfid length is > 12
     * -----------------------------------------------------------------------
     */
    String getProductEntryBarcode(String rfid) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given id
     *
     * @param rfid the rfid of the product, must be not null and of length <= 12
     *
     * @return availability of corresponding rfid in DB (0 not available, 1 avaialble),
     *         null if rfid doesn't exist
     *
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if rfid length is > 12
     * -----------------------------------------------------------------------
     */
    Integer getProductEntryAvailability(String rfid) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of available product_entry in DB for given barcode
     *
     * @param barcode the product barcode, must be not null and of length <= 14
     *
     * @return list of all available product_entries rfids in DB. List can be empty
     *         null if barcode does not exist
     *
     * @throws MissingDAOParameterException if barcode is null
     * @throws InvalidDAOParameterException if barcode length is > 14
     * --------------------------------------------------------------
     */
    List<String> getAvailableProductEntries(String barcode) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method removes all product_entry entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * --------------------------------------------------------------
     */
    boolean resetProductEntries();
}
