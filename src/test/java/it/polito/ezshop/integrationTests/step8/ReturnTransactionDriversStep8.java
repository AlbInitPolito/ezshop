package it.polito.ezshop.integrationTests.step8;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.ReturnTransactionModel;
import it.polito.ezshop.model.SaleTransactionModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ReturnTransactionDriversStep8 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query1 = "DELETE FROM return_transaction";
    String query2 = "DELETE FROM sale_transaction";
    String query3 = "DELETE FROM product_in_transaction";
    String query8 = "DELETE FROM product_in_transaction_rfid";
    String query7 = "DELETE FROM product_entry";
    String query4 = "DELETE FROM product_type";
    String query5 = "DELETE FROM balance_operation";
    String query6 = "DELETE FROM return_product";
    String query9 = "DELETE FROM return_product_rfid";

    @Test
    public void testStartReturnTransactionDriver() {
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        SaleTransaction sale;
        ReturnTransactionDAO rtdao = new ReturnTransactionDAO();
        ReturnTransactionModel returnTrans = new ReturnTransactionModel();
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 18.75, 1, 0.0, sysdate(), null);");

        try {
            assertNull(stdao.getSaleTransaction(200));
            sale = stdao.getSaleTransaction(201);
            assertNotNull(sale);
            assertNotNull(rtdao.getMaxReturnTransactionId());
            returnTrans.setID(rtdao.getMaxReturnTransactionId() + 1);
            returnTrans.setTransactionID(201);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testReturnProductDriver() {
        ProductTypeDAO ptdao = new ProductTypeDAO();
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        SaleTransaction sale;
        ProductType prod;
        ReturnTransactionModel returnTrans = new ReturnTransactionModel();
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (201, 123, 3, 0.0)");

        try {
            sale = stdao.getSaleTransaction(201);
            prod = ptdao.getProductByBarcode("123456789128");
            assertNotNull(sale);
            assertNotNull(prod);
            int index = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf("123456789128");
            assert (index != -1);
            TicketEntry ticket = sale.getEntries().get(index);
            assert (ticket.getAmount() - 2 > 0);
            ticket.setAmount(2);
            returnTrans.getEntries().add(ticket);
            assert (returnTrans.getEntries().size() == 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testEndReturnTransaction() {
        ProductTypeDAO ptdao = new ProductTypeDAO();
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        SaleTransaction sale;
        ReturnTransactionDAO rtdao = new ReturnTransactionDAO();
        ReturnProductDAO rpdao = new ReturnProductDAO();
        ReturnTransactionModel returnTrans = new ReturnTransactionModel();
        ReturnProductRfidDAO returnProductRfidDAO = new ReturnProductRfidDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        returnTrans.setID(null);
        returnTrans.setTransactionID(stdao.getMaxSaleTransactionId() + 1);
        TicketEntryDAO tedao = new TicketEntryDAO();
        List<TicketEntry> entries = new ArrayList<>();
        double discount, total = 0;
        Integer id = rtdao.getMaxReturnTransactionId() + 1;
        returnTrans.setID(id);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(NULL, 1.50, 1, 0.0, sysdate(), null);");
        System.out.println("StDAO max sale transaction ID: " + stdao.getMaxSaleTransactionId());
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (" + stdao.getMaxSaleTransactionId() + ", 123, 3, 0.0)");

        try {
            returnTrans.setTransactionID(stdao.getMaxSaleTransactionId());
            ProductType toBeAdded = ptdao.getProductByBarcode("123456789128");
            TicketEntry entry1 = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            entries.add(entry1);
            returnTrans = rtdao.addReturnTransaction(returnTrans);
            sale = stdao.getSaleTransaction(stdao.getMaxSaleTransactionId());
            assertNotNull(sale);
            List<TicketEntry> listT = sale.getEntries();
            if (listT.size() == 0) {
                double toBeRemoved = 0.0;
                List<String> listRFID = returnProductRfidDAO.getReturnProductRfids(returnTrans.getID());
                for (String RFID : listRFID) {
                    productEntryDAO.updateProductEntry(RFID, true);
                    ticketEntryRfidDAO.removeTicketEntryRfid(RFID, sale.getTicketNumber());
                    String barcode = productEntryDAO.getProductEntryBarcode(RFID);
                    ProductType prod = productTypeDAO.getProductByBarcode(barcode);
                    toBeRemoved += ((ProductTypeModel) prod).getPricePerUnit() * (1 - ((ProductTypeModel) prod).getDiscountRate());
                    returnProductRfidDAO.addReturnProductRfid(returnTrans.getID(), RFID);
                }
                for (TicketEntry t : entries) {
                    ProductType tmpProduct = ptdao.getProductByBarcode(t.getBarCode());
                    ptdao.updateProductType(new ProductTypeModel(tmpProduct.getId(), tmpProduct.getBarCode(), null, tmpProduct.getPricePerUnit(), tmpProduct.getQuantity() + t.getAmount(),
                            0.0, null, null, null, null));
                }
                toBeRemoved = toBeRemoved * (1 - sale.getDiscountRate());
                total = sale.getPrice() - toBeRemoved;
                returnTrans.setReturnedValue(toBeRemoved);

            } else {
                List<String> list = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList());

                for (TicketEntry ticket : entries) {
                    int index = list.indexOf(ticket.getBarCode());
                    sale.getEntries().get(index).setAmount(sale.getEntries().get(index).getAmount() - ticket.getAmount());
                    tedao.updateTicketEntry(new TicketEntryModel(ticket.getBarCode(), null, sale.getEntries().get(index).getAmount(), 0.0, sale.getEntries().get(index).getPricePerUnit()), 201);
                    ProductType toUpdate = ptdao.getProductByBarcode(ticket.getBarCode());
                    toUpdate.setProductDescription(null);
                    toUpdate.setQuantity(toUpdate.getQuantity() + ticket.getAmount());
                    toUpdate.setLocation(null);
                    ptdao.updateProductType(toUpdate);
                    rpdao.addReturnProduct(ticket, returnTrans.getID());
                }

                for (TicketEntry obj : sale.getEntries()) {
                    if (obj.getDiscountRate() != 0.0)
                        discount = obj.getDiscountRate();
                    else discount = ((ProductTypeModel) ptdao.getProductByBarcode(obj.getBarCode())).getDiscountRate();
                    total = total + (obj.getAmount() * (obj.getPricePerUnit() * (1 - discount)));
                }
            }

            total = total * (1 - sale.getDiscountRate());
            returnTrans.setReturnedValue(sale.getPrice() - total);
            returnTrans.setTransactionID(null);
            rtdao.updateReturnTransaction(returnTrans);
            assertNotNull(rtdao.getReturnTransaction(id));
            assert (rtdao.getReturnTransaction(id).getReturnedValue() == sale.getPrice() - total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testDeleteReturnTransactionDriver() {
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        ReturnProductDAO returnProductDAO = new ReturnProductDAO();
        ReturnTransactionModel returnTrans = new ReturnTransactionModel();
        ReturnProductRfidDAO returnProductRfidDAO = new ReturnProductRfidDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();

        dbConnection.executeUpdate(query9);
        dbConnection.executeUpdate(query8);
        dbConnection.executeUpdate(query7);
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        returnTrans.setID(null);
        returnTrans.setTransactionID(saleTransactionDAO.getMaxSaleTransactionId() + 1);
        TicketEntryDAO tedao = new TicketEntryDAO();
        List<TicketEntry> entries = new ArrayList<>();
        double discount, total = 0;
        Integer id = returnTransactionDAO.getMaxReturnTransactionId() + 1;
        returnTrans.setID(id);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES(1, '8008008008006', null, 0.2, 180, null, 1, 'C', 2)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES(2, '123456789128', null, 0.3, 150, null, 2, 'D', 3)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 1, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 1, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 1, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 2, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 2, 1)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(202, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(203, 0.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (201, '000000000002')");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (202, '000000000003')");
        dbConnection.executeUpdate("INSERT INTO return_transaction VALUES (1, null, 201, null);");
        dbConnection.executeUpdate("INSERT INTO return_product_rfid VALUES (1, '000000000002');");

        try {
            returnTrans.setTransactionID(saleTransactionDAO.getMaxSaleTransactionId());
            ProductType toBeAdded = productTypeDAO.getProductByBarcode("123456789128");
            TicketEntry entry1 = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            entries.add(entry1);

            assert (returnTrans != null);

            List<String> listRFID = returnProductRfidDAO.getReturnProductRfids(returnTrans.getID());
            sale = saleTransactionDAO.getSaleTransaction(returnTrans.getTransactionID());

            if(!listRFID.isEmpty()) {
                double toAdd=0;
                for ( String element: listRFID) {
                    productEntryDAO.updateProductEntry(element, false);
                    ticketEntryRfidDAO.addTicketEntryRfid(element,returnTrans.getTransactionID());
                    String barcode = productEntryDAO.getProductEntryBarcode(element);
                    ProductType tmpProduct = productTypeDAO.getProductByBarcode(barcode);
                    toAdd += tmpProduct.getPricePerUnit();
                    productTypeDAO.updateProductType(new ProductTypeModel(tmpProduct.getId(), tmpProduct.getBarCode(), null, tmpProduct.getPricePerUnit(), tmpProduct.getQuantity() - 1,
                            0.0, null, null, null, null));
                    ReturnProductRfidDAOInterface returnProductRFIDDAO = new ReturnProductRfidDAO();
                    returnProductRFIDDAO.removeReturnProductRfid(returnTrans.getID(),element);
                }
                returnTransactionDAO.removeReturnTransaction(returnTrans.getID());
                toAdd = toAdd *(1-sale.getDiscountRate());
                total = sale.getPrice() + toAdd;
            } else {
                ProductTypeDAOInterface productDAO = new ProductTypeDAO();
                TicketEntryDAOInterface ticketDAO = new TicketEntryDAO();
                ReturnProductDAO retpDAO = new ReturnProductDAO();
                ProductType product;
                List<String> list = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList());
                returnTrans.setEntries(retpDAO.getReturnProducts(returnTrans.getID()));
                for (TicketEntry ticket : returnTrans.getEntries()) {
                    int index = list.indexOf(ticket.getBarCode());
                    sale.getEntries().get(index).setAmount(sale.getEntries().get(index).getAmount() + ticket.getAmount());
                    ticketDAO.updateTicketEntry(new TicketEntryModel(ticket.getBarCode(), null, sale.getEntries().get(index).getAmount(), -1, -1), returnTrans.getTransactionID());
                    product = productDAO.getProductByBarcode(ticket.getBarCode());
                    productDAO.updateProductType(new ProductTypeModel(product.getId(), product.getBarCode(), null, null, product.getQuantity() - ticket.getAmount(), null, null, null, null, null));
                    ReturnProductDAOInterface returnproductDAO = new ReturnProductDAO();
                    returnproductDAO.removeReturnProduct(ticket.getBarCode(), returnTrans.getID());
                }
                returnTransactionDAO.removeReturnTransaction(returnTrans.getID());
                for (TicketEntry obj : sale.getEntries()) {
                    if (obj.getDiscountRate() != 0.0)
                        discount = obj.getDiscountRate();
                    else
                        discount = ((ProductTypeModel) productDAO.getProductByBarcode(obj.getBarCode())).getDiscountRate();
                    total = total + (obj.getAmount() * (obj.getPricePerUnit() * (1 - discount)));
                }

                total = total * (1 - sale.getDiscountRate());

            }
            saleTransactionDAO.updateSaleTransaction(new SaleTransactionModel(sale.getTicketNumber(), null, total, 0, null, null, sale.getEntries()));

            assert (returnTransactionDAO.getReturnTransaction(returnTrans.getID()) == null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbConnection.executeUpdate(query9);
        dbConnection.executeUpdate(query8);
        dbConnection.executeUpdate(query7);
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testReturnProductRFIDriver() {
        ProductEntryDAO ptdao = new ProductEntryDAO();
        SaleTransactionDAOInterface stdao = new SaleTransactionDAO();
        SaleTransaction sale;
        ReturnTransactionModel returnTrans = new ReturnTransactionModel();
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query7);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
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

            String barcode = ptdao.getProductEntryBarcode("000000000002");
            assertNotNull(barcode);
            assert (ptdao.getProductEntryAvailability("000000000002") != 1);
            sale = stdao.getSaleTransaction(201);
            assertNotNull(sale);
            int index = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(barcode);
            assert (index != -1);
            TicketEntryRfidDAO ticketRFIDDAO = new TicketEntryRfidDAO();
            assert (ticketRFIDDAO.getSaleTicketEntriesRfid(sale.getTicketNumber()).contains("000000000002"));
            TicketEntry ticket = sale.getEntries().get(index);
            ticket.setAmount(1);
            returnTrans.getEntries().add(ticket);
            assert (returnTrans.getEntries().size() == 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query6);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query7);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
}
