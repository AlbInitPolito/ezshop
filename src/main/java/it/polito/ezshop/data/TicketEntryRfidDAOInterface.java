package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface TicketEntryRfidDAOInterface {

    /** -----------------------------------------------------------------------------------------------------------------
     * This method creates a new product_in_transaction_rfid entry in DB
     *
     * @param rfid the product rfid, must be not null and of length <= 12,
     * @param saleId the sale id, must be not null and > 0
     *
     * @return true if add succeeds,
     *         false if rfid does not exists or add goes wrong
     *
     * @throws MissingDAOParameterException if rfid is null
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if rfid length is > 12
     * @throws InvalidDAOParameterException if saleId is <= 0
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean addTicketEntryRfid(String rfid, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes a product_in_transaction_rfid entry from DB
     *
     * @param rfid the product rfid, must be not null and of length <= 12,
     * @param saleId the id of sale transaction, must be not null and > 0
     *
     * @return true if nothing goes wrong,
     *         false if there are problems with db
     *
     * @throws MissingDAOParameterException if rfid is null
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if rfid length is > 12
     * @throws InvalidDAOParameterException if saleId is <= 0
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean removeTicketEntryRfid(String rfid, Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------
     * This method returns the product_in_transaction_rfid entries in DB of given SaleTransaction
     *
     * @param saleId the id of sale transaction, must be not null and > 0
     *
     * @return list of all rfids in given sale transaction id in DB. List can be empty
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * ---------------------------------------------------------------------------------------------------
     */
    List<String> getSaleTicketEntriesRfid(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes all product_in_transaction_rfid entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean resetTicketEntryRfid();
}
