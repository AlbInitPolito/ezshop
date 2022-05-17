package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SaleTransactionDriversStep4 {

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
    List<TicketEntry> entries = new ArrayList<>();
    @Test
    public void testStartSaleTransactionDriver(){
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        assertNotNull(stdao.getMaxSaleTransactionId());
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
    }
    @Test
    public void testDeleteProductFromSaleDriver() {
        ProductTypeDAO ptdao = new ProductTypeDAO();
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        try {
            ProductType toBeAdded = ptdao.getProductByBarcode("123456789128");
            TicketEntry entry = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            assert (entry.getAmount() - 3 >= 0);
            toBeAdded.setQuantity(toBeAdded.getQuantity() + 3);
            toBeAdded.setProductDescription(null);
            toBeAdded.setNote(null);
            toBeAdded.setLocation(null);
            ptdao.updateProductType(toBeAdded);
            assert (ptdao.getProductByBarcode("123456789128").getQuantity() == 13);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
    }
    @Test
    public void testAddProductToSaleDriver() {
        ProductTypeDAO ptdao = new ProductTypeDAO();
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
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        try {
            ProductType toBeAdded = ptdao.getProductByBarcode("123456789128");
            assert (toBeAdded.getQuantity() - 3 > 0);
            TicketEntry entry = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            entries.add(entry);
            toBeAdded.setQuantity(toBeAdded.getQuantity() - 3);
            toBeAdded.setProductDescription(null);
            toBeAdded.setNote(null);
            toBeAdded.setLocation(null);
            ptdao.updateProductType(toBeAdded);
            assert (ptdao.getProductByBarcode("123456789128").getQuantity() == 7);
            assertTrue(entries.contains(entry));
            assert (entries.size() == 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
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
    public void testApplyDiscountRateToProduct() {
        ProductTypeDAO ptdao = new ProductTypeDAO();
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        try {
            ProductType toBeAdded = ptdao.getProductByBarcode("123456789128");
            assert (toBeAdded != null);
            TicketEntry entry = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            entries.add(entry);
            int index= entries.indexOf(entry);
            assertTrue(entries.contains(entry));
            entries.get(index).setDiscountRate(0.5);
            assert (entries.get(index).getDiscountRate()==0.5);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
    }
}
