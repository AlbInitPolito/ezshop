package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface BalanceOperationDAOInterface {

    /** --------------------------------------------------------------------------------------
     * This method creates a new BalanceOperation entry in DB
     *
     * @param operation .Type the BalanceOperation type, must be compliant with BalanceOperationModel.Type,
     *                  .Money the BalanceOperation money, must be > 0
     *
     * @return BalanceOperation object with new id, specified Type and Money, date of insert,
     *         null if insert goes wrong
     *
     * @throws MissingDAOParameterException if operation is null
     * @throws MissingDAOParameterException if operation.Type is null
     * @throws MissingDAOParameterException if operation.Money is <= 0
     * --------------------------------------------------------------------------------------
     */
    BalanceOperation addBalanceOperation(BalanceOperation operation) throws MissingDAOParameterException;

    /** --------------------------------------------------------
     * This method removes a BalanceOperation entry from DB
     *
     * @param balanceId the id of BalanceOperation to remove, must be > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if balanceID is <= 0
     * ---------------------------------------------------------
     */
    boolean removeBalanceOperation(int balanceId) throws MissingDAOParameterException;

    /** -----------------------------------------------------------------------------------------------------------------------------
     * This method changes a BalanceOperation entry for given parameters only
     *
     * @param operation .BalanceId the id of the BalanceOperation, must be > 0,
     *                  .Type the new BalanceOperation type, must be compliant with BalanceOperationModel.Type if  passed (not null),
     *                  .Money the new BalanceOperation money, must be > 0 if passed
     *
     * @return true if update succeeds,
     *         false if update fails or if Type is null and Money is <= 0
     *
     * @throws MissingDAOParameterException if operation is null
     * @throws MissingDAOParameterException if operation.BalanceID is <= 0
     * @throws MissingDAOParameterException if both operation.Type and operation.Money are null
     * -----------------------------------------------------------------------------------------------------------------------------
     */
    boolean updateBalanceOperation(BalanceOperation operation) throws MissingDAOParameterException;

    /** --------------------------------------------------------------
     *This method returns the list of all BalanceOperation in DB
     *
     * @return list of all BalanceOperation in DB. List can be empty
     * --------------------------------------------------------------
     */
    List<BalanceOperation> getBalanceOperations();


    /** -----------------------------------------------------------------------
     * This method returns the BalanceOperation in DB with given id
     *
     * @param balanceId the id of the BalanceOperation, must be > 0
     *
     * @return BalanceOperation with corresponding id in DB,
     *         null if balance operation doesn't exist
     *
     * @throws MissingDAOParameterException if balanceID is <= 0
     * -----------------------------------------------------------------------
     */
    BalanceOperation getBalanceOperation(int balanceId) throws MissingDAOParameterException;

    /** --------------------------------------------------------
     * This method removes all BalanceOperation entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ---------------------------------------------------------
     */
    boolean resetBalanceOperations();
}
