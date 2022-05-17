package it.polito.ezshop.integrationTests.step8;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SaleTransactionRFIDDrivers {

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
    public void AddProductToSaleRFIDDriver(){
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
        entries.clear();

        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");

        try{
            String barcode = productEntryDAO.getProductEntryBarcode("000000000000");
            assertEquals("123456789128", barcode);
            assert(productEntryDAO.getProductEntryAvailability("000000000000") != 0);
            ProductType product = productTypeDAO.getProductByBarcode(barcode);
            assertNotNull(product);
            assertEquals("123456789128", product.getBarCode());
            TicketEntryModel ticket = new TicketEntryModel(barcode, product.getProductDescription(), 0, 0.0, product.getPricePerUnit());
            ticket.addRFID("000000000000");
            entries.add(ticket);
            assert(entries.size() == 1);
            assert(entries.get(0).getAmount() == 1);
            assertTrue(((TicketEntryModel)entries.get(0)).contains("000000000000"));

            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()-1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry("000000000000", false);

            assert(productTypeDAO.getProductByBarcode(barcode).getQuantity() == 9);
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 0);

            barcode = productEntryDAO.getProductEntryBarcode("000000000001");
            assertEquals("123456789128", barcode);
            assert(productEntryDAO.getProductEntryAvailability("000000000001") != 0);
            product = productTypeDAO.getProductByBarcode(barcode);
            assertNotNull(product);
            assertEquals("123456789128", product.getBarCode());

            for(TicketEntry t : entries){
                if(t.getBarCode().equals(barcode)){
                    ((TicketEntryModel) t).addRFID("000000000001");
                }
            }

            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()-1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry("000000000001", false);

            assert(productTypeDAO.getProductByBarcode(barcode).getQuantity() == 8);
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 0);

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
        entries.clear();
    }

    @Test
    public void DeleteProductFromSaleRFIDDriver(){
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(clearReturnProduct);
        dbConnection.executeUpdate(clearReturnProductRFID);
        dbConnection.executeUpdate(clearReturnTransaction);
        dbConnection.executeUpdate(clearProductInTransaction);
        dbConnection.executeUpdate(clearProductInTransactionRFID);
        dbConnection.executeUpdate(clearSaleTransaction);
        dbConnection.executeUpdate(clearProductEntry);
        dbConnection.executeUpdate(clearProductType);
        dbConnection.executeUpdate(clearBalanceOperation);
        entries.clear();

        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000000', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");

        try {
            String barcode = productEntryDAO.getProductEntryBarcode("000000000000");
            ProductType product = productTypeDAO.getProductByBarcode(barcode);
            TicketEntryModel ticket = new TicketEntryModel(barcode, product.getProductDescription(), 0, 0.0, product.getPricePerUnit());
            ticket.addRFID("000000000000");
            productEntryDAO.updateProductEntry("000000000000", false);
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()-1, 0.0, null, null, null, null));

            barcode = productEntryDAO.getProductEntryBarcode("000000000000");
            product = productTypeDAO.getProductByBarcode(barcode);
            ticket.addRFID("000000000001");
            productEntryDAO.updateProductEntry("000000000001", false);
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()-1, 0.0, null, null, null, null));

            entries.add(ticket);
            assert(((TicketEntryModel) entries.get(0)).getRFIDs().size() == 2);
            assert(entries.get(0).getAmount() == 2);

            assert(productTypeDAO.getProductByBarcode(barcode).getQuantity() == 8);
            barcode = productEntryDAO.getProductEntryBarcode("000000000000");
            assertNotNull(barcode);
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 0);
            barcode = productEntryDAO.getProductEntryBarcode("000000000001");
            assertNotNull(barcode);
            assert(productEntryDAO.getProductEntryAvailability("000000000001") == 0);

            int index = entries.stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(barcode);

            assert(index != -1);
            assertTrue(((TicketEntryModel) entries.get(index)).contains("000000000001"));

            assert(entries.get(index).getAmount() == 2);
            ((TicketEntryModel) entries.get(index)).removeRFID("000000000001");
            assertFalse(((TicketEntryModel) entries.get(index)).contains("000000000001"));
            assert(entries.get(index).getAmount() == 1);
            product = productTypeDAO.getProductByBarcode(barcode);
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()+1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry("000000000001", true);

            assert(productTypeDAO.getProductByBarcode(barcode).getQuantity() == 9);
            assert(productEntryDAO.getProductEntryAvailability("000000000001") == 1);
            assertTrue(((TicketEntryModel) entries.get(index)).contains("000000000000"));
            assert(entries.get(0).getAmount() == 1);
            entries.remove(index);
            barcode = productEntryDAO.getProductEntryBarcode("000000000000");
            product = productTypeDAO.getProductByBarcode(barcode);
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()+1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry("000000000000", true);

            assert(entries.size() == 0);
            assert(productTypeDAO.getProductByBarcode(barcode).getQuantity() == 10);
            assert(productEntryDAO.getProductEntryAvailability("000000000000") == 1);

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
        entries.clear();
    }
}
