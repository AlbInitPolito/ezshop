package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.util.List;

public interface OrderDAOInterface {

    /** ---------------------------------------------------------------------------------------------------------------------
     * This method creates a new Order entry in DB
     *
     * @param order .price the product price, must be > 0,
     *              .productCode the product barcode, must be not null and of length <= 14,
     *              .quantity the product quantity, must be > 0
     *
     * @return Order object with new id and specified price, product, quantity, null supplier, null balanceId, ISSUED status,
     *         null if insert goes wrong or if product doesn't exist
     *
     * @throws MissingDAOParameterException if order is null
     * @throws MissingDAOParameterException if order.productCode is null
     * @throws MissingDAOParameterException if order.price is <= 0
     * @throws MissingDAOParameterException if order.quantity is <= 0
     * @throws InvalidDAOParameterException if order.productCode length is > 14
     * ---------------------------------------------------------------------------------------------------------------------
     */
    Order addOrder(Order order) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------------------------------------------------------------------
     * This method changes a Order entry for given parameters only
     *
     * @param order .orderID the order id, must be not null and > 0,
     *              .status the new order status, must be compliant with OrderModel.OrderStatus if passed (not null),
     *              .balanceID the new order balance operation, must be > 0 if passed (not null)
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if order is null
     * @throws MissingDAOParameterException if order.orderID is null
     * @throws MissingDAOParameterException if order.balanceID and status are both null
     * @throws InvalidDAOParameterException if order.orderID is <= 0
     * @throws InvalidDAOParameterException if order.balanceID is <= 0
     * --------------------------------------------------------------------------------------------------------------------------
     */
    boolean updateOrder(Order order) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------------
     * This method returns the list of Order in DB
     *
     * @return list of all Order in DB. List can be empty
     * --------------------------------------------------------------
     */
    List<Order> getOrders();

    /** -----------------------------------------------------------------------
     * This method returns the Order in DB with given id
     *
     * @param orderId the id of the order, must be not null and > 0
     *
     * @return Order with corresponding id in DB,
     *         null if order doesn't exist or if related product doesn't exist
     *
     * @throws MissingDAOParameterException if orderId is null
     * @throws InvalidDAOParameterException if orderId is <= 0
     * -----------------------------------------------------------------------
     */
    Order getOrder(Integer orderId) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------
     * This method removes all Order entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ---------------------------------------------------------
     */
    boolean resetOrders();
}
