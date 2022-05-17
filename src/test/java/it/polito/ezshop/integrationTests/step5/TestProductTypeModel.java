package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestProductTypeModel {
    @Test
    public void testCreateProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdateProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testDeleteProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testGetAllProductTypeModel() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testGetProductTypeByBarcode() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod2 = new ProductTypeModel(null, "1234567891231", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        ProductTypeModel prod3 = new ProductTypeModel(null, "7418529637899", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
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
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdateQuantity() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        int toBeAdded = 4;

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdatePosition() {
        ProductTypeModel prod1 = new ProductTypeModel(null, "8008008008006", "Apple", 0.5, 80, 0.2, "Notes", 1, "A", 4);
        String newLocation = "11-U-11";

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
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

            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException | InvalidProductIdException | InvalidLocationException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }
}
