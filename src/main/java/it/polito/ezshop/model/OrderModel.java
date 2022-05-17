package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.OrderDAO;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderModel implements it.polito.ezshop.data.Order {

    /**
     * ORDER_STATUS
     * This enum contains four different options for the status of an order.
     */
    public enum OrderStatus {
        ISSUED,
        ORDERED,
        PAYED,
        COMPLETED
    }

    private double pricePerUnit;
    private int quantity;
    private OrderStatus status;
    private Integer balanceID;
    private Integer orderID;
    private String productCode;

    /**
     * Constructor
     *
     * @param pricePerUnit pricePerUnit
     * @param quantity     quantity
     * @param status       status
     * @param balanceID    balanceID
     * @param orderID      orderID
     * @param productCode  productCode
     */
    public OrderModel(double pricePerUnit, int quantity, OrderStatus status, Integer balanceID, Integer orderID, String productCode) {
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
        this.balanceID = balanceID;
        this.orderID = orderID;
        this.productCode = productCode;
    }

    /**
     * Constructor
     */
    public OrderModel() {
        this.pricePerUnit = 0.0;
        this.quantity = 0;
        this.status = null;
        this.balanceID = null;
        this.orderID = null;
        this.productCode = null;
    }

    /**
     * Get the balance ID for this Order
     *
     * @return balance ID
     */
    @Override
    public Integer getBalanceId() {
        return this.balanceID;
    }

    /**
     * Set the balance ID for this Order
     *
     * @param balanceId balanceId
     */
    @Override
    public void setBalanceId(Integer balanceId) {
        this.balanceID = balanceId;
    }

    /**
     * Get the product code for this Order
     *
     * @return product code
     */
    @Override
    public String getProductCode() {
        return this.productCode;
    }

    /**
     * Set the product code for this Order
     *
     * @param productCode productCode
     */
    @Override
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Get the price per unit for this Order
     *
     * @return price per unit
     */
    @Override
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    /**
     * Set the price per unit for this Order
     *
     * @param pricePerUnit pricePerUnit
     */
    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Get the quantity (integer value) of Product in this Order
     *
     * @return quantity
     */
    @Override
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Set the quantity (integer value) of Product in this Order
     *
     * @param quantity quantity
     */
    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the status of this Order
     *
     * @return the status of this Order
     */
    @Override
    public String getStatus() {
        if (this.status == null)
            return null;
        return this.status.name();
    }

    /**
     * Set the status of this Order
     *
     * @param status status
     */
    @Override
    public void setStatus(String status) {
        if (status == null)
            this.status = null;
        else
            this.status = OrderStatus.valueOf(status.toUpperCase());
    }

    /**
     * Get the ID number associated to this Order
     *
     * @return orderID
     */
    @Override
    public Integer getOrderId() {
        return this.orderID;
    }

    /**
     * Set the ID number associated to this Order
     *
     * @param orderId orderId
     */
    @Override
    public void setOrderId(Integer orderId) {
        this.orderID = orderId;
    }

    /**
     * Issue a new Order: this contact the DB and returns the orderID
     * if the operation is completed, otherwise it will return -1.
     *
     * @param productCode  productCode
     * @param quantity     quantity
     * @param pricePerUnit pricePerUnit
     * @return The ID of the issued Order
     * @throws InvalidProductCodeException  if the product code is null or empty, if it is not a number or if it is not a valid barcode.
     * @throws InvalidQuantityException     if the quantity is less than or equal to 0.
     * @throws InvalidPricePerUnitException if the price per unit si less than or equal to 0.
     */
    public static Integer issueOrder(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        /* InvalidProductCodeException */
        if (productCode == null || productCode.equals("") || !ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();

        /* InvalidQuantityException */
        if (quantity <= 0)
            throw new InvalidQuantityException();

        /* InvalidPricePerUnitException */
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        OrderDAO orderDAO = new OrderDAO();
        OrderModel order = new OrderModel();

        order.setOrderId(null);
        order.setProductCode(productCode);
        order.setQuantity(quantity);
        order.setPricePerUnit(pricePerUnit);
        order.setStatus(OrderStatus.ISSUED.name());

        try {
            order = (OrderModel) orderDAO.addOrder(order);
        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            return -1;
        }


        if (order == null)
            return -1;

        return order.getOrderId();
    }

    /**
     * Issue a new Order and set its state to PAYED: this contact the DB and returns the orderID
     * if the operation is completed, otherwise it will return -1.
     *
     * @param productCode  productCode
     * @param quantity     quantity
     * @param pricePerUnit pricePerUnit
     * @return The ID of the payed Order
     * @throws InvalidProductCodeException  if the product code is null or empty, if it is not a number or if it is not a valid barcode.
     * @throws InvalidQuantityException     if the quantity is less than or equal to 0.
     * @throws InvalidPricePerUnitException if the price per unit si less than or equal to 0.
     */
    public static Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException {
        /* InvalidProductCodeException */
        if (productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException();
        if (!ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();

        /* InvalidQuantityException */
        if (quantity <= 0)
            throw new InvalidQuantityException();

        /* InvalidPricePerUnitException */
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();
        try {
            OrderDAO orderDAO = new OrderDAO();
            OrderModel order = new OrderModel();
            BalanceOperationDAO bDAO = new BalanceOperationDAO();

            BalanceOperationModel balance = new BalanceOperationModel(0, LocalDate.now(), pricePerUnit * quantity, BalanceOperationModel.Type.ORDER);
            balance = (BalanceOperationModel) bDAO.addBalanceOperation(balance);

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderStatus.ISSUED.name());
            order.setBalanceId(balance.getBalanceId());
            order = (OrderModel) orderDAO.addOrder(order);

            if (order == null)
                return -1;

            order.setStatus(OrderModel.OrderStatus.PAYED.name());
            order.setBalanceId(balance.getBalanceId());

            orderDAO.updateOrder(order);

            return order.getOrderId();
        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            return -1;
        }
    }

    /**
     * Change the OrderStatus of an Order identified by a given orderID
     * from ISSUED to PAYED.
     *
     * @param orderId orderId
     * @return whether the operation has been completed or not
     * @throws InvalidOrderIdException if the order id is less than or equal to 0 or if it is null.
     */
    public static boolean payOrder(Integer orderId)
            throws InvalidOrderIdException {
        /* InvalidOrderIdException */
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();

        try {
            OrderModel order;
            OrderDAO orderDAO = new OrderDAO();
            BalanceOperationDAO bDAO = new BalanceOperationDAO();

            order = (OrderModel) orderDAO.getOrder(orderId);

            if (order == null)
                return false;

            BalanceOperationModel balance = new BalanceOperationModel(0, LocalDate.now(), order.getPricePerUnit() * order.getQuantity(), BalanceOperationModel.Type.ORDER);
            balance = (BalanceOperationModel) bDAO.addBalanceOperation(balance);

            order.setStatus(OrderStatus.PAYED.name());
            order.setPricePerUnit(0.0);
            order.setQuantity(0);
            order.setBalanceId(balance.getBalanceId());
            order.setProductCode(null);

            return orderDAO.updateOrder(order);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * Record the arrival of a given Order, changing its state from PAYED to COMPLETED.
     * The quantity of the related ProductType will be updated.
     *
     * @param orderId orderId
     * @return whether the operation has been completed or not
     * @throws InvalidOrderIdException  if the order id is less than or equal to 0 or if it is null.
     * @throws InvalidLocationException if the product location is in an invalid format (not --).
     */
    public static boolean recordOrderArrival(Integer orderId)
            throws InvalidOrderIdException, InvalidLocationException {
        /* InvalidOrderIdException */
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();

        OrderModel order;
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeModel product;
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        try {
            order = (OrderModel) orderDAO.getOrder(orderId);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
        if (order == null)
            return false;
        if (!order.getStatus().equals("PAYED"))
            return false;
        order.setStatus(OrderStatus.COMPLETED.name());

        try {
            product = (ProductTypeModel) productTypeDAO.getProductByBarcode(order.getProductCode());
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }

        if (product.getLocation() == null)
            throw new InvalidLocationException();

        Pattern pattern = Pattern.compile("^[0-9]+-[a-zA-Z]-[0-9]+$");
        Matcher matcher = pattern.matcher(product.getLocation());
        if (!matcher.matches())
            throw new InvalidLocationException();

        try {
            if (!ProductTypeModel.updateQuantity(product.getId(), order.getQuantity()))
                return false;
            return orderDAO.updateOrder(order);
        } catch (InvalidProductIdException | InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * This method records the arrival of an order with given <orderId>. This method changes the quantity of available product.
     * This method records each product received, with its RFID. RFIDs are recorded starting from RFIDfrom, in increments of 1
     * ex recordOrderArrivalRFID(10, "000000001000")  where order 10 ordered 10 quantities of an item, this method records
     * products with RFID 1000, 1001, 1002, 1003 etc until 1009
     * The product type affected must have a location registered. The order should be either in the PAYED state (in this
     * case the state will change to the COMPLETED one and the quantity of product type will be updated) or in the
     * COMPLETED one (in this case this method will have no effect at all).
     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
     *
     * @param orderId the id of the order that has arrived
     * @return true if the operation was successful
     * false if the order does not exist or if it was not in an ORDERED/COMPLETED state
     * @throws InvalidOrderIdException  if the order id is less than or equal to 0 or if it is null.
     * @throws InvalidLocationException if the ordered product type has not an assigned location.
     * @throws InvalidRFIDException     if the RFID has invalid format or is not unique
     */
    public static boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom)
            throws InvalidOrderIdException, InvalidLocationException, InvalidRFIDException {
        /* InvalidOrderIdException */
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();

        try {
            Integer.parseInt(RFIDfrom);
        } catch (NumberFormatException e) {
            throw new InvalidRFIDException();
        }

        OrderModel order;
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeModel product;
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        try {
            order = (OrderModel) orderDAO.getOrder(orderId);
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
        if (order == null)
            return false;
        if (order.getStatus().equals("COMPLETED"))
            return true;
        if (!order.getStatus().equals("PAYED"))
            return false;
        order.setStatus(OrderStatus.COMPLETED.name());

        try {
            product = (ProductTypeModel) productTypeDAO.getProductByBarcode(order.getProductCode());
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }

        List<String> allRFID;
        try {
            allRFID = productEntryDAO.getProductEntries(order.getProductCode());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            return false;
        }

        if (product.getLocation() == null)
            throw new InvalidLocationException();

        Pattern pattern = Pattern.compile("^[0-9]+-[a-zA-Z]-[0-9]+$");
        Matcher matcher = pattern.matcher(product.getLocation());
        if (!matcher.matches())
            throw new InvalidLocationException();

        try {
            if (!ProductTypeModel.updateQuantity(product.getId(), order.getQuantity()))
                return false;
            for (String rfid : OrderModel.generateRFID(RFIDfrom, order.getQuantity())) {
                if (allRFID.contains(rfid))
                    throw new InvalidRFIDException();
                productEntryDAO.addProductEntry(product.getBarCode(), rfid);
            }
            return orderDAO.updateOrder(order);
        } catch (InvalidProductIdException | InvalidDAOParameterException | MissingDAOParameterException ex) {
            return false;
        }
    }

    /**
     * A method to generate a list of RFIDs given the base and the amount.
     * @param baseRFID
     * @param quantity
     * @return
     */
    public static List<String> generateRFID(String baseRFID, int quantity) {
        List<String> listRFID = new ArrayList<>();
        String prefixZeros;

        if (baseRFID == null || baseRFID.length() != 12)
            return null;
        if (quantity <= 0)
            return null;
        try {
            int numericalBaseRFID = Integer.parseInt(baseRFID);
            for (int i = numericalBaseRFID; i < numericalBaseRFID + quantity; i++) {
                prefixZeros = "";
                if (String.valueOf(i).length() < 12)
                    for (int j = 0; j < (12 - String.valueOf(i).length()); j++)
                        prefixZeros += "0";
                listRFID.add(prefixZeros + i);
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return listRFID;
    }

    /**
     * Get the list with all the Orders stored in the database
     *
     * @return orderDAO.getOrders() on success
     */
    public static List<Order> getAllOrders() {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrders();
    }

}