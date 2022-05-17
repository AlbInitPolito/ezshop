package it.polito.ezshop.integrationTests.step10;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;
import org.junit.Assert;
import it.polito.ezshop.model.OrderModel;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.UserModel;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class EZShopDriver {
    UserDAO userDAO = new UserDAO();
    User loggedUser;
    DbConnection dbConnection = DbConnection.getInstance();
    String queryUser = "DELETE FROM user";
    String clearCustomers = "DELETE FROM customer";
    String clearLoyaltyCard = "DELETE FROM loyalty_card";
    String clearProductType = "DELETE FROM product_type";
    String clearSaleTransaction = "DELETE FROM sale_transaction";
    String clearProductInTransaction = "DELETE FROM product_in_transaction";
    String clearReturnTransaction = "DELETE FROM return_transaction";
    String clearReturnProduct = "DELETE FROM return_product";
    String clearBalanceOperation = "DELETE FROM balance_operation";
    String clearProductEntry = "DELETE FROM product_entry";
    String clearReturnProductRFID = "DELETE FROM return_product_rfid";
    String clearProductInTransactionRFID = "DELETE FROM product_in_transaction_rfid";
    SaleTransactionModel sale = new SaleTransactionModel();
    AccountBookModel abm = new AccountBookModel();


    @Test
    public void testResetDriver() {
        BalanceOperationDAOInterface balance = new BalanceOperationDAO();
        OrderDAOInterface order = new OrderDAO();
        ProductTypeDAOInterface product = new ProductTypeDAO();
        ReturnTransactionDAOInterface returnT = new ReturnTransactionDAO();
        SaleTransactionDAOInterface saleT = new SaleTransactionDAO();
        TicketEntryDAOInterface ticket = new TicketEntryDAO();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();

        assert (returnProductDAO.resetReturnProducts());
        assert (returnT.resetReturnTransactions());
        assert (order.resetOrders());
        assert (ticket.resetTicketEntries());
        assert (saleT.resetSaleTransactions());
        assert (balance.resetBalanceOperations());
        assert (product.resetProductTypes());
    }

    @Test
    public void testCreateUser() {
        Integer userID;
        dbConnection.executeUpdate(queryUser);

        assertThrows(InvalidRoleException.class, () ->
                UserModel.createUser("vivi", "1234", null));
        assertThrows(InvalidPasswordException.class, () ->
                UserModel.createUser("vivi", null, UserModel.Role.Administrator.name()));
        assertThrows(InvalidUsernameException.class, () ->
                UserModel.createUser(null, "1234", UserModel.Role.Administrator.name()));
        assertThrows(InvalidRoleException.class, () ->
                UserModel.createUser("vivi", "1234", ""));

        try {
            assert (UserModel.createUser("asfniuasdhvnsaiovnasiuvnsiudvnsadiuvnasdivuasndvasiuvnasdiuvadasdadasdasdasdasdasdasdasdasdasdasdasnsadvis", "1234", UserModel.Role.Administrator.name()) == -1);
            assert (UserModel.createUser("vivi", "asfniuasdhvnsaiovnasiuvnsiudvnsadiuvnasdivuasndvasiuvnasdiuvadasdadasdasdasdasdasdasdasdasdasdasdasnsadvis", UserModel.Role.Administrator.name()) == -1);
            userID = UserModel.createUser("vivi", "1234", UserModel.Role.Administrator.name());
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
        DbConnection dbConnection = DbConnection.getInstance();
        String query = "DELETE FROM user";
        dbConnection.executeUpdate(query);

        Integer userID;
        try {
            userID = UserModel.createUser("vivi", "1234", UserModel.Role.Administrator.name());
            assertThrows (InvalidUserIdException.class, () -> UserModel.deleteUser(-1));
            assert (UserModel.deleteUser(userID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(query);
        }
    }

    @Test
    public void testGetAllUsers() {
        dbConnection.executeUpdate(queryUser);
        try {
            assert (UserModel.getAllUsers().size() == 0);
            UserModel.createUser("vivi", "1234", UserModel.Role.ShopManager.name());
            UserModel.createUser("vivi2", "12344", UserModel.Role.Cashier.name());
            UserModel.createUser("vivi3", "12345", UserModel.Role.Administrator.name());
            assert (UserModel.getAllUsers().size() == 3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetUser() {
        Integer userID;
        dbConnection.executeUpdate(queryUser);
        try {
            assertThrows (InvalidUserIdException.class, () ->
                    UserModel.getUser(-1));
            assertThrows (InvalidUserIdException.class, () ->
                    UserModel.getUser(null));
            userID = UserModel.createUser("vivi", "1234", UserModel.Role.ShopManager.name());
            assert (UserModel.getUser(userID) != null);
            assert (UserModel.getUser(userID).getId().equals(userID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testUpdateUserRights() {
        Integer userID;
        dbConnection.executeUpdate(queryUser);
        try {
            userID = UserModel.createUser("vivi", "1234", UserModel.Role.ShopManager.name());
            assertThrows (InvalidUserIdException.class, () ->
                    UserModel.updateUserRights(-1, "Administrator"));
            assertThrows (InvalidUserIdException.class, () ->
                    UserModel.updateUserRights(null, "Administrator"));
            assertThrows (InvalidRoleException.class, () ->
                    UserModel.updateUserRights(1, "Barber"));
            assert (UserModel.updateUserRights(userID, "Cashier"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testLogin() {
        UserModel admin = new UserModel("admin", "admin", UserModel.Role.Administrator.name());
        UserModel shopm = new UserModel("shopm", "shopm", UserModel.Role.ShopManager.name());
        UserModel cash = new UserModel("cash", "cash", UserModel.Role.Cashier.name());

        dbConnection.executeUpdate(queryUser);

        try {
            Integer adminID = UserModel.createUser(admin.getUsername(), admin.getPassword(), admin.getRole());
            Integer shopmID = UserModel.createUser(shopm.getUsername(), shopm.getPassword(), shopm.getRole());
            Integer cashID = UserModel.createUser(cash.getUsername(), cash.getPassword(), cash.getRole());

            loggedUser = UserModel.login(admin.getUsername(), admin.getPassword());
            assertNotNull(loggedUser);
            assertNotNull (loggedUser.getRole());
            assert (loggedUser.getRole().equals("Administrator"));

            loggedUser = UserModel.login(shopm.getUsername(), shopm.getPassword());
            assertNotNull(loggedUser);
            assertNotNull (loggedUser.getRole());
            assert (loggedUser.getRole().equals("ShopManager"));

            loggedUser = UserModel.login(cash.getUsername(), cash.getPassword());
            assertNotNull(loggedUser);
            assertNotNull (loggedUser.getRole());
            assert (loggedUser.getRole().equals("Cashier"));
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testLogout() {
        UserModel admin = new UserModel("admin", "admin", UserModel.Role.Administrator.name());

        dbConnection.executeUpdate(queryUser);

        try {
            UserModel.createUser(admin.getUsername(), admin.getPassword(), admin.getRole());
            loggedUser = UserModel.login(admin.getUsername(), admin.getPassword());
            assertNotNull(loggedUser);
            loggedUser = null;
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
            fail();
        } finally {
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testCreateProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");

            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            assert (prod1.getId() > 0);
            assert (prod2.getId() > 0);
            assert (prod3.getId() > 0);

            assertThrows(InvalidProductDescriptionException.class, () -> ProductTypeModel.createProductType(
                    null,
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));
            assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    null,
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));
            assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    "blabla",
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));
            assertThrows(InvalidPricePerUnitException.class, () -> ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    -1.0,
                    prod2.getNote()
            ));
            assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    "80080080008007",
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testUpdateProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            assertThrows(InvalidProductIdException.class, () ->
                    ProductTypeModel.updateProduct(null, prod1.getProductDescription(),
                            prod1.getBarCode(), prod1.getPricePerUnit(), prod1.getNote()));
            assertThrows(InvalidProductDescriptionException.class, () ->
                    ProductTypeModel.updateProduct(prod1.getId(), null,
                            prod1.getBarCode(), prod1.getPricePerUnit(), prod1.getNote()));
            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.updateProduct(prod1.getId(), prod1.getProductDescription(),
                            null, prod1.getPricePerUnit(), prod1.getNote()));
            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.updateProduct(prod1.getId(), prod1.getProductDescription(),
                            "null", prod1.getPricePerUnit(), prod1.getNote()));
            assertThrows(InvalidPricePerUnitException.class, () ->
                    ProductTypeModel.updateProduct(prod1.getId(), prod1.getProductDescription(),
                            prod1.getBarCode(), -1.0, prod1.getNote()));
            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.updateProduct(prod1.getId(), prod1.getProductDescription(),
                            "8008008008001", prod1.getPricePerUnit(), prod1.getNote()));

            assert (ProductTypeModel.updateProduct(prod1.getId(), "newDescription",
                    "0001110001116", 100.1, "New notes"));
            assert (!ProductTypeModel.updateProduct(900000, prod1.getProductDescription(),
                    "8008008008001", prod1.getPricePerUnit(), prod1.getNote()));
            assert (ProductTypeModel.updateProduct(prod1.getId(), "newDescription1",
                    "0001110001116", 100.1, "New notes"));

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testDeleteProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            assertThrows(InvalidProductIdException.class, () ->
                    ProductTypeModel.deleteProductType(null));

            assert (ProductTypeModel.deleteProductType(prod3.getId()));

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetAllProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            assert (ProductTypeModel.getAllProductTypes() != null);
            assert (ProductTypeModel.getAllProductTypes().size() == 3);

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetProductTypeByBarcode() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.getProductTypeByBarCode(null));
            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.getProductTypeByBarCode("null"));
            assertThrows(InvalidProductCodeException.class, () ->
                    ProductTypeModel.getProductTypeByBarCode("8008008008001"));

            assert (ProductTypeModel.getProductTypeByBarCode("8008008008006") != null);
            assertNull(ProductTypeModel.getProductTypeByBarCode("0001110001116"));

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetProductTypesByDescription() {
        ProductTypeModel prod1 = new ProductTypeModel("8008008008006", "Pizza Margherita", 0.5, 80, 0.2, "Freshness", 1, "F", 3);
        ProductTypeModel prod2 = new ProductTypeModel("1234567891231", "Pizza Marinara", 0.6, 90, 0.1, "Mmmh", 2, "D", 1);
        ProductTypeModel prod3 = new ProductTypeModel("7418529637899", "Pizza Benevento", 0.7, 100, 0.0, "Ja", 3, "A", 2);
        ProductTypeModel prod4 = new ProductTypeModel("0001112223332", "Amatriciana", 0.5, 80, 0.2, "Freshness", 1, "F", 3);
        ProductTypeModel prod5 = new ProductTypeModel("0001112223349", "Cacio e pepe", 0.6, 90, 0.1, "Mmmh", 2, "D", 1);
        ProductTypeModel prod6 = new ProductTypeModel("0001112223356", "Carbonara", 0.7, 100, 0.0, "Ja", 3, "A", 2);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            prod2.setId(ProductTypeModel.createProductType(
                    prod2.getProductDescription(),
                    prod2.getBarCode(),
                    prod2.getPricePerUnit(),
                    prod2.getNote()
            ));

            prod3.setId(ProductTypeModel.createProductType(
                    prod3.getProductDescription(),
                    prod3.getBarCode(),
                    prod3.getPricePerUnit(),
                    prod3.getNote()
            ));

            prod4.setId(ProductTypeModel.createProductType(
                    prod4.getProductDescription(),
                    prod4.getBarCode(),
                    prod4.getPricePerUnit(),
                    prod4.getNote()
            ));

            prod5.setId(ProductTypeModel.createProductType(
                    prod5.getProductDescription(),
                    prod5.getBarCode(),
                    prod5.getPricePerUnit(),
                    prod5.getNote()
            ));

            prod6.setId(ProductTypeModel.createProductType(
                    prod6.getProductDescription(),
                    prod6.getBarCode(),
                    prod6.getPricePerUnit(),
                    prod6.getNote()
            ));

            assert (ProductTypeModel.getProductTypesByDescription("Pizza").size() == 3);

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testUpdateQuantity() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        int toBeAdded = 4;

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));

            assertThrows(InvalidProductIdException.class, () ->
                    ProductTypeModel.updateQuantity(null, 1));
            assert (ProductTypeModel.updateQuantity(prod1.getId(), 0));
            assert (!ProductTypeModel.updateQuantity(prod1.getId(), -1000));

            assert (ProductTypeModel.updateQuantity(prod1.getId(), toBeAdded));

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testUpdatePosition() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        String newLocation = "11-U-11";

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        dbConnection.executeUpdate(queryUser);
        productTypeDAO.resetProductTypes();

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            prod1.setId(ProductTypeModel.createProductType(
                    prod1.getProductDescription(),
                    prod1.getBarCode(),
                    prod1.getPricePerUnit(),
                    prod1.getNote()
            ));
            prod1.setLocation("1-A-1");

            assert (ProductTypeModel.updatePosition(prod1.getId(), "1-A-1"));
            assert (ProductTypeModel.updatePosition(prod1.getId(), "1-A-1"));

            assertThrows(InvalidProductIdException.class, () ->
                    ProductTypeModel.updatePosition(null, "5-Z-4"));
            assertThrows(InvalidLocationException.class, () ->
                    ProductTypeModel.updatePosition(prod1.getId(), null));
            assertThrows(InvalidLocationException.class, () ->
                    ProductTypeModel.updatePosition(prod1.getId(), "4-FA-s"));

            assert (ProductTypeModel.updatePosition(prod1.getId(), newLocation));

        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException mpe) {
            mpe.printStackTrace();
            fail();
        } catch (InvalidPasswordException | InvalidRoleException | InvalidUsernameException e) {
            e.printStackTrace();
        } finally {
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testIssueOrder() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);

        assertThrows(InvalidProductCodeException.class, () ->
                OrderModel.issueOrder(null, 1, 0.5));
        assertThrows(InvalidQuantityException.class, () ->
                OrderModel.issueOrder(productCode, -1, 1.0));
        assertThrows(InvalidPricePerUnitException.class, () ->
                OrderModel.issueOrder(productCode, quantity, -0.5));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            productTypeDAO.addProductType(product);
            OrderModel.issueOrder(productCode, 50, 0.5);

        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testPayOrderFor() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        AccountBookModel abm = new AccountBookModel();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);
        AccountBookModel.recordBalanceUpdate(abm, 500.0);

        assertThrows(InvalidProductCodeException.class, () ->
                OrderModel.payOrderFor(null, 1, 0.5));
        assertThrows(InvalidQuantityException.class, () ->
                OrderModel.payOrderFor(productCode, -1, 1.0));
        assertThrows(InvalidPricePerUnitException.class, () ->
                OrderModel.payOrderFor(productCode, quantity, -0.5));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            productTypeDAO.addProductType(product);
            assertFalse(quantity*pricePerUnit > AccountBookModel.computeBalance(abm));
            OrderModel.payOrderFor(productCode, 50, 0.5);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        }

        String productCode2 = "0001110001116";
        product.setBarCode(productCode2);
        try {
            assert (OrderModel.payOrderFor(productCode2, 50, 0.5) == -1);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testPayOrder() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        Integer orderID;
        AccountBookModel abm = new AccountBookModel();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);
        AccountBookModel.recordBalanceUpdate(abm, 500.0);

        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.payOrder(-1));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            productTypeDAO.addProductType(product);
            assertFalse(quantity*pricePerUnit > AccountBookModel.computeBalance(abm));
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);
            assert (OrderModel.payOrder(orderID));
            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testRecordOrderArrival() {
        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.recordOrderArrival(-1));
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();

        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();

        Integer orderID;

        /* Reset */
        ticketEntryDAO.resetTicketEntries();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        saleTransactionDAO.resetSaleTransactions();
        returnTransactionDAO.resetReturnTransactions();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            product.setLocation("1-C-2");
            ProductTypeModel.updatePosition(product.getId(), product.getLocation());
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);

            assert (!OrderModel.recordOrderArrival(orderID));
            assert (OrderModel.payOrder(orderID));
            OrderModel.recordOrderArrival(orderID);

            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            ticketEntryDAO.resetTicketEntries();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            saleTransactionDAO.resetSaleTransactions();
            returnTransactionDAO.resetReturnTransactions();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testRecordOrderArrivalRFID() {
        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.recordOrderArrival(-1));
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;
        String RFIDfrom = "000000010000";

        ProductTypeModel product = new ProductTypeModel();

        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        Integer orderID;

        /* Reset */
        productEntryDAO.resetProductEntries();
        ticketEntryDAO.resetTicketEntries();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        saleTransactionDAO.resetSaleTransactions();
        returnTransactionDAO.resetReturnTransactions();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            product.setLocation("1-C-2");
            ProductTypeModel.updatePosition(product.getId(), product.getLocation());
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);

            assert (!OrderModel.recordOrderArrivalRFID(orderID, RFIDfrom));
            assert (OrderModel.payOrder(orderID));
            OrderModel.recordOrderArrivalRFID(orderID, RFIDfrom);

            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();
            ticketEntryDAO.resetTicketEntries();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            saleTransactionDAO.resetSaleTransactions();
            returnTransactionDAO.resetReturnTransactions();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testGetAllOrders() {
        String productDes = "Apple";
        String productCode = "8008008008006";

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        dbConnection.executeUpdate(queryUser);

        try {
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            loggedUser = UserModel.login("admin", "admin");
            product.setProductDescription(productDes);
            product.setBarCode(productCode);
            product.setQuantity(80);
            product.setPricePerUnit(0.5);
            productTypeDAO.addProductType(product);

            OrderModel.issueOrder(productCode, 50, 0.5);
            OrderModel.issueOrder(productCode, 10, 0.4);
            OrderModel.issueOrder(productCode, 4, 0.2);
            OrderModel.issueOrder(productCode, 7, 0.3);

            List<Order> list = OrderModel.getAllOrders();

            assert (list != null);
            assert (list.size() == 4);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            dbConnection.executeUpdate(queryUser);
        }
    }

    @Test
    public void testAPIDefineCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        try{
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.defineCustomer(null));
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.defineCustomer(""));
            assert(CustomerModel.defineCustomer("qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopa") == -1);
            assert(CustomerModel.defineCustomer("testCustomerName") > 0);
            assert(CustomerModel.defineCustomer("testCustomerName") == -1);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearCustomers);
    }

    @Test
    public void testAPImodifyCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
        try{
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.modifyCustomer(null, "testNewCustomerName", "0123456789"));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.modifyCustomer(-1, "testNewCustomerName", "0123456789"));
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.modifyCustomer(123, null, "0123456789"));
            assertThrows(InvalidCustomerNameException.class, () -> CustomerModel.modifyCustomer(123, "", "0123456789"));
            assertFalse(CustomerModel.modifyCustomer(123, "qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjha", "0123456789"));
            assertThrows(InvalidCustomerCardException.class, () -> CustomerModel.modifyCustomer(123, "testNewCustomerName", "012345678a"));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0)");
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('9876543210', 0)");
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
            dbConnection.executeUpdate("INSERT INTO customer VALUES (124, 'testCustomerName2', '0123456789')");
            assertFalse(CustomerModel.modifyCustomer(123, "testCustomerName", "0123456789"));
            assertTrue(CustomerModel.modifyCustomer(123, "testNewCustomerName", null));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testNewCustomerName");
            assertNull(cdao.getCustomer(123).getCustomerCard());
            assertTrue(CustomerModel.modifyCustomer(123, "testDifferentCustomerName", "9876543210"));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testDifferentCustomerName");
            assertEquals(cdao.getCustomer(123).getCustomerCard(), "9876543210");
            assertTrue(CustomerModel.modifyCustomer(123, "testYetAnotherCustomerName", ""));
            assertEquals(cdao.getCustomer(123).getCustomerName(), "testYetAnotherCustomerName");
            assertNull(cdao.getCustomer(123).getCustomerCard());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
    }

    @Test
    public void testAPIDeleteCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        try {
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.deleteCustomer(null));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.deleteCustomer(-1));
            assertNotNull(cdao.getCustomer(123));
            assertTrue(CustomerModel.deleteCustomer(123));
            assertNull(cdao.getCustomer(123));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAPIGetCustomer(){
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        try {
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.getCustomer(-1));
            assertThrows(InvalidCustomerIdException.class, () -> CustomerModel.getCustomer(null));
            assertNull(CustomerModel.getCustomer(123));
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL)");
            Customer result = CustomerModel.getCustomer(123);
            assertNotNull(result);
            assert(result.getId().equals(123));
            assertEquals(result.getCustomerName(), "testCustomerName");
            assertNull(result.getCustomerCard());
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearCustomers);
    }

    @Test
    public void testAPIGetAllCustomers(){
        dbConnection.executeUpdate(clearCustomers);
        assert(CustomerModel.getAllCustomers().size() == 0);
        dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName1', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (124, 'testCustomerName2', NULL)");
        dbConnection.executeUpdate("INSERT INTO customer VALUES (125, 'testCustomerName3', NULL)");
        assert(CustomerModel.getAllCustomers().size() == 3);
        dbConnection.executeUpdate(clearCustomers);
    }

    @Test
    public void testAPICreateCard(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        dbConnection.executeUpdate(clearLoyaltyCard);

        String result = LoyaltyCardModel.createCard();
        assertNotNull(result);
        assertTrue(LoyaltyCardModel.checkSerialNumberFormat(result));
        try {
            assertNotNull(lcdao.getCard(result));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearLoyaltyCard);
    }

    @Test
    public void testAPIAttachCardToCustomer(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
        try{
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            dbConnection.executeUpdate("INSERT INTO customer VALUES (123, 'testCustomerName', NULL);");
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            assertTrue(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));
            Customer result = cdao.getCustomerByCard("0123456789");
            assertNotNull(result);
            assertEquals(result.getCustomerCard(), "0123456789");

            assertThrows(InvalidCustomerIdException.class, () -> LoyaltyCardModel.attachCardToCustomer("0123456789", null));
            assertThrows(InvalidCustomerIdException.class, () -> LoyaltyCardModel.attachCardToCustomer("0123456789", -1));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer(null, 123));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer("", 123));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.attachCardToCustomer("012345678a", 123));
            assertFalse(LoyaltyCardModel.attachCardToCustomer("0123456789", 123));

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
    }

    @Test
    public void testAPIModifyPointsOnCard(){
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
        try{
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard(null, 100));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard("", 100));
            assertThrows(InvalidCustomerCardException.class, () -> LoyaltyCardModel.modifyPointsOnCard("012345678", 100));

            assertFalse(LoyaltyCardModel.modifyPointsOnCard("0123456789", 100));
            dbConnection.executeUpdate("INSERT INTO loyalty_card VALUES ('0123456789', 0);");
            assertFalse(LoyaltyCardModel.modifyPointsOnCard("0123456789", -1));
            assertTrue(LoyaltyCardModel.modifyPointsOnCard("0123456789", 100));
            assert(lcdao.getCard("0123456789").getPoints() == 100);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
    }

    @Test
    public void testAPIStartSaleTransaction(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        assertNotNull(sale.startSaleTransaction());
        assertFalse(sale.startSaleTransaction() <=0);
        int max= stdao.getMaxSaleTransactionId();
        assertTrue(sale.startSaleTransaction()>max);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }

    @Test
    public void testAPIAddProductToSale(){
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);

        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //transactionID null t1(null,123456789128,1)
        assertThrows(InvalidTransactionIdException.class,()->sale.addProductToSale(null,"123456789128",1));
        //transactionID 0 t2(0,123456789128,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.addProductToSale(0,"123456789128",0));
        //transaction id <0 t3(-1,123456789128,7)
        assertThrows(InvalidTransactionIdException.class,()->sale.addProductToSale(-1,"123456789128",7));
        //product code null t4(3,null,4)
        assertThrows(InvalidProductCodeException.class,()->sale.addProductToSale(4,null,4));
        //product code ""t5(4,"",2)
        assertThrows(InvalidProductCodeException.class,()->sale.addProductToSale(4,"",2));
        //product code not valid t6(1,023456789128,8)
        assertThrows(InvalidProductCodeException.class,()->sale.addProductToSale(1,"023456789128",8));
        //amount =0 t7(2,"0001110001116",0)
        assertThrows(InvalidQuantityException.class,()->sale.addProductToSale(2,"0001110001116",0));
        //amount <0 t8(1,"0001110001116",-1)
        assertThrows(InvalidQuantityException.class,()->sale.addProductToSale(1,"0001110001116",-1));

        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            //transaction not open
            Assert.assertFalse("Error in test T9",sale.addProductToSale(3,"123456789128",1));
            //product not available
            Assert.assertFalse("Error in test T10",sale.addProductToSale(sale.getTicketNumber(),"123456789128",1));
            //quantity not available
            Assert.assertFalse("Error in test T11",sale.addProductToSale(sale.getTicketNumber(),"8008008008006",81));
            //correct add to db
            int quantity= productTypeDAO.getProductByBarcode("8008008008006").getQuantity();
            assertTrue("Error in test T12",sale.addProductToSale(sale.getTicketNumber(),"8008008008006",1));
            //verificare che la quantità sia tolta dal db e che la sale abbia il ticket
            assertEquals("Error in test T13", (int) productTypeDAO.getProductByBarcode("8008008008006").getQuantity(), quantity - 1);
            Assert.assertFalse("Error in test T14", sale.getEntries().isEmpty());
        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidQuantityException | InvalidTransactionIdException | InvalidProductCodeException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
    }


    @Test
    public void testAPIAddProductToSaleRFIDDriver(){

        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);

        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 0, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 125, 1)");

        assertThrows(InvalidTransactionIdException.class, () -> sale.addProductToSaleRFID(null, "000000000000"));
        assertThrows(InvalidTransactionIdException.class, () -> sale.addProductToSaleRFID(-1, "000000000000"));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, ""));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, null));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, "12345678901"));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, "123456789a01"));

        try {
            sale.startSaleTransaction();
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000005"));
            assertTrue(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000000"));
            assertTrue(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000001"));

        } catch(Exception e){
            e.printStackTrace();
            fail();
        }

        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
    }

    @Test
    public void testAPIDeleteProductFromSaleRFID(){
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);

        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 0, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 125, 1)");

        sale.startSaleTransaction();
        sale.getEntries().add(new TicketEntryModel("123456789128",null,0,0.0,0.50));
        ((TicketEntryModel)sale.getEntries().get(0)).addRFID("000000000000");
        ((TicketEntryModel)sale.getEntries().get(0)).addRFID("000000000001");
        assertThrows(InvalidTransactionIdException.class, () -> sale.deleteProductFromSaleRFID(null, "00000000000"));
        assertThrows(InvalidTransactionIdException.class, () -> sale.deleteProductFromSaleRFID(-1, "000000000000"));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(sale.getTicketNumber(), ""));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(sale.getTicketNumber(), null));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "12345678901"));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "123456789a01"));

        try {
            assertTrue(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000000"));
            assertTrue(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000001"));
            assertFalse(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000000"));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }

        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
    }

    @Test
    public void testAPIEndSaleTransaction(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(-1));

        try {
            //false if this.id==null && sale== null  T4(4)
            Assert.assertFalse("Error in test T4",sale.endSaleTransaction(4));

            sale.startSaleTransaction();
            sale.setPaymentType(1);
            //false if this.id != id and sale == null
            Assert.assertFalse("Error in test T5",sale.endSaleTransaction(2));
            stdao.addSaleTransaction(sale);
            sale.startSaleTransaction();
            //transaction is close
            Assert.assertFalse("Error in test T6",sale.endSaleTransaction(1));
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.addProductToSale(sale.getTicketNumber(),productTypeModel1.getBarCode(),2);
            assertTrue("Error in test T7",sale.endSaleTransaction(sale.getTicketNumber()));
            //verify if the transaction is in db
            Assert.assertNotNull("Error in test T8", stdao.getSaleTransaction(2));
            //verify if the ticket is present
            TicketEntryDAOInterface tickDAO = new TicketEntryDAO();
            Assert.assertNotNull("Error in test T8",tickDAO.getSaleTicketEntries(2));
        } catch (InvalidTransactionIdException | MissingDAOParameterException | InvalidDAOParameterException | InvalidProductCodeException | InvalidQuantityException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
    }

    @Test
    public void testAPIReceiveCashPayment(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");

        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 1);
            sale.endSaleTransaction(sale.getTicketNumber());
            int max = stdao.getMaxSaleTransactionId();
            // if the cash is ==0 T1(NULL,0)
            assertThrows(InvalidPaymentException.class,()-> sale.receiveCashPayment(null,0));
            //if the cash <0 t2(null,-1)
            assertThrows(InvalidPaymentException.class,()-> sale.receiveCashPayment(null,-1));
            //the transaction is null t3(null,1)
            assertThrows(InvalidTransactionIdException.class, ()-> sale.receiveCashPayment(null,1));
            //the transaction is =0 t4(0,2)
            assertThrows(InvalidTransactionIdException.class, ()-> sale.receiveCashPayment(0,2));
            //the transaction <0 t5(-1,60.2)
            assertThrows(InvalidTransactionIdException.class, ()-> sale.receiveCashPayment(-1,60.2));

            //return -1 the sale does not exist t6(max+1,3.20)
            assertEquals("Error in T6", -1, sale.receiveCashPayment(max + 1, 3.20), 0.0);
            sale.startSaleTransaction();
            //sale exist but is open t7(max+1,4)
            assertEquals("Error in T7", -1, sale.receiveCashPayment(sale.getTicketNumber(), 4), 0.0);


            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 4);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 2);
            sale.applyDiscountRateToProduct(sale.getTicketNumber(), productTypeModel1.getBarCode(), 0.5);
            max= sale.getTicketNumber();
            sale.endSaleTransaction(sale.getTicketNumber());
            //return -1 if the cash is not enough t8(max,1)
            assertEquals("Error T8", -1, sale.receiveCashPayment(max, 1.0), 0.0);
            //return rest if the transaction is correct done no rest t9(max,3)
            assertEquals("Error T9", 0, sale.receiveCashPayment(max, 3.0), 0.0);
            //rest of 70cent t10(max,3.70)
            assertEquals("Error T10", 0.70, sale.receiveCashPayment(max, 3.7), 0.00001);

        } catch (InvalidQuantityException | InvalidTransactionIdException | InvalidDiscountRateException | MissingDAOParameterException | InvalidDAOParameterException | InvalidProductCodeException | InvalidPaymentException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }

    @Test
    public void testAPIReceiveCreditCardPayment(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");
        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        try {

            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 1);
            sale.endSaleTransaction(sale.getTicketNumber());
            int max = stdao.getMaxSaleTransactionId();
            //credit card empty t1(null,"")
            assertThrows(InvalidCreditCardException.class,()->sale.receiveCreditCardPayment(null,""));
            //creditcard null t2(null,null)
            assertThrows(InvalidCreditCardException.class,()->sale.receiveCreditCardPayment(null,null));
            //credit card not valid t3(null,0185062428408512)
            assertThrows(InvalidCreditCardException.class,()->sale.receiveCreditCardPayment(null,"0185062428408512"));
            //transactionID null t4(null,8185062428408512)
            assertThrows(InvalidTransactionIdException.class,()->sale.receiveCreditCardPayment(null,"8185062428408512"));
            //transactionID 0 t5(0,8185062428408512)
            assertThrows(InvalidTransactionIdException.class,()->sale.receiveCreditCardPayment(0,"8185062428408512"));
            //transaction id <0 t6(-1,8185062428408512)
            assertThrows(InvalidTransactionIdException.class,()->sale.receiveCreditCardPayment(-1,"8185062428408512"));

            //sale do not exist t7(max+1,8185062428408512)
            Assert.assertFalse("Error! in test T7", sale.receiveCreditCardPayment(max+1,"8185062428408512"));
            sale.startSaleTransaction();
            //sale exist but is open t8(sale.getticket,8185062428408512)
            Assert.assertFalse("Error in test T8", sale.receiveCreditCardPayment(sale.getTicketNumber(),"8185062428408512"));

            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 4);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 2);
            sale.applyDiscountRateToProduct(sale.getTicketNumber(), productTypeModel1.getBarCode(), 0.5);
            max= sale.getTicketNumber();
            sale.endSaleTransaction(sale.getTicketNumber());
            //false if the credit card does not exist in the file t9(max,4485370086510871)
            Assert.assertFalse("Error in test T9", sale.receiveCreditCardPayment(max,"5367780211133163"));
            //false if the credit card does not have enough money t10(max,4716258050958645)
            Assert.assertFalse("Error in test T10", sale.receiveCreditCardPayment(max,"4716258050958645"));
            //true se l'operazione va a buon fine T11() max,5100293991053009)
            assertTrue("Error in test T11", sale.receiveCreditCardPayment(max,"5100293991053009"));

        } catch (InvalidQuantityException | InvalidTransactionIdException | InvalidDiscountRateException | MissingDAOParameterException | InvalidDAOParameterException | InvalidProductCodeException |  InvalidCreditCardException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }

    @Test
    public void testAPIReturnCashPayment(){
        ReturnTransactionModel ret = new ReturnTransactionModel();

        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        UserDAO userDAO = new UserDAO();

        dbConnection.executeUpdate(queryUser);
        returnProductDAO.resetReturnProducts();
        returnTransactionDAO.resetReturnTransactions();
        ticketEntryDAO.resetTicketEntries();
        saleTransactionDAO.resetSaleTransactions();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();
        userDAO.resetUsers();

        try {
            assertThrows(InvalidTransactionIdException.class, ()->ret.returnCashPayment(null));
            assertThrows(InvalidTransactionIdException.class, ()->ret.returnCashPayment(0));
            ret.setID(1);
            assert(ret.returnCashPayment(1)==-1);
            assert(ret.returnCashPayment(returnTransactionDAO.getMaxReturnTransactionId()+1)==-1);

            DbConnection db = DbConnection.getInstance();
            String query = "INSERT INTO sale_transaction VALUES(null, 120.3, 1, 0.0, sysdate(), null);";
            db.executeUpdate(query);
            String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
            query = "INSERT INTO return_transaction VALUES(null, 10, " + opquery[0] + ",null);";
            db.executeUpdate(query);
            opquery = db.executeQuery("SELECT last_insert_id();").get(0);

            assert(ret.returnCashPayment(Integer.parseInt(opquery[0]))>0);
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
    public void testAPIComputePointsForSale(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");
        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->sale.computePointsForSale(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->sale.computePointsForSale(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->sale.computePointsForSale(-1));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 1);
            sale.endSaleTransaction(sale.getTicketNumber());
            int max = stdao.getMaxSaleTransactionId();
            //false if the ticketid==null&&sale==null t4(max+1)
            assertEquals("Error in test T4", -1, sale.computePointsForSale(max + 1));
            sale.startSaleTransaction(); //t5 not the same transaction
            assertEquals("Error in test T5", -1, sale.computePointsForSale(sale.getTicketNumber()+1));
            //t6 transaction open
            assertEquals("Error in test T6", 0, sale.computePointsForSale(sale.getTicketNumber()));
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            int index= sale.getTicketNumber();
            sale.endSaleTransaction(sale.getTicketNumber());
            //transaction close
            assertEquals("Error in test T7", 0, sale.computePointsForSale(index));


        } catch (InvalidQuantityException | InvalidTransactionIdException | InvalidDAOParameterException | MissingDAOParameterException | InvalidProductCodeException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }


    @Test
    public void testAPIApplyDiscountToSale(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");
        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //transactionID null t1(null,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToSale(null,0));
        //transactionID 0 t2(0,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToSale(0,0));
        //transaction id <0 t3(-1,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToSale(-1,0));
        //discount rate not valid t4(2,-1)
        assertThrows(InvalidDiscountRateException.class,()->sale.applyDiscountRateToSale(2,-1));
        //discount rate not valid t5(2,1.2)
        assertThrows(InvalidDiscountRateException.class,()->sale.applyDiscountRateToSale(2,1.2));

        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            Integer sale1 = sale.startSaleTransaction();
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel2.getBarCode(), 1);
            sale.setPaymentType(1);//sale 1 already payed
            sale.endSaleTransaction(sale.getTicketNumber());
            //false sale==null this.ticket ==null t6(3,0.5)
            Assert.assertFalse("Error in test T6", sale.applyDiscountRateToSale(3,0.5));
            sale.startSaleTransaction(); //t5 not the same transaction
            //false this.ticket!= transactionid sale==null t7(6,0.6)
            Assert.assertFalse("Error in test T7", sale.applyDiscountRateToSale(6,0.6));
            //false transaction already payed t8(1,0.4)
            Assert.assertFalse("Error in test T8", sale.applyDiscountRateToSale(1,0.4));
            //true transaction open t9(2,0.3)
            assertTrue("Error in test T9", sale.applyDiscountRateToSale(sale.getTicketNumber(),0.3));
            //verify
            assertEquals("Error in test T10",0.3,sale.getDiscountRate(),0.0);
            //true transaction close t11(1,0.2)
            Integer saleId = sale.startSaleTransaction();
            assertTrue("Error in test T11", sale.applyDiscountRateToSale(saleId,0.2));
            //verify
            assertEquals("Error in test T12",0.0,stdao.getSaleTransaction(sale1).getDiscountRate(),0.0);
        } catch (InvalidQuantityException | InvalidTransactionIdException | InvalidDAOParameterException | MissingDAOParameterException | InvalidProductCodeException | InvalidDiscountRateException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }

    @Test
    public void testAPIDeleteProductFromSale(){

        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //transactionID null t1(null,123456789128,1)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteProductFromSale(null,"123456789128",1));
        //transactionID 0 t2(0,123456789128,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteProductFromSale(0,"123456789128",0));
        //transaction id <0 t3(-1,123456789128,7)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteProductFromSale(-1,"123456789128",7));
        //product code null t4(3,null,4)
        assertThrows(InvalidProductCodeException.class,()->sale.deleteProductFromSale(4,null,4));
        //product code ""t5(4,"",2)
        assertThrows(InvalidProductCodeException.class,()->sale.deleteProductFromSale(4,"",2));
        //product code not valid t6(1,023456789128,8)
        assertThrows(InvalidProductCodeException.class,()->sale.deleteProductFromSale(1,"023456789128",8));
        //amount =0 t7(2,123456789128,0)
        assertThrows(InvalidQuantityException.class,()->sale.deleteProductFromSale(2,"123456789128",0));
        //amount <0 t7(1,123456789128,-1)
        assertThrows(InvalidQuantityException.class,()->sale.deleteProductFromSale(1,"123456789128",-1));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.addProductToSale(sale.getTicketNumber(), productTypeModel1.getBarCode(), 3);
            //transaction not open
            Assert.assertFalse("Error in test T8",sale.deleteProductFromSale(3,"123456789128",1));
            //product not available
            Assert.assertFalse("Error in test T9",sale.deleteProductFromSale(sale.getTicketNumber(),"123456789128",1));
            //product not present in transaction
            Assert.assertFalse("Error in test T10",sale.deleteProductFromSale(sale.getTicketNumber(),"0001110001116",81));
            //quantity not present
            Assert.assertFalse("Error in test T11",sale.deleteProductFromSale(sale.getTicketNumber(),"8008008008006",4));
            //modify the quantity
            assertTrue("Error in test T12",sale.deleteProductFromSale(sale.getTicketNumber(),"8008008008006",1));
            //verify
            assertEquals("Error in test T13",2,sale.getEntries().get(0).getAmount(),0.0);
            //delete the element
            assertTrue("Error in test T14",sale.deleteProductFromSale(sale.getTicketNumber(),"8008008008006",2));
            //verify
            assertTrue("Error in test T15",sale.getEntries().isEmpty());

        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidQuantityException | InvalidTransactionIdException | InvalidProductCodeException e) {
            e.printStackTrace();
        }

        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
    }
    @Test
    public void testAPIStartReturnTransactionDriver(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        ReturnTransactionModel saleR = new ReturnTransactionModel();
        ReturnTransactionDAOInterface salerDAO = new ReturnTransactionDAO();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate("insert into sale_transaction values(null, 37.2, null, null, sysdate(), null);");
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(-1));
        int maxS= stdao.getMaxSaleTransactionId();
        int maxR = salerDAO.getMaxReturnTransactionId();
        try {
            assertTrue(saleR.startReturnTransaction(maxS) >maxR);
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
    }
    @Test
    public void testAPIReturnProductDriver(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        ReturnTransactionModel saleR = new ReturnTransactionModel();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("insert into sale_transaction values(1, 37.2, null, null, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (1, 125, 2, 0);");


        //returnID null t1(null,"0001110001116",3)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProduct(null,"0001110001116",3));
        //returnID 0 t2(0,"0001110001116",2)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProduct(0,"0001110001116",2));
        //returnID <0 t3(-1,"0001110001116",76)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProduct(-1,"0001110001116",76));
        //product code null t4(3,null,3)
        assertThrows(InvalidProductCodeException.class,()->saleR.returnProduct(3,null,3));
        //product code ""t5(2,"",3)
        assertThrows(InvalidProductCodeException.class,()->saleR.returnProduct(2,"",3));
        //product code not valid t6(4,0001413001116,3)
        assertThrows(InvalidProductCodeException.class,()->saleR.returnProduct(4,"0001413001116",3));
        //amount =0 t7(6,"0001110001116",0)
        assertThrows(InvalidQuantityException.class,()->saleR.returnProduct(6,"0001110001116",0));
        //amount <0 t8(8,"0001110001116",-1)
        assertThrows(InvalidQuantityException.class,()->saleR.returnProduct(8,"0001110001116",-1));
        try {
            //transazione non esiste
            assertFalse(saleR.returnProduct(4,"0001110001116",1));
            saleR.startReturnTransaction(stdao.getMaxSaleTransactionId());
            //false se il prodotto non esiste
            assertFalse(saleR.returnProduct(saleR.getID(),"0001110001116",1));
            //false prodotto esiste ma non nella transazione
            assertFalse(saleR.returnProduct(saleR.getID(),"123456789135",2));
            //amount higher
            assertFalse(saleR.returnProduct(saleR.getID(),"123456789142",5));
            //true
            assertTrue(saleR.returnProduct(saleR.getID(),"123456789142",1));
        } catch (InvalidTransactionIdException | InvalidQuantityException | InvalidProductCodeException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
    }
    @Test
    public void testAPIEndReturnTransactionDriver(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        ReturnTransactionModel saleR = new ReturnTransactionModel();
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("insert into sale_transaction values(null, 37.2, null, null, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (1, 125, 2, 0);");

        //returnID null t1(null,"0001110001116",3)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(null,false));
        //returnID 0 t2(0,"0001110001116",2)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(0,false));
        //returnID <0 t3(-1,"0001110001116",76)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(-1,false));
        try {
            //transazione non esiste
            assertFalse(saleR.endReturnTransaction(4,true));
            saleR.startReturnTransaction(stdao.getMaxSaleTransactionId());
            //true
            assertTrue(saleR.endReturnTransaction(saleR.getID(),true));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
    }
    @Test
    public void testAPIDeleteReturnTransactionDriver(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        ReturnTransactionModel saleR = new ReturnTransactionModel();
        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("insert into sale_transaction values(1, 37.2, null, null, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (" + stdao.getMaxSaleTransactionId() + ", 125, 2, 0);");
        dbConnection.executeUpdate("INSERT INTO return_transaction VALUES (1, 2, 1,null);");
        dbConnection.executeUpdate("INSERT INTO return_product VALUES (1, 125, 2);");

        //returnID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(null));
        //returnID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(0));
        //returnID <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(-1));
        try {
            //transazione non esiste
            assertFalse(saleR.deleteReturnTransaction(4));
            saleR.startReturnTransaction(stdao.getMaxSaleTransactionId());
            //false transaction still open
            assertFalse(saleR.deleteReturnTransaction(saleR.getID()));
            assertTrue(saleR.deleteReturnTransaction(1));
            saleR.startReturnTransaction(stdao.getMaxSaleTransactionId());
            //false transaction payed
            saleR.startReturnTransaction(stdao.getMaxSaleTransactionId());
            int saleReturnID= saleR.getID();
            saleR.endReturnTransaction(saleR.getID(),true);
            returnDAO.updateReturnTransaction(new ReturnTransactionModel(saleReturnID,30.0,null,null));
            saleR.returnCashPayment(saleReturnID);
            assertFalse(saleR.deleteReturnTransaction(saleReturnID));
        } catch (InvalidTransactionIdException | MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
    }

    @Test
    public void testAPIRecordBalanceUpdate(){
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        dbConnection.executeUpdate(clearBalanceOperation);
        assertFalse(AccountBookModel.recordBalanceUpdate(abm, -10000));
        assertTrue(AccountBookModel.recordBalanceUpdate(abm, 10000));
        assert(balanceOperationDAO.getBalanceOperations().size() == 1);
        assert(balanceOperationDAO.getBalanceOperations().get(0).getMoney() == 10000);
        assertEquals(balanceOperationDAO.getBalanceOperations().get(0).getType(), BalanceOperationModel.Type.CREDIT.name());
        assertEquals(balanceOperationDAO.getBalanceOperations().get(0).getDate(), LocalDate.now());
        assertTrue(AccountBookModel.recordBalanceUpdate(abm, -5000));
        assert(balanceOperationDAO.getBalanceOperations().size() == 2);
        assert(balanceOperationDAO.getBalanceOperations().get(1).getMoney() == 5000);
        assertEquals(balanceOperationDAO.getBalanceOperations().get(1).getType(), BalanceOperationModel.Type.DEBIT.name());
        assertEquals(balanceOperationDAO.getBalanceOperations().get(1).getDate(), LocalDate.now());
        dbConnection.executeUpdate(clearBalanceOperation);
    }

    @Test
    public void testAPIGetCreditsAndDebits(){
        dbConnection.executeUpdate(clearBalanceOperation);
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (123, 'SALE', 155.50, '2019-09-08 17:13:45')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (124, 'ORDER', 37.50, '2019-07-08 19:23:00')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (125, 'RETURN', 12.50, '2019-12-12 12:12:12')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (126, 'DEBIT', 5000, '2020-08-08 17:13:45')");

        List<BalanceOperation> balanceOps = AccountBookModel.getCreditsAndDebits(null, null);
        assert(balanceOps.size() == 4);
        assert(balanceOps.get(0).getBalanceId() == 123);
        assert(balanceOps.get(1).getBalanceId() == 124);
        assert(balanceOps.get(2).getBalanceId() == 125);
        assert(balanceOps.get(3).getBalanceId() == 126);

        balanceOps = AccountBookModel.getCreditsAndDebits(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 31));
        assert(balanceOps.size() == 3);
        for(BalanceOperation b : balanceOps)
            assert(b.getBalanceId() != 126);

        balanceOps = AccountBookModel.getCreditsAndDebits(LocalDate.of(2019, 12, 31), LocalDate.of(2019, 1, 1)); // swapped dates
        assert(balanceOps.size() == 3);
        for(BalanceOperation b : balanceOps)
            assert(b.getBalanceId() != 126);

        balanceOps = AccountBookModel.getCreditsAndDebits(null, LocalDate.of(2019, 12, 11));
        assert(balanceOps.size() == 2);
        for(BalanceOperation b : balanceOps)
            assert(b.getBalanceId() != 126 && b.getBalanceId() != 125);

        balanceOps = AccountBookModel.getCreditsAndDebits(LocalDate.of(2019, 12, 11), null);
        assert(balanceOps.size() == 2);
        for(BalanceOperation b : balanceOps)
            assert(b.getBalanceId() != 123 && b.getBalanceId() != 124);

        dbConnection.executeUpdate(clearBalanceOperation);
    }

    @Test
    public void testAPIComputeBalance(){
        dbConnection.executeUpdate(clearBalanceOperation);
        assert(AccountBookModel.computeBalance(abm) == 0);
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (123, 'SALE', 155.50, '2019-09-08 17:13:45')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (124, 'ORDER', 37.50, '2019-07-08 19:23:00')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (125, 'RETURN', 12.50, '2019-12-12 12:12:12')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (126, 'DEBIT', 5000, '2020-08-08 17:13:45')");
        dbConnection.executeUpdate("INSERT INTO balance_operation VALUES (127, 'CREDIT', 7500, '2020-07-05 10:28:01')");
        assert(AccountBookModel.computeBalance(abm) == 2605.5);
        dbConnection.executeUpdate(clearBalanceOperation);
    }
    @Test
    public void testReturnProductRFIDriver()
    {
        dbConnection.executeUpdate(queryUser);
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearBalanceOperation);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearReturnProductRFID);
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
        try{
            UserModel.createUser("admin", "admin", UserModel.Role.Administrator.name());
            User loggedUser = UserModel.login("admin", "admin");
            assertNotNull(loggedUser);
            ReturnTransactionModel returnTrans = new ReturnTransactionModel();
           returnTrans.startReturnTransaction(201);
            assertTrue(returnTrans.returnProductRFID(returnTrans.getID(),"000000000002"));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(queryUser);
        dbConnection.executeUpdate(clearCustomers);
        dbConnection.executeUpdate(clearLoyaltyCard);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearBalanceOperation);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearReturnProductRFID);
    }
}
