package it.polito.ezshop.data;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;


import java.time.LocalDate;
import java.util.List;


public class EZShop implements EZShopInterface {

    User loggedUser = null;
    AccountBookModel abm = new AccountBookModel();

    public SaleTransactionModel sale = new SaleTransactionModel();
    public ReturnTransactionModel returnTrans = new ReturnTransactionModel();

    @Override
    public void reset() {
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        ReturnProductRfidDAO returnProductRfidDAO = new ReturnProductRfidDAO();
        OrderDAOInterface order = new OrderDAO();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAOInterface returnT = new ReturnTransactionDAO();
        TicketEntryDAOInterface ticket = new TicketEntryDAO();
        SaleTransactionDAOInterface saleT = new SaleTransactionDAO();
        ProductTypeDAOInterface product = new ProductTypeDAO();
        BalanceOperationDAOInterface balance = new BalanceOperationDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();
        UserDAO userDAO = new UserDAO();

        order.resetOrders();
        returnProductDAO.resetReturnProducts();
        returnProductRfidDAO.resetReturnProductRfid();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productEntryDAO.resetProductEntries();
        returnT.resetReturnTransactions();
        ticket.resetTicketEntries();
        saleT.resetSaleTransactions();
        product.resetProductTypes();
        balance.resetBalanceOperations();
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();
        userDAO.resetUsers();
        this.loggedUser = null;
        this.abm = new AccountBookModel();
        this.sale = new SaleTransactionModel();
        this.returnTrans = new ReturnTransactionModel();
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        return UserModel.createUser(username, password, role);
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if (loggedUser == null || !loggedUser.getRole().equals("Administrator"))
            throw new UnauthorizedException();

        return UserModel.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        if (loggedUser == null || !loggedUser.getRole().equals("Administrator"))
            throw new UnauthorizedException();

        return UserModel.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if (loggedUser == null || !loggedUser.getRole().equals("Administrator"))
            throw new UnauthorizedException();

        return UserModel.getUser(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        if (loggedUser == null || !loggedUser.getRole().equals("Administrator"))
            throw new UnauthorizedException();

        return UserModel.updateUserRights(id, role);
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (loggedUser != null) {
            return null;
        }
        loggedUser = UserModel.login(username, password);
        return loggedUser;
    }

    @Override
    public boolean logout() {
        if (loggedUser == null)
            return false;
        else {
            loggedUser = null;
            return true;
        }
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.createProductType(description, productCode, pricePerUnit, note);
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.updateProduct(id, newDescription, newCode, newPrice, newNote);
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.deleteProductType(id);
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.getAllProductTypes();
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.getProductTypeByBarCode(barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.getProductTypesByDescription(description);
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.updateQuantity(productId, toBeAdded);
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return ProductTypeModel.updatePosition(productId, newPos);
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return OrderModel.issueOrder(productCode, quantity, pricePerUnit);
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        if (quantity * pricePerUnit > AccountBookModel.computeBalance(this.abm))
            return -1;

        return OrderModel.payOrderFor(productCode, quantity, pricePerUnit);
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        OrderDAO orderDAO = new OrderDAO();
        try {
            Order order = orderDAO.getOrder(orderId);
            if(order.getQuantity() * order.getPricePerUnit() > AccountBookModel.computeBalance(this.abm))
                return false;
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        return OrderModel.payOrder(orderId);
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return OrderModel.recordOrderArrival(orderId);
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return OrderModel.recordOrderArrivalRFID(orderId, RFIDfrom);
    }
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        return OrderModel.getAllOrders();
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return CustomerModel.defineCustomer(customerName);
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return CustomerModel.modifyCustomer(id, newCustomerName, newCustomerCard);
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return CustomerModel.deleteCustomer(id);
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return CustomerModel.getCustomer(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return CustomerModel.getAllCustomers();
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return LoyaltyCardModel.createCard();
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return LoyaltyCardModel.attachCardToCustomer(customerCard, customerId);
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        return LoyaltyCardModel.modifyPointsOnCard(customerCard, pointsToBeAdded);
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.startSaleTransaction();
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.addProductToSale(transactionId, productCode, amount);
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        if(loggedUser == null){
            throw new UnauthorizedException();
        }
        return sale.addProductToSaleRFID(transactionId, RFID);
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.deleteProductFromSale(transactionId, productCode, amount);
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        if(loggedUser == null){
            throw new UnauthorizedException();
        }
        return sale.deleteProductFromSaleRFID(transactionId, RFID);
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.applyDiscountRateToProduct(transactionId, productCode, discountRate);
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.applyDiscountRateToSale(transactionId, discountRate);
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.computePointsForSale(transactionId);
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.endSaleTransaction(transactionId);
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.deleteSaleTransaction(saleNumber);
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.getSaleTransaction(transactionId);
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.startReturnTransaction(saleNumber);
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.returnProduct(returnId, productCode, amount);
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.returnProductRFID(returnId,RFID);
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.endReturnTransaction(returnId, commit);
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.deleteReturnTransaction(returnId);
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.receiveCashPayment(ticketNumber, cash);
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return sale.receiveCreditCardPayment(ticketNumber, creditCard);
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.returnCashPayment(returnId);
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (loggedUser == null) {
            throw new UnauthorizedException();
        }
        return returnTrans.returnCreditCardPayment(returnId, creditCard);
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager")) {
            throw new UnauthorizedException();
        }
        return AccountBookModel.recordBalanceUpdate(this.abm, toBeAdded);
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && !loggedUser.getRole().equals("ShopManager")) {
            throw new UnauthorizedException();
        }
        return AccountBookModel.getCreditsAndDebits(from, to);
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (loggedUser == null || (!loggedUser.getRole().equals("Administrator")) && (!loggedUser.getRole().equals("ShopManager"))) {
            throw new UnauthorizedException();
        }
        return AccountBookModel.computeBalance(this.abm);
    }
}
