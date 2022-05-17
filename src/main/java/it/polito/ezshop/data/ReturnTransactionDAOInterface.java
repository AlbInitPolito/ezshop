package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ReturnTransactionModel;

public interface ReturnTransactionDAOInterface {

    /** ----------------------------------------------------------------
     * This method returns the max id from ReturnTransaction table in DB
     *
     * @return max id,
     *         null if something goes wrong
     * ----------------------------------------------------------------
     */
    public Integer getMaxReturnTransactionId();

    /** --------------------------------------------------------------------------------------------------------------
     * This method creates a new ReturnTransaction entry in DB
     *
     * @param ret .ID the id of the return transaction, must be and > 0 if passed (not null),
     *            .transactionID the id of the sale transaction, must be not null and > 0
     *
     * @return ReturnTransaction object with new id, null returnedValue, given transactionID if ret.ID is null,
     *         ReturnTransaction object with given id, null returnedValue, given transactionID if ret.ID is not null
     *
     * @throws MissingDAOParameterException if ret is null
     * @throws MissingDAOParameterException if ret.transactionID is <= 0
     * @throws InvalidDAOParameterException if ret.transactionID is <= 0
     * @throws InvalidDAOParameterException if ret.ID is not null and <= 0
     * --------------------------------------------------------------------------------------------------------------
     */
    ReturnTransactionModel addReturnTransaction(ReturnTransactionModel ret) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method removes a ReturnTransaction entry from DB
     *
     * @param transactionID the id of return transaction to remove, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if transactionID is null
     * @throws InvalidDAOParameterException if transactionID is <= 0
     * --------------------------------------------------------------
     */
    boolean removeReturnTransaction(Integer transactionID) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------------------------------------
     * This method changes a ReturnTransaction entry for given parameters only
     *
     * @param transaction .ID the return transaction id, must be not null and > 0,
     *                    .returnedValue the product value, must be > 0 if passed (not null)
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if transaction is null
     * @throws MissingDAOParameterException if transaction.ID is null
     * @throws MissingDAOParameterException if transaction.returnedValue is null
     * @throws InvalidDAOParameterException if transaction.ID is <= 0
     * -------------------------------------------------------------------------------------------------------------------------
     */
    boolean updateReturnTransaction(ReturnTransactionModel transaction) throws MissingDAOParameterException, InvalidDAOParameterException;

    /* -------------------------------------------------------------------------------------------------------------------------
     * This method changes a ReturnTransaction entry for given parameters only
     *
     * @param returnId, must be > 0
     * @param balanceId the return transaction id, must be not null and > 0
     *
     * @return true if update succeeds
     *         false if update fails or if id is null or <= 0 or if balanceId is <= 0
     * -------------------------------------------------------------------------------------------------------------------------
     */
    //boolean setReturnBalanceOperation(Integer returnId, int balanceId);

    /** -----------------------------------------------------------------------
     * This method returns the ProductType in DB with given id
     *
     * @param returnId the id of the return transaction, must be not null and > 0
     *
     * @return ReturnTransactionModel object with corresponding id in DB,
     *         null if return transaction doesn't exist
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws InvalidDAOParameterException if returnId is <= 0
     * -----------------------------------------------------------------------
     */
    ReturnTransactionModel getReturnTransaction(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------
     * This method sets the balance operation field of a ReturnTransaction in DB
     *
     * @param balanceId, must be > 0
     * @param returnId the return transaction id, must be not null and > 0
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if returnId is null
     * @throws MissingDAOParameterException if balanceId is <= 0
     * @throws InvalidDAOParameterException if balanceId is <= 0
     * -------------------------------------------------------------------------------
     */
    boolean setReturnBalanceOperation(Integer returnId, int balanceId ) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------
     * This method returns the BalanceOperation with only id of the ReturnTransaction with given id
     *
     * @param returnId the id of the return transaction, must be not null and > 0
     *
     * @return BalanceOperation of the corresponding ReturnTransaction in DB,
     *         null if sale balance operation doesn't exist
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * -------------------------------------------------------------------------------------------
     */
    public BalanceOperation getReturnBalanceOperation(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException;


    /** -------------------------------------------------------------
     * This method removes all ReturnTransaction entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * --------------------------------------------------------------
     */
    boolean resetReturnTransactions();
}
