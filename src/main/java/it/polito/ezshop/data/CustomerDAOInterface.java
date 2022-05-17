package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.LoyaltyCardModel;

import java.util.List;

public interface CustomerDAOInterface {

    /** ---------------------------------------------------------------------------
     * This method creates a new Customer entry in DB
     *
     * @param customer .customerName the customer name, must be not null and of length <= 80
     *
     * @return Customer object with new id and specified name,
     *         null if insert goes wrong
     *
     * @throws MissingDAOParameterException if customer is null
     * @throws MissingDAOParameterException if customer.customerName is null
     * @throws InvalidDAOParameterException if customer.customerName is of length > 80
     * ----------------------------------------------------------------------------
     */
    Customer addCustomer(Customer customer) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------
     * This method removes a Customer entry from DB
     *
     * @param customerId the id of Customer to remove, must be not null and > 0
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * @throws MissingDAOParameterException if customerId is null
     * @throws InvalidDAOParameterException if customerId is <= 0
     * --------------------------------------------------------------
     */
    boolean removeCustomer(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------------------
     * This method changes a Customer entry for given parameters only
     *
     * @param customer .Id the id of the customer, must be not null and > 0,
     *                 .customerName the new customer name, must be not empty if passed (not null),
     *                 .customerCard the new customer card, must be of length <= 10 if passed (not null)
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if customer is null
     * @throws MissingDAOParameterException if customer.customerID is null
     * @throws MissingDAOParameterException if both customer.customerName and customer.customerCard are null
     * @throws InvalidDAOParameterException if customer.customerID is <= 0
     * @throws InvalidDAOParameterException if customer.customerCard is not null and its length is > 10
     * @throws InvalidDAOParameterException if customer.customerName is not null and of length > 80
     * --------------------------------------------------------------------------------------------------------------
     */
    boolean updateCustomer(Customer customer) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of Customer in DB
     *
     * @return list of all Customer in DB. List can be empty
     * --------------------------------------------------------------
     */
    List<Customer> getCustomers();

    /** -----------------------------------------------------------------------
     * This method returns the Customer in DB with given id
     *
     * @param customerId the id of the customer, must be not null and > 0
     *
     * @return customer with corresponding id in DB,
     *         null if Id is null or <= 0
     *
     * @throws MissingDAOParameterException if customerId is null
     * @throws InvalidDAOParameterException if customerId is <= 0
     * -----------------------------------------------------------------------
     */
    Customer getCustomer(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------------
     * This method returns the Customer in DB with given customer card serial number
     *
     * @param serialNumber the serialNumber of the customer loyalty card, must be not null and of length <= 10
     *
     * @return customer with corresponding loyalty card in DB,
     *         null if serialNumber is null or of length 10 or if customer doesn't exist
     *
     * @throws MissingDAOParameterException if serialNumber is null
     * @throws InvalidDAOParameterException if serialNumber is not null and its length > 10
     * --------------------------------------------------------------------------------------------------
     */
    Customer getCustomerByCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------------------
     * This method removes the loyalty_card from a Customer entry of given id
     *
     * @param customerId the id of the customer, must be not null and > 0
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if customerId is null
     * @throws MissingDAOParameterException if customerId is <= 0
     * --------------------------------------------------------------------------------------------------------------
     */
    boolean detachCustomerCard(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------
     * This method removes all Customer entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ---------------------------------------------------------
     */
    boolean resetCustomers();
}
