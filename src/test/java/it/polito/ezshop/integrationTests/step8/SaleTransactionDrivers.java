package it.polito.ezshop.integrationTests.step8;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.*;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SaleTransactionDrivers {

    DbConnection dbConnection = DbConnection.getInstance();
    String query0 = "DELETE FROM return_product";
    String query1 = "DELETE FROM return_transaction";
    String query2 = "DELETE FROM product_in_transaction";
    String query3 = "DELETE FROM sale_transaction";
    String query4 = "DELETE FROM product_type";
    String query5 = "DELETE FROM balance_operation";
    List<TicketEntry> entries = new ArrayList<>();



    @Test
    public void testEndSaleTransactionDriver() {
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        ProductTypeDAO ptdao = new ProductTypeDAO();
        SaleTransactionModel sale = new SaleTransactionModel();
        TicketEntryDAO tedao = new TicketEntryDAO();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        try {
            ProductType toBeAdded = ptdao.getProductByBarcode("123456789128");
            TicketEntry entry1 = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 3, 0.0, toBeAdded.getPricePerUnit());
            toBeAdded = ptdao.getProductByBarcode("123456789135");
            TicketEntry entry2 = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 2, 0.0, toBeAdded.getPricePerUnit());
            toBeAdded = ptdao.getProductByBarcode("123456789142");
            TicketEntry entry3 = new TicketEntryModel(toBeAdded.getBarCode(), toBeAdded.getProductDescription(), 4, 0.0, toBeAdded.getPricePerUnit());
            entries.add(entry1);
            entries.add(entry2);
            entries.add(entry3);
            SaleTransaction target = stdao.getSaleTransaction(123);
            assertNull(target);
            sale.setTime(Time.valueOf(LocalTime.now()));
            sale.setDate(Date.valueOf(LocalDate.now()));
            sale.setPaymentType(0);
            sale.setEntries(entries);
            sale.setTicketNumber(stdao.getMaxSaleTransactionId() + 1);
            SaleTransaction result = stdao.addSaleTransaction(sale);
            Integer id = stdao.getMaxSaleTransactionId();
            assertNotNull(result);
            entries.forEach(element -> {
                try {
                    tedao.addTicketEntry(element, id);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testReceiveCashPaymentDriver() {
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(1, 0.0, 0, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES( 1,123,4,null)");
        try {
            SaleTransaction target = stdao.getSaleTransaction(123);
            assertNull(target);
            target = stdao.getSaleTransaction(1);
            assertNotNull(target);
            double total = 23;
            double cash = 50;
            //get the rest
            double rest;
            rest = cash - total;
            assertTrue(rest > 0);
            //update the saleTransaction
            stdao.updateSaleTransaction(new SaleTransactionModel(target.getTicketNumber(), 1, total, -1, null, null, null));
            //verify all
            assertEquals(stdao.getSaleTransaction(1).getPrice(), total, 0.0);
            assertEquals(stdao.getSaleTransaction(1).getPaymentType(), 1, 0.0);
            //introduce a new balance Operation
            BalanceOperationDAO operation = new BalanceOperationDAO();
            int balanceID = operation.addBalanceOperation(new BalanceOperationModel(0, null, total, BalanceOperationModel.Type.SALE)).getBalanceId();
            assertEquals(operation.getBalanceOperation(balanceID).getMoney(), total, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testReceiveCreditCardPaymentDriver() {
        SaleTransactionDAO stdao = new SaleTransactionDAO();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(1, 0.0, 0, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES( 1,123,4,null);");
        try {
            SaleTransaction target = stdao.getSaleTransaction(123);
            assertNull(target);
            target = stdao.getSaleTransaction(1);
            assertNotNull(target);
            List<CreditCardModel> listCard = CreditCardModel.loadCreditCardsFromFile("files\\creditcardsFORTESTING.txt");
            assertNotNull(listCard);
            double total = 23;
            int index = listCard.stream().map(CreditCardModel::getCreditCardNumber).collect(Collectors.toList()).indexOf("01234567890123456");
            assert (index == -1);
            index = listCard.stream().map(CreditCardModel::getCreditCardNumber).collect(Collectors.toList()).indexOf("3012087005515173");
            CreditCardModel card = listCard.get(index);
            assertNotNull(card);
            //update the saleTransaction
            stdao.updateSaleTransaction(new SaleTransactionModel(target.getTicketNumber(), 1, total, -1, null, null, null));
            //verify all
            assertEquals(stdao.getSaleTransaction(1).getPrice(), total, 0.0);
            assertEquals(stdao.getSaleTransaction(1).getPaymentType(), 1, 0.0);
            //introduce a new balance Operation
            BalanceOperationDAO operation = new BalanceOperationDAO();
            int balanceID = operation.addBalanceOperation(new BalanceOperationModel(0, null, total, BalanceOperationModel.Type.SALE)).getBalanceId();
            assertEquals(operation.getBalanceOperation(balanceID).getMoney(), total, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        entries.clear();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }


    @Test
    public void testApplyDiscountRateToSaleDriver() {
        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("insert into sale_transaction values(null, 37.2, null, null, sysdate(), null);");

        Integer sid = saleDAO.getMaxSaleTransactionId();
        try {
            assert (saleDAO.getSaleTransaction(sid) != null);
            assert (saleDAO.updateSaleTransaction(new SaleTransactionModel(sid, null, -1, 0.3, null, null, null)));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }

    @Test
    public void testGetSaleTransactionDriver() {
        //returns SaleTransaction
        //Integer transactionId, not null and > 0

        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("insert into sale_transaction values(null, 37.2, null, null, sysdate(), null);");

        Integer sid = saleDAO.getMaxSaleTransactionId();
        SaleTransaction sale = null;
        try {
            sale = saleDAO.getSaleTransaction(sid);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
        assert(sale!=null);
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    @Test
    public void testDeleteSaleTransactionDriver() {


        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
        ProductTypeDAOInterface productDAO = new ProductTypeDAO();
        TicketEntryDAOInterface ticketDAO = new TicketEntryDAO();
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("insert into sale_transaction values(1, 0.30, null, null, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (1, 125, 2, 0.15, 0);");

        try {
           SaleTransaction sale = saleDAO.getSaleTransaction(saleDAO.getMaxSaleTransactionId());
           assert(sale!=null);
            for (TicketEntry element : sale.getEntries()) {
                ProductType product = productDAO.getProductByBarcode(element.getBarCode());
                int quantity= product.getQuantity()+element.getAmount();
                assertEquals( quantity, 12);
                productDAO.updateProductType(new ProductTypeModel(product.getId(),product.getBarCode(),null,null,quantity,null,null,null,null,null));
                ticketDAO.removeTicketEntry(element, sale.getTicketNumber());
                TicketEntry ticket = ticketDAO.getTicketEntry(product.getBarCode(),sale.getTicketNumber());
                assertNull(ticket);
            }

        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    @Test
    public void testComputePointsToSaleDriver() {


        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        dbConnection.executeUpdate("insert into sale_transaction values(null, 37.2, null, null, sysdate(), null);");

        try {
            SaleTransaction sale = saleDAO.getSaleTransaction(saleDAO.getMaxSaleTransactionId());
            assert(sale!=null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
}
