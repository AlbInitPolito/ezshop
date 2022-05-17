package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface ReturnProductRfidDAOInterface {

    /** -----------------------------------------------------------------------------------------------------------------
     * This method creates a new return_product_rfid entry in DB
     *
     * @param returnId the return transaction id, must be not null and > 0,
     * @param rfid the product rfid, must be not null and of length <= 12
     *
     * @return true if product is added correctly,
     *         false if product rfid doesn't exist or add fails
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * @throws InvalidDAOParameterException if rfid length is > 12
     * -----------------------------------------------------------------------------------------------------------------
     */
    boolean addReturnProductRfid(Integer returnId, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------
     * This method returns the list of rfids in DB with given return transaction id
     *
     * @param returnId the id of return transaction related to return_product_rfid, must be not null and > 0
     *
     * @return list of all TicketEntry with given sale transaction id in DB. List can be empty
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * --------------------------------------------------------------------------------------------
     */
    List<String> getReturnProductRfids(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /**
     * -------------------------------------------------------------------------------------
     * @param returnId the id of return transaction related to return_product_rfid, must be not null and > 0
     * @param rfid the product rfid to remove
     *
     * @return true if delete succeeds
     *         false if delete fails
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if rfid is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * @throws InvalidDAOParameterException if rfid length is > 12
     * ------------------------------------------------------------------------------------------
     */
    public boolean removeReturnProductRfid(Integer returnId, String rfid) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ---------------------------------------------------------------------------------------------------------------
     * This method removes all return_product_rfid entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ----------------------------------------------------------------------------------------------------------------
     */
    boolean resetReturnProductRfid();
}
