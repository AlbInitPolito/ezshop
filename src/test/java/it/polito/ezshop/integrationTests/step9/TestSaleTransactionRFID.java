package it.polito.ezshop.integrationTests.step9;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.SaleTransactionModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestSaleTransactionRFID {

    DbConnection dbConnection = DbConnection.getInstance();
    String clearReturnProduct = "DELETE FROM return_product";
    String clearReturnTransaction = "DELETE FROM return_transaction";
    String clearProductInTransaction = "DELETE FROM product_in_transaction";
    String clearSaleTransaction = "DELETE FROM sale_transaction";
    String clearProductType = "DELETE FROM product_type";
    String clearBalanceOperation = "DELETE FROM balance_operation";
    String clearProductEntry = "DELETE FROM product_entry";
    String clearReturnProductRFID = "DELETE FROM return_product_rfid";
    String clearProductInTransactionRFID = "DELETE FROM product_in_transaction_rfid";
    SaleTransactionModel sale = new SaleTransactionModel();

    @Test
    public void testAddProductToSaleRFID(){
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

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
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");

        assertThrows(InvalidTransactionIdException.class, () -> sale.addProductToSaleRFID(null, "000000000000"));
        assertThrows(InvalidTransactionIdException.class, () -> sale.addProductToSaleRFID(-1, "000000000000"));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, ""));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, null));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, "12345678901"));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, "12345678a901"));

        try{
            assertFalse(sale.addProductToSaleRFID(1, "000000000000"));
            sale.startSaleTransaction();
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000006")); // Non-existing RFID
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000003")); // Unavailable product
            assertThrows(InvalidQuantityException.class, () -> sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000005"));
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 1);
            assert(productEntryDAO.getProductEntryAvailability("000000000001") == 1);
            assert(productEntryDAO.getProductEntryAvailability("000000000002") == 1);
            assertTrue(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000000"));
            assertTrue(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000001"));
            assertTrue(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000002"));
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000000"));
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000001"));
            assertFalse(sale.addProductToSaleRFID(sale.getTicketNumber(), "000000000002"));
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 0);
            assert(productEntryDAO.getProductEntryAvailability("000000000001") == 0);
            assert(productEntryDAO.getProductEntryAvailability("000000000002") == 0);

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
    public void testDeleteProductFromSaleRFID(){
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
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
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 0)");

        assertThrows(InvalidTransactionIdException.class, () -> sale.deleteProductFromSaleRFID(null, "000000000000"));
        assertThrows(InvalidTransactionIdException.class, () -> sale.deleteProductFromSaleRFID(-1, "000000000000"));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(1, ""));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(1, null));
        assertThrows(InvalidRFIDException.class, () -> sale.addProductToSaleRFID(1, "12345678901"));
        assertThrows(InvalidRFIDException.class, () -> sale.deleteProductFromSaleRFID(1, "12345678a901"));
        try {
            assertFalse(sale.deleteProductFromSaleRFID(2, "000000000000"));
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel("123456789128",null,0,0.0,0.50));
            ((TicketEntryModel)sale.getEntries().get(0)).addRFID("000000000000");
            ((TicketEntryModel)sale.getEntries().get(0)).addRFID("000000000001");
            saleTransactionDAO.addSaleTransaction(sale);
            assertFalse(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000006")); // Non existing RFID
            assertFalse(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000004")); // Available product, and therefore not part of the transaction
            assertFalse(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000005")); // Existing RFID, unavailable product, but its product type is not part of the transaction
            assertFalse(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000002")); // Existing RFID, unavailable product, its product type is part of the transaction, but not this specific product entry
            assertTrue(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000000"));
            assertFalse(((TicketEntryModel) sale.getEntries().get(0)).contains("000000000000"));
            assertTrue(sale.deleteProductFromSaleRFID(sale.getTicketNumber(), "000000000001"));
            assert((sale.getEntries().size() == 0));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
