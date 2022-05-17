package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductTypeModelDrivers {
    @Test
    public void testCreateProductTypeDriver() {
        String barcode = "8008008008006";
        String productDescription = "Apple";
        Double pricePerUnit = 0.5;
        Integer quantity = 80;
        Double discountRate = 0.2;
        String notes = "Freshness";
        Integer aisleID = 1;
        String rackID = "F";
        Integer levelID = 3;

        ProductTypeModel product = new ProductTypeModel(barcode, productDescription,
                pricePerUnit, quantity, discountRate,
                notes, aisleID, rackID, levelID);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        /* Reset */
        productEntryDAO.resetProductEntries();
        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            ProductTypeModel tmp = (ProductTypeModel) productTypeDAO.getProductByBarcode(barcode);
            assert (tmp == null);

            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();
            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdateProductDriver() {
        String barcode = "8008008008006";
        String productDescription = "Apple";
        Double pricePerUnit = 0.5;
        Integer quantity = 80;
        Double discountRate = 0.2;
        String notes = "Freshness";
        Integer aisleID = 1;
        String rackID = "F";
        Integer levelID = 3;

        String newDescription = "Yo";
        double newPrice = 0.4;
        String newNote = "Absolutely yo.";

        ProductTypeModel product = new ProductTypeModel(barcode, productDescription,
                pricePerUnit, quantity, discountRate,
                notes, aisleID, rackID, levelID);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);

            product.setLocation("4-A-5");
            assert (productTypeDAO.updateProductType(product));

            product = (ProductTypeModel) productTypeDAO.getProductById(product.getId()) ;
            assert (product != null);

            product.setProductDescription(newDescription);
            product.setBarCode(null);
            product.setPricePerUnit(newPrice);
            product.setNote(newNote);

            assert (productTypeDAO.updateProductType(product));

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testDeleteProductTypeDriver() {
        String barcode = "8008008008006";
        String productDescription = "Apple";
        Double pricePerUnit = 0.5;
        Integer quantity = 80;
        Double discountRate = 0.2;
        String notes = "Freshness";
        Integer aisleID = 1;
        String rackID = "F";
        Integer levelID = 3;

        ProductTypeModel product = new ProductTypeModel(barcode, productDescription,
                pricePerUnit, quantity, discountRate,
                notes, aisleID, rackID, levelID);

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);
            assert (productTypeDAO.removeProductType(product.getId()));

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testGetAllProductTypesDriver() {
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();
        List<ProductType> productTypeList;

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));
            productTypeDAO.addProductType(new ProductTypeModel("1234567891231", "Babà", 0.6,
                    90, 0.1, "Mmmh", 2, "D", 1));
            productTypeDAO.addProductType(new ProductTypeModel("7418529637899", "Pizza", 0.7,
                    100, 0.0, "Ja", 3, "A", 2));

            productTypeList = productTypeDAO.getProducts();

            assert (productTypeList != null);
            assert (productTypeList.size() == 3);

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testGetProductTypeByBarCodeDriver() {
        ProductTypeModel product = new ProductTypeModel("8008008008006", "Apple", 0.5,
                80, 0.2, "Freshness", 1, "F", 3);
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);

            product = (ProductTypeModel) productTypeDAO.getProductByBarcode(product.getBarCode());
            assert (product != null);

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testGetProductsByDescriptionDriver() {
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();
        List<ProductType> productTypeList;
        List<ProductType> out = new ArrayList<>();

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Pizza Margherita", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));
            productTypeDAO.addProductType(new ProductTypeModel("1234567891231", "Pizza Marinara", 0.6,
                    90, 0.1, "Mmmh", 2, "D", 1));
            productTypeDAO.addProductType(new ProductTypeModel("7418529637899", "Pizza Benevento", 0.7,
                    100, 0.0, "Ja", 3, "A", 2));
            productTypeDAO.addProductType(new ProductTypeModel("0001112223332", "Amatriciana", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));
            productTypeDAO.addProductType(new ProductTypeModel("0001112223349", "Cacio e pepe", 0.6,
                    90, 0.1, "Mmmh", 2, "D", 1));
            productTypeDAO.addProductType(new ProductTypeModel("0001112223356", "Carbonara", 0.7,
                    100, 0.0, "Ja", 3, "A", 2));

            productTypeList = productTypeDAO.getProducts();
            assert (productTypeList != null);

            for (ProductType p : productTypeList)
                if (p.getProductDescription().contains("Pizza"))
                    out.add(p);

            assert (out.size() == 3);

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdateQuantityDriver() {
        ProductTypeModel product = new ProductTypeModel("8008008008006", "Apple", 0.5,
                80, 0.2, "Freshness", 1, "F", 3);
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();
        Integer toBeAdded = 60;

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);
            assert (product.getQuantity() + toBeAdded >= 0);
            product.setQuantity(product.getQuantity() + toBeAdded);

            product.setProductDescription(null);
            product.setBarCode(null);
            product.setPricePerUnit(null);
            product.setNote(null);
            product.setDiscountRate(null);

            assert (productTypeDAO.updateProductType(product));

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }

    @Test
    public void testUpdatePositionDriver() {
        ProductTypeModel product = new ProductTypeModel("8008008008006", "Apple", 0.5,
                80, 0.2, "Freshness", 1, "F", 3);
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        productTypeDAO.resetProductTypes();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        UserDAO userDAO = new UserDAO();
        String newLocation = "10-D-10";

        /* Reset */
        productEntryDAO.resetProductEntries();

        ticketEntryRfidDAO.resetTicketEntryRfid();
        productTypeDAO.resetProductTypes();
        saleTransactionDAO.resetSaleTransactions();
        userDAO.resetUsers();

        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            assert (product != null);

            product.setQuantity(null);
            product.setProductDescription(null);
            product.setBarCode(null);
            product.setPricePerUnit(null);
            product.setNote(null);
            product.setDiscountRate(null);
            product.setLocation(newLocation);

            assert (productTypeDAO.updateProductType(product));

        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();

            ticketEntryRfidDAO.resetTicketEntryRfid();
            productTypeDAO.resetProductTypes();
            saleTransactionDAO.resetSaleTransactions();
            userDAO.resetUsers();
        }
    }
}
