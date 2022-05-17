package it.polito.ezshop.integrationTests.step11;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.ReturnTransactionModel;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class EZShopAPITesting {
    UserDAO userDAO = new UserDAO();
    DbConnection dbConnection = DbConnection.getInstance();
    String queryUser = "DELETE FROM user";

    @Test
    public void testReset() {
        EZShop ezShop = new EZShop();
        ezShop.reset();
    }

    @Test
    public void testCreateUser() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testDeleteUser() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "ShopManager");
            assert (user2ID > 0);
            ezShop.login("matti", "23101998");
            assertThrows(UnauthorizedException.class, () ->
                    ezShop.deleteUser(userID));
            ezShop.logout();
            ezShop.login("admin", "admin");
            assert (ezShop.deleteUser(user2ID));
            assertThrows(InvalidUserIdException.class, () ->
                    ezShop.deleteUser(-1));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetAllUsers() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "ShopManager");
            assert (user2ID > 0);
            Integer user3ID = ezShop.createUser("vivi", "02061997", "Cashier");
            assert (user3ID > 0);
            ezShop.login("admin", "admin");

            assert (ezShop.getAllUsers().size() != 0);
            assert (ezShop.getAllUsers().size() == 3);

            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, ezShop::getAllUsers);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetUser() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "ShopManager");
            assert (user2ID > 0);
            Integer user3ID = ezShop.createUser("vivi", "02061997", "Cashier");
            assert (user3ID > 0);
            ezShop.login("admin", "admin");

            assert (ezShop.getUser(userID).getId().equals(userID));
            assert (ezShop.getUser(user2ID).getId().equals(user2ID));
            assert (ezShop.getUser(user3ID).getId().equals(user3ID));

            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getUser(userID));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testUpdateUserRights() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "ShopManager");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");

            assert (ezShop.updateUserRights(user2ID, "Cashier"));
            assert (ezShop.getUser(user2ID).getRole().equals("Cashier"));

            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.updateUserRights(user2ID, "Cashier"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testLogin() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            User u = ezShop.login("admin", "admin");
            assert (u != null);
            assert (u.getUsername().equals("admin"));
            assert (u.getPassword().equals("admin"));
            assert (u.getRole().equals("Administrator"));
            assert (ezShop.login("admin", "admin") == null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testLogout() {
        EZShop ezShop = new EZShop();
        dbConnection.executeUpdate(queryUser);

        try {
            assert (!ezShop.logout());
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            ezShop.login("admin", "admin");
            assert (ezShop.logout());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testCreateProductType() {
        EZShop ezShop = new EZShop();
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

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            assert (ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!") > 0);

            assertThrows(InvalidProductDescriptionException.class, () ->
                    ezShop.createProductType(null, "8008008008006",
                            0.1, "Fresh and green!"));
            assertThrows(InvalidProductCodeException.class, () ->
                    ezShop.createProductType("Apple", null,
                            0.1, "Fresh and green!"));
            assertThrows(InvalidProductCodeException.class, () ->
                    ezShop.createProductType("Apple", "null",
                            0.1, "Fresh and green!"));
            assertThrows(InvalidPricePerUnitException.class, () ->
                    ezShop.createProductType("Apple", "8008008008006",
                            -1.0, "Fresh and green!"));

            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.createProductType("Apple", "8008008008006",
                            0.1, "Fresh and green!"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
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
        }
    }

    @Test
    public void testUpdateProduct() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            Integer prodID = ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezShop.updateProduct(prodID, "Pear", "0001110001116",
                    0.8, "Green and fresh!"));

            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.updateProduct(prodID, "Pear", "0001110001116",
                            0.8, "Green and fresh!"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testDeleteProduct() {
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
        EZShop ezShop = new EZShop();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            Integer prodID1 = ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezShop.deleteProductType(prodID1));

            Integer prodID2 = ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.deleteProductType(prodID2));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetAllProductTypes() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");

            assert (ezShop.getAllProductTypes().size() == 1);
            assert (ezShop.getAllProductTypes().size() != 0);

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, ezShop::getAllProductTypes);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetProductTypeByBarCode() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");

            ProductType prod = ezShop.getProductTypeByBarCode("8008008008006");
            assert (prod != null);
            assert (prod.getBarCode().equals("8008008008006"));

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getProductTypeByBarCode("8008008008006"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetProductTypesByDescription() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezShop.createProductType("Pizza Marinara", "0001110001116",
                    0.1, "2!");
            ezShop.createProductType("Calzone", "9999999999994",
                    0.1, "3!");

            assert (ezShop.getProductTypesByDescription("Pizza").size() == 2);
            assert (ezShop.getProductTypesByDescription("Calzone").size() == 1);
            assert (ezShop.getProductTypesByDescription("Pasta").size() == 0);

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getProductTypesByDescription("Pizza"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testUpdateQuantity() {
        EZShop ezShop = new EZShop();
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

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");

            Integer prodID = ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezShop.updateQuantity(prodID, 100));
            assert (!ezShop.updateQuantity(prodID, -500));
            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.updateQuantity(prodID, 100));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
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
        }
    }

    @Test
    public void testUpdatePosition() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");

            Integer prodID = ezShop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezShop.updatePosition(prodID, "1-F-5"));
            assertThrows(InvalidLocationException.class, () ->
                    ezShop.updatePosition(prodID, "4F5"));
            ezShop.logout();

            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.updatePosition(prodID, "1-F-5"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testIssueOrder() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            Integer orderID = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            assert (orderID > 0);

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.issueOrder("8008008008006",
                            80, 0.5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testPayOrderFor() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezShop.recordBalanceUpdate(5000.0);
            Integer orderID = ezShop.payOrderFor("8008008008006", 80, 0.5);
            assert (orderID > 0);
            assert (ezShop.computeBalance() == 4960.0);

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.payOrderFor("8008008008006", 80, 0.5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testPayOrder() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezShop.recordBalanceUpdate(5000.0);
            Integer orderID = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            assert (ezShop.payOrder(orderID));
            assert (ezShop.computeBalance() == 4960.0);

            Integer orderID2 = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.payOrder(orderID2));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testRecordOrderArrival() {
        EZShop ezShop = new EZShop();
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
        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            Integer prodID = ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezShop.updatePosition(prodID, "1-A-4");
            ezShop.recordBalanceUpdate(5000.0);
            Integer orderID = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.payOrder(orderID);
            assert (ezShop.recordOrderArrival(orderID));

            Integer orderID2 = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.payOrder(orderID2);
            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.recordOrderArrival(orderID2));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {

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
        }
    }

    @Test
    public void testRecordOrderArrivalRFID() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        String RFIDfrom = "000000010000";

        productEntryDAO.resetProductEntries();
        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            Integer prodID = ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezShop.updatePosition(prodID, "1-A-4");
            ezShop.recordBalanceUpdate(5000.0);
            Integer orderID = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.payOrder(orderID);
            assert (ezShop.recordOrderArrivalRFID(orderID, RFIDfrom));

            Integer orderID2 = ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.payOrder(orderID2);
            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.recordOrderArrivalRFID(orderID2, RFIDfrom));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetAllOrders() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);
            ezShop.login("admin", "admin");
            ezShop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            assert (ezShop.getAllOrders().size() == 0);
            ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.issueOrder("8008008008006",
                    80, 0.5);
            ezShop.issueOrder("8008008008006",
                    80, 0.5);
            assert (ezShop.getAllOrders().size() == 5);

            ezShop.logout();
            ezShop.login("matti", "23101998");

            assertThrows(UnauthorizedException.class, ezShop::getAllOrders);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testDefineCustomer() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.defineCustomer("Giorgia"));

            ezShop.login("admin", "admin");

            Integer customerID = ezShop.defineCustomer("Giorgia");

            assert (customerID > 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
        }
    }

    @Test
    public void testModifyCustomer() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            String card1 = "0123456789";
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.modifyCustomer(1,"Giorgia", card1));

            ezShop.login("admin", "admin");

            String card = ezShop.createCard();

            Integer customerID = ezShop.defineCustomer("Giorgia");
            ezShop.attachCardToCustomer(card, customerID);
            card = "9876543210";
            assert (ezShop.modifyCustomer(customerID, "Giorgia", card));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testDeleteCustomer() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.deleteCustomer(1));

            ezShop.login("admin", "admin");

            Integer customerID = ezShop.defineCustomer("Giorgia");

            assert (ezShop.deleteCustomer(customerID));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testGetCustomer() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getCustomer(1));

            ezShop.login("admin", "admin");

            Integer customerID = ezShop.defineCustomer("Giorgia");

            Customer c = ezShop.getCustomer(customerID);

            assert (c != null);
            assert (c.getId().equals(customerID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testGetAllCustomers() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, ezShop::getAllCustomers);

            ezShop.login("admin", "admin");

            ezShop.defineCustomer("Giorgia");

            assert (ezShop.getAllCustomers().size() == 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testCreateCard() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, ezShop::createCard);

            ezShop.login("admin", "admin");

            String card = ezShop.createCard();

            assert (card != null);
            assert (card.length() == 10);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testAttachCardToCustomer() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.attachCardToCustomer("0123456789", 1));

            ezShop.login("admin", "admin");

            Integer customerID = ezShop.defineCustomer("Giorgia");
            String card = ezShop.createCard();

            assert (ezShop.attachCardToCustomer(card, customerID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testModifyPointsOnCard() {
        EZShop ezShop = new EZShop();
        CustomerDAO customerDAO = new CustomerDAO();
        LoyaltyCardDAO loyaltyCardDAO = new LoyaltyCardDAO();

        dbConnection.executeUpdate(queryUser);
        customerDAO.resetCustomers();
        loyaltyCardDAO.resetLoyaltyCards();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.modifyPointsOnCard("0123456789", 1));

            ezShop.login("admin", "admin");

            ezShop.defineCustomer("Giorgia");
            String card = ezShop.createCard();

            assert (ezShop.modifyPointsOnCard(card, 500));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            customerDAO.resetCustomers();
            loyaltyCardDAO.resetLoyaltyCards();
        }
    }

    @Test
    public void testStartSaleTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, ezShop::startSaleTransaction);

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();

            assert (saleID > 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testAddProductToSale() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.addProductToSale(1, "8008008008006", 50));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            assert (ezShop.addProductToSale(saleID, "8008008008006", 50));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testAddProductToSaleRFID(){
        EZShop ezshop = new EZShop();
        UserDAO userDAO = new UserDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        userDAO.resetUsers();
        productEntryDAO.resetProductEntries();
        productTypeDAO.resetProductTypes();

        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        productTypeModel1.setBarCode("123456789128");
        productTypeModel1.setPricePerUnit(0.50);
        productTypeModel1.setLocation("7-b-9");
        productTypeModel1.setQuantity(3);

        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        productTypeModel2.setBarCode("123456789135");
        productTypeModel2.setPricePerUnit(0.50);
        productTypeModel2.setLocation("4-o-0");
        productTypeModel2.setQuantity(3);

        ProductTypeModel productTypeModel3 = new ProductTypeModel();
        productTypeModel3.setBarCode("123456789142");
        productTypeModel3.setPricePerUnit(0.50);
        productTypeModel3.setLocation("3-q-2");
        productTypeModel3.setQuantity(3);
        try {
            ezshop.createUser("cashier", "password", "Cashier");
            assertThrows(UnauthorizedException.class, () -> ezshop.addProductToSaleRFID(1, "0000000000"));
            ezshop.login("cashier", "password");
            Integer productID = productTypeDAO.addProductType(productTypeModel1).getId();
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', " + productID + ", 1)");
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', " + productID + ", 1)");
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', " + productID + ", 1)");

            Integer saleID = ezshop.startSaleTransaction();
            assertTrue(ezshop.addProductToSaleRFID(saleID, "000000000000"));
            assertTrue(ezshop.addProductToSaleRFID(saleID, "000000000001"));
            assertTrue(ezshop.addProductToSaleRFID(saleID, "000000000002"));
            assert(productTypeDAO.getProductByBarcode("123456789128").getQuantity() == 0);

        } catch(Exception e){
            e.printStackTrace();
            fail();
        } finally{
            userDAO.resetUsers();
            productEntryDAO.resetProductEntries();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testDeleteProductFromSale() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();


        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.deleteProductFromSale(1, "8008008008006", 50));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezShop.deleteProductFromSale(saleID, "8008008008006", 50));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();

        }
    }

    @Test
    public void testDeleteProductFromSaleRFID(){
        EZShop ezshop = new EZShop();
        UserDAO userDAO = new UserDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        userDAO.resetUsers();
        productEntryDAO.resetProductEntries();
        productTypeDAO.resetProductTypes();

        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        productTypeModel1.setBarCode("123456789128");
        productTypeModel1.setPricePerUnit(0.50);
        productTypeModel1.setLocation("7-b-9");
        productTypeModel1.setQuantity(3);

        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        productTypeModel2.setBarCode("123456789135");
        productTypeModel2.setPricePerUnit(0.50);
        productTypeModel2.setLocation("4-o-0");
        productTypeModel2.setQuantity(3);

        ProductTypeModel productTypeModel3 = new ProductTypeModel();
        productTypeModel3.setBarCode("123456789142");
        productTypeModel3.setPricePerUnit(0.50);
        productTypeModel3.setLocation("3-q-2");
        productTypeModel3.setQuantity(3);

        try {
            ezshop.createUser("cashier", "password", "Cashier");
            assertThrows(UnauthorizedException.class, () -> ezshop.deleteProductFromSaleRFID(1, "0000000000"));
            ezshop.login("cashier", "password");
            Integer productID = productTypeDAO.addProductType(productTypeModel1).getId();
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', " + productID + ", 1)");
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', " + productID + ", 1)");
            dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', " + productID + ", 1)");

            Integer saleID = ezshop.startSaleTransaction();
            ezshop.addProductToSaleRFID(saleID, "000000000000");
            ezshop.addProductToSaleRFID(saleID, "000000000001");
            ezshop.addProductToSaleRFID(saleID, "000000000002");
            assertTrue(ezshop.deleteProductFromSaleRFID(saleID, "000000000002"));
            assert(productTypeDAO.getProductByBarcode("123456789128").getQuantity() == 1);
            assertTrue(ezshop.deleteProductFromSaleRFID(saleID, "000000000000"));
            assert(productTypeDAO.getProductByBarcode("123456789128").getQuantity() == 2);

        } catch(Exception e){
            e.printStackTrace();
            fail();
        } finally{
            userDAO.resetUsers();
            productEntryDAO.resetProductEntries();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testApplyDiscountRateToProduct() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.applyDiscountRateToProduct(1, "8008008008006", 0.5));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezShop.applyDiscountRateToProduct(saleID, "8008008008006", 0.5));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testApplyDiscountRateToSale() {
        EZShop ezShop = new EZShop();
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

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.applyDiscountRateToSale(1, 0.5));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezShop.applyDiscountRateToSale(saleID, 0.5));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
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
        }
    }

    @Test
    public void testComputePointsForSale() {
        EZShop ezShop = new EZShop();
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

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.computePointsForSale(1));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            int cpfs = ezShop.computePointsForSale(saleID);

            assert (cpfs >= 0);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {

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
        }
    }

    @Test
    public void testEndSaleTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.endSaleTransaction(1));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezShop.endSaleTransaction(saleID));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testDeleteSaleTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.deleteSaleTransaction(1));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezShop.deleteSaleTransaction(saleID));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetSaleTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getSaleTransaction(1));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            SaleTransaction sT = ezShop.getSaleTransaction(saleID);
            assert (sT != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testStartReturnTrasaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.startReturnTransaction(1));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            Integer returnID = ezShop.startReturnTransaction(saleID);
            assert (returnID > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testReturnProduct() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.returnProduct(1, "8008008008006", 5));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            Integer returnID = ezShop.startReturnTransaction(saleID);
            assert (ezShop.returnProduct(returnID, "8008008008006", 5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testEndReturnTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.endReturnTransaction(1, true));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            Integer returnID = ezShop.startReturnTransaction(saleID);
            ezShop.returnProduct(returnID, "8008008008006", 5);
            assert (ezShop.endReturnTransaction(returnID, true));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();

        }
    }

    @Test
    public void testDeleteReturnTransaction() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.endReturnTransaction(1, true));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            Integer returnID = ezShop.startReturnTransaction(saleID);
            ezShop.returnProduct(returnID, "8008008008006", 5);
            ezShop.endReturnTransaction(returnID, true);
            assert (ezShop.deleteReturnTransaction(returnID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();

        }
    }

    @Test
    public void testReceiveCashPayment() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.receiveCashPayment(1, 150.0));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            SaleTransaction sT = ezShop.getSaleTransaction(saleID);
            double cash = ezShop.receiveCashPayment(sT.getTicketNumber(), 150.0);
            assert (cash >= 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testReceiveCreditCardPayment() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();


        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.receiveCreditCardPayment(1, "6532720076163432"));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            SaleTransaction sT = ezShop.getSaleTransaction(saleID);
            assertTrue (ezShop.receiveCreditCardPayment(sT.getTicketNumber(), "4485370086510891"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();

        }
    }

    @Test
    public void testReturnCashPayment() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.endReturnTransaction(1, true));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            SaleTransaction sT = ezShop.getSaleTransaction(saleID);
            assert (ezShop.receiveCashPayment(sT.getTicketNumber(), 100.0)>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testReturnCreditCardPayment() {
        EZShop ezShop = new EZShop();
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

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);

            assertThrows(UnauthorizedException.class, () ->
                    ezShop.receiveCreditCardPayment(1, "6532720076163432"));

            ezShop.login("admin", "admin");

            Integer saleID = ezShop.startSaleTransaction();
            Integer prodID = ezShop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezShop.updatePosition(prodID, "1-A-5");
            ezShop.updateQuantity(prodID, 8000);
            ezShop.addProductToSale(saleID, "8008008008006", 50);
            ezShop.endSaleTransaction(saleID);
            SaleTransaction sT = ezShop.getSaleTransaction(saleID);
            assert (ezShop.receiveCreditCardPayment(sT.getTicketNumber(), "3017786004753256"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
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
        }
    }

    @Test
    public void testRecordBalanceUpdate() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);

            ezShop.login("matti", "23101998");
            assertThrows(UnauthorizedException.class, () ->
                    ezShop.recordBalanceUpdate(1.0));
            ezShop.logout();

            ezShop.login("admin", "admin");

            assert (ezShop.recordBalanceUpdate(500.0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetCreditsAndDebits() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);

            ezShop.login("matti", "23101998");
            assertThrows(UnauthorizedException.class, () ->
                    ezShop.getCreditsAndDebits(LocalDate.of(2021,2,2), LocalDate.of(2021, 4, 5)));
            ezShop.logout();

            ezShop.login("admin", "admin");
            ezShop.recordBalanceUpdate(500.0);
            ezShop.recordBalanceUpdate(500.0);
            ezShop.recordBalanceUpdate(500.0);
            List<BalanceOperation> list = ezShop.getCreditsAndDebits(LocalDate.of(2020,5,1), LocalDate.now());
            assert (list.size() == 3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testComputeBalance() {
        EZShop ezShop = new EZShop();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();

        try {
            Integer userID = ezShop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
            Integer user2ID = ezShop.createUser("matti", "23101998", "Cashier");
            assert (user2ID > 0);

            ezShop.login("matti", "23101998");
            assertThrows(UnauthorizedException.class, ezShop::computeBalance);
            ezShop.logout();

            ezShop.login("admin", "admin");
            ezShop.recordBalanceUpdate(500.0);
            ezShop.recordBalanceUpdate(500.0);
            ezShop.recordBalanceUpdate(500.0);
            assert (ezShop.computeBalance() == 1500.0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
            returnProductDAO.resetReturnProducts();
            returnTransactionDAO.resetReturnTransactions();
            ticketEntryDAO.resetTicketEntries();
            saleTransactionDAO.resetSaleTransactions();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();
        }
    }
    @Test
    public void testReturnProductRFID() {
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
        dbConnection.executeUpdate("INSERT INTO user VALUES('1','admin', 'admin','Administrator')");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (201, 123, 3, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (201, '000000000002')");
        try {
            EZShopInterface ezShop = new EZShop();
            assertThrows(UnauthorizedException.class,()->ezShop.returnProductRFID(null,"0001110001116"));

            ezShop.login("admin", "admin");
            //returnID null t1(null,"0001110001116")
            assertThrows(InvalidTransactionIdException.class,()->ezShop.returnProductRFID(null,"0001110001116"));
            //returnID 0 t2(0,"0001110001116")
            assertThrows(InvalidTransactionIdException.class,()->ezShop.returnProductRFID(0,"0001110001116"));
            //returnID <0 t3(-1,"0001110001116")
            assertThrows(InvalidTransactionIdException.class,()->ezShop.returnProductRFID(-1,"0001110001116"));
            //product code null t4(3,null)
            assertThrows(InvalidRFIDException.class,()->ezShop.returnProductRFID(3,null));
            //product code ""t5(2,"")
            assertThrows(InvalidRFIDException.class,()->ezShop.returnProductRFID(3,""));
            //product code invalid t6(2,"12")
            assertThrows(InvalidRFIDException.class,()->ezShop.returnProductRFID(3,"12"));
            //product code invalid t7(2,"23A342123287")
            assertThrows(InvalidRFIDException.class,()->ezShop.returnProductRFID(3,"23A342123287"));
            ezShop.startReturnTransaction(201);
            assertTrue(ezShop.returnProductRFID(1,"000000000002"));
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRFIDException | InvalidTransactionIdException | UnauthorizedException e) {
            e.printStackTrace();
        }


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
    }

}
