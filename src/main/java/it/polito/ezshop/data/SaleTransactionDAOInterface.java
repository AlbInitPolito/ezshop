package it.polito.ezshop.data;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface SaleTransactionDAOInterface {

    /** ----------------------------------------------------------------
     * This method returns the max id from SaleTransaction table in DB
     *
     * @return max id
     *         null if something goes wrong
     * ----------------------------------------------------------------
     */
    Integer getMaxSaleTransactionId();

    /** ----------------------------------------------------------------------------------------------------
     * This method creates a new SaleTransaction entry in DB
     *
     * @param sale .ID the sale transaction id, must be not null and > 0 if sale is not null,
     *             .cost the sale transaction total cost,
     *             .paymentType the payment type, must be >= 0 when passed (not null) if sale is not null,
     *             .discountRate the sale discount
     *
     * @return SaleTransaction object with new id and Date and Time of insert if sale is null,
     *         SaleTransaction object with given id, cost, paymentType, discount,
     *                                and Date and Time of insert if sale is not null
     *
     * @throws MissingDAOParameterException if sale is not null and sale.ID is null
     * @throws InvalidDAOParameterException if sale is not null and sale.ID is <= 0
     * @throws InvalidDAOParameterException if sale is not null and sale.paymentType is not null and < 0
     * -----------------------------------------------------------------------------------------------------
     */
    SaleTransaction addSaleTransaction(SaleTransaction sale) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------
     * This method removes a SaleTransaction entry from DB
     *
     * @param saleId the id of SaleTransaction to remove, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * -----------------------------------------------------------------------------
     */
    boolean removeSaleTransaction(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ------------------------------------------------------------------------------------------
     * This method changes a SaleTransaction entry for given parameters only
     *
     * @param sale .ID the sale transaction id, must be not null and > 0,
     *             .price the sale transaction total cost, is considered only if > 0
     *             .paymentType the payment type, must be >= 0 if passed (not null),
     *             .discount the sale discount, is considered only if >= 0
     *             .date the sale date,
     *             .time the sale time, must be not null if date is not null
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if sale is null
     * @throws MissingDAOParameterException if sale.ID is null
     * @throws MissingDAOParameterException if sale.paymentType, sale.discount, sale.date, sale.price are all null
     * @throws MissingDAOParameterException if sale.time is null and sale.date is not null
     * @throws InvalidDAOParameterException if sale.ID is <= 0
     * @throws InvalidDAOParameterException if sale.paymentType is not null and < 0
     * ------------------------------------------------------------------------------------------
     */
    boolean updateSaleTransaction(SaleTransaction sale) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ------------------------------------------------------------------------
     * This method returns the SaleTransaction in DB with given id
     *
     * @param saleId the id of the sale transaction, must be not null and > 0
     *
     * @return SaleTransaction with corresponding id in DB,
     *         null if sale transaction doesn't exist
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * ------------------------------------------------------------------------
     */
    SaleTransaction getSaleTransaction(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method returns the list of SaleTransaction in DB
     *
     * @return list of all SaleTransaction in DB. List can be empty
     * -------------------------------------------------------------
     */
    List<SaleTransaction> getSaleTransactions();

    /** -------------------------------------------------------------------------------
     * This method sets the balance operation field of a SaleTransaction in DB
     *
     * @param balanceId, must be > 0
     * @param saleId the sale transaction id, must be not null and > 0
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws MissingDAOParameterException if balanceId is <= 0
     * @throws InvalidDAOParameterException if balanceId is <= 0
     * -------------------------------------------------------------------------------
     */
    boolean setSaleBalanceOperation(Integer saleId, int balanceId ) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------
     * This method returns the BalanceOperation with only id of the SaleTransaction with given id
     *
     * @param saleId the id of the sale transaction, must be not null and > 0
     *
     * @return BalanceOperation of the corresponding SaleTransaction in DB,
     *         null if sale balance operation doesn't exist
     *
     * @throws MissingDAOParameterException if saleId is null
     * @throws InvalidDAOParameterException if saleId is <= 0
     * -------------------------------------------------------------------------------------------
     */
    public BalanceOperation getSaleBalanceOperation(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -----------------------------------------------------------------------------
     * This method removes a SaleTransaction entry from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * -----------------------------------------------------------------------------
     */
    boolean resetSaleTransactions();
}
