package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface TicketEntryDAOInterface {

    /** -----------------------------------------------------------------------------------------------------------------
     * This method creates a new TicketEntry entry in DB
     *
     * @param entry .barCode the product barcode, must be not null and of length <= 14,
     *              .discountRate the product discount,
     *              .amount the product quantity, must be >= 0
     * @param saleId the sale id, must be not null and > 0
     *
     * @return TicketEntry object with product description, price, and ticket quantity, discount if didn't already exist,
     *         TicketEntry object of already registered ticket entry and updated quantity if already exists,
     *         null if product barcode doesn't exist or if update goes wrong
     *
     * @throws MissingDAOParameterException if entry is null
     * @throws MissingDAOParameterException if entry.barCode is null
     * @throws MissingDAOParameterException if saleId is null
     * @throws MissingDAOParameterException if entry.amount is < 0
     * @throws InvalidDAOParameterException if entry.barCode length is < 14
     * @throws InvalidDAOParameterException if saleId is <= 0
     * -----------------------------------------------------------------------------------------------------------------
     */
    TicketEntry addTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes a TicketEntry entry from DB
     *
     * @param entry .barCode the barcode of ProductType, must be not null and of length <= 14,
     * @param saleId the id of sale transaction related to TicketEntry, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails or if barCode doesn't exist
     *
     * @throws MissingDAOParameterException if entry is null
     * @throws MissingDAOParameterException if entry.barCode is null
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if entry.barCode length is > 14
     * @throws InvalidDAOParameterException if saleId is <= 0
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean removeTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------------------------------------------
     * This method changes a TicketEntry entry for given parameters only
     *
     * @param entry .barCode the barcode of ProductType, must be not null and of length <= 14,
     *              .discountRate the new product discount, is considered only if >= 0,
     *              .amount the new product quantity, is considered only if > 0
     * @param saleId the id of sale transaction related to TicketEntry, must be not null and > 0
     *
     * @return true if update succeeds,
     *         false if update fails or if barCode doesn't exist
     *
     * @throws MissingDAOParameterException if entry is null
     * @throws MissingDAOParameterException if saleId is null
     * @throws MissingDAOParameterException if entry.barCode is null
     * @throws MissingDAOParameterException if entry.discountRate and entry.amount are both null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * @throws InvalidDAOParameterException if entry.barCode length is > 14
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean updateTicketEntry(TicketEntry entry, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------
     * This method returns the TicketEntry in DB of given SaleTransaction
     *
     * @param barcode the barcode of ProductType, must be not null and of length <= 14,
     * @param saleId the id of sale transaction related to TicketEntry, must be not null and > 0
     *
     * @return TicketEntry with corresponding sale ID and product barCode in DB,
     *         null if barCode doesn't exist or TicketEntry doesn't exist
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws MissingDAOParameterException if barcode is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * @throws InvalidDAOParameterException if barcode length is > 14
     * ---------------------------------------------------------------------------------------------------
     */
    TicketEntry getTicketEntry(String barcode, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    //List<TicketEntry> getTicketEntries();

    /** -------------------------------------------------------------------------------------------
     * This method returns the list of TicketEntry in DB with given sale transaction id
     *
     * @param saleId the id of sale transaction related to TicketEntry, must be not null and > 0
     *
     * @return list of all TicketEntry with given sale transaction id in DB. List can be empty
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * --------------------------------------------------------------------------------------------
     */
    List<TicketEntry> getSaleTicketEntries(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes a TicketEntry entry from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean resetTicketEntries();
}
