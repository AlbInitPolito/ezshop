package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface ReturnProductDAOInterface {

    /** -----------------------------------------------------------------------------------------------------------------
     * This method creates a new return_product entry in DB
     *
     * @param entry .barCode the product barcode, must be not null and of length <= 14,
     *              .amount the product quantity to return, must be not null and > 0
     * @param returnId the return transaction id, must be not null and > 0
     *
     * @return TicketEntry object with product quantity if didn't already exist,
     *         TicketEntry object of already registered return_product entry and updated quantity if already exists,
     *         null if product barcode doesn't exist or if update goes wrong
     *
     * @throws MissingDAOParameterException if entry is null
     * @throws MissingDAOParameterException if entry.barCode is null
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if entry.amount is <= 0
     * @throws InvalidDAOParameterException if entry.barCode length is > 14
     * @throws InvalidDAOParameterException if returnId is <= 0
     * -----------------------------------------------------------------------------------------------------------------
     */
    TicketEntry addReturnProduct(TicketEntry entry, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes a return_product entry from DB
     *
     * @param barcode the barcode of ProductType, must be not null and of length <= 14,
     * @param returnId the id of return transaction related to return_product, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails or if barCode doesn't exist
     *
     * @throws MissingDAOParameterException if barcode is null
     * @throws MissingDAOParameterException if returnId is null
     * @throws InvalidDAOParameterException if barcode length is > 14
     * @throws InvalidDAOParameterException if returnId is <= 0
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean removeReturnProduct(String barcode, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------------------------------------------
     * This method changes a return_product entry for given parameters only
     *
     * @param entry .barCode the barcode of ProductType, must be not null and of length <= 14,
     *              .amount the new product quantity to remove
     * @param returnId the id of return transaction related to return_product, must be not null and > 0
     *
     * @return true if update succeeds,
     *         false if update fails or if barCode doesn't exist
     *
     * @throws MissingDAOParameterException if entry is null
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if entry.barCode is null
     * @throws MissingDAOParameterException if entry.amount is <= 0
     * @throws InvalidDAOParameterException if returnId is <= 0
     * @throws InvalidDAOParameterException if entry.barCode length is > 14
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean updateReturnProduct(TicketEntry entry, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------
     * This method returns the return_product in DB of given SaleTransaction
     *
     * @param barcode the barcode of ProductType, must be not null and of length <= 14,
     * @param returnId the id of return transaction related to return_product, must be not null and > 0
     *
     * @return TicketEntry with corresponding return ID and product barcode in DB,
     *         null if barCode doesn't exist or return_product doesn't exist
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if barcode is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * @throws InvalidDAOParameterException if barcode length is > 14
     * ---------------------------------------------------------------------------------------------------
     */
    TicketEntry getReturnProduct(String barcode, Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------
     * This method returns the list of return_product in DB with given return transaction id
     *
     * @param returnId the id of return transaction related to return_product, must be not null and > 0
     *
     * @return list of all TicketEntry with given sale transaction id in DB. List can be empty
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * --------------------------------------------------------------------------------------------
     */
    List<TicketEntry> getReturnProducts(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes all return_product entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean resetReturnProducts();
}
