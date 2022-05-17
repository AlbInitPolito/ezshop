package it.polito.ezshop.integrationTests.step9;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class testReturnTransactionModel {
    ReturnTransactionModel saleR = new ReturnTransactionModel();
    SaleTransactionModel sale = new SaleTransactionModel();
    SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
    DbConnection dbConnection = DbConnection.getInstance();
    ReturnTransactionDAOInterface returnDAO= new ReturnTransactionDAO();
    String query0 = "DELETE FROM return_product";
    String query1 = "DELETE FROM return_transaction";
    String query2 = "DELETE FROM product_in_transaction";
    String query3 = "DELETE FROM sale_transaction";
    String query6 = "DELETE FROM product_entry";
    String query4 = "DELETE FROM product_type";
    String query5 = "DELETE FROM balance_operation";

    @Test
    public void testStartReturnTransaction() {
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
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class, () -> saleR.startReturnTransaction(-1));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,3,0.0,productTypeModel2.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //-1 if transaction does not exist
            assertEquals("Error in test T4", -1, (int) saleR.startReturnTransaction(3));
            int result = saleR.startReturnTransaction(1);
            //result is not <0
            assertNotEquals("Error in test T5", -1, result);
            //result is not =0
            assertNotEquals("Error in test T6", 0, result);
            assertTrue("Error in test 7", result > 0);

        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidTransactionIdException e) {
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
    public void testReturnProduct(){
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
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
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            TicketEntryDAOInterface ticketDAO= new TicketEntryDAO();

            saleDAO.addSaleTransaction(sale);
            sale.getEntries().forEach(element -> {
                try {
                    ticketDAO.addTicketEntry(element,sale.getTicketNumber());
                } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                    e.printStackTrace();
                }
            });
            //false if return transaction does not exist
            assertFalse("Error in test T9", saleR.returnProduct(1,"0001110001116",4));
            saleR.setID(1);
            saleR.setTransactionID(3);
            //false if id not correspond
            assertFalse("Error in test T10", saleR.returnProduct(2,"0001110001116",4));
            //false if the product does not exist
            assertFalse("Error in test T11", saleR.returnProduct(1,"123456789166",4));
            //false if the transaction does not exist
            assertFalse("Error in test T12", saleR.returnProduct(1,"0001110001116",4));
            saleR.setTransactionID(1);
            //false the sale not contain the product
            assertFalse("Error in test T13", saleR.returnProduct(1,"0001110001116",4));
            //false the quantity is too much
            assertFalse("Error in test T14", saleR.returnProduct(1,"8008008008006",5));
            assertTrue("Error in test T15", saleR.returnProduct(1,"8008008008006",3));
            assertFalse("Error in test T16",saleR.getEntries().isEmpty());
            assertEquals("Error in test T17","8008008008006", saleR.getEntries().get(0).getBarCode());
        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidQuantityException | InvalidTransactionIdException | InvalidProductCodeException e) {
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
    public void testEndReturnTransaction(){
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
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(202, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(203, 0.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (201, 123, 3, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (202, 124, 3, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (203, 124, 1, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (201, '000000000002')");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (202, '000000000003')");
        //returnID null t1(null,"0001110001116",3)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(null,false));
        //returnID 0 t2(0,"0001110001116",2)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(0,false));
        //returnID <0 t3(-1,"0001110001116",76)
        assertThrows(InvalidTransactionIdException.class,()->saleR.endReturnTransaction(-1,false));
        try {
            //false if return transaction does not exist
            assertFalse("Error in test T4", saleR.endReturnTransaction(9,true));
            saleR.setID(1);
            saleR.setTransactionID(201);
            //false if id not correspond
            assertFalse("Error in test T5", saleR.endReturnTransaction(3,false));
            //reset of the return transaction
            assertTrue("Error in test T6", saleR.endReturnTransaction(1,false));
            assertNull("Error in test T7", saleR.getID());
            //save in the db
            saleR.setID(1);
            saleR.setTransactionID(203);
            //if there are no element RFID
            assertTrue("Error in test T8", saleR.endReturnTransaction(1,true));
            assertNull("Error in test T9", saleR.getID());
            assertNotNull("Error in test T10", returnDAO.getReturnTransaction(1));
            ReturnProductDAOInterface returnPDAO= new ReturnProductDAO();
            assertNotNull(returnPDAO.getReturnProducts(1));
            //if there is element in RFID
            saleR= new ReturnTransactionModel();
            saleR.setID(2);
            saleR.setTransactionID(201);
            TicketEntryModel tickRFID = new TicketEntryModel();
            tickRFID.setBarCode("123456789128");
            tickRFID.setAmount(0);
            tickRFID.addRFID("000000000002");
            saleR.getEntries().add(tickRFID);
            assertTrue ("Error in test T11",saleR.endReturnTransaction(2,true));
            assertNull("Error in test T12", saleR.getID());
            assertNotNull("Error in test T13", returnDAO.getReturnTransaction(2));
            ReturnProductRfidDAOInterface returnPDAORFID= new ReturnProductRfidDAO();
            assertNotNull(returnPDAORFID.getReturnProductRfids(2));

        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidTransactionIdException e) {
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
    public void testDeleteReturnTransaction(){
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setId(1);
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setId(2);
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //returnID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(null));
        //returnID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(0));
        //returnID <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->saleR.deleteReturnTransaction(-1));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            int sid = sale.startSaleTransaction();
            TicketEntryModel t = new TicketEntryModel("8008008008006",null,3,0.0,productTypeModel1.getPricePerUnit());
            sale.getEntries().add(t);
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            productTypeDAO.updateProductType(new ProductTypeModel(productTypeModel1.getId(),productTypeModel1.getBarCode(),null,null,productTypeModel1.getQuantity()-4,null,null,null,null,null));
            saleDAO.addSaleTransaction(sale);
            TicketEntryDAO tdao = new TicketEntryDAO();
            tdao.addTicketEntry(t, sid);
            //false if return transaction does not exist
            assertFalse("Error in test T4", saleR.deleteReturnTransaction(3));
            saleR.setID(1);
            //false if id not correspond
            assertFalse("Error in test T5",  saleR.deleteReturnTransaction(2));
            //false if transaction still open
            assertFalse("Error in test T6",  saleR.deleteReturnTransaction(1));
            t.setAmount(2);
            saleR.getEntries().add(t);
            ReturnTransactionDAOInterface retDAO=new ReturnTransactionDAO();
            saleR.setTransactionID(saleDAO.getMaxSaleTransactionId());
            ReturnProductDAO rdao = new ReturnProductDAO();
            retDAO.addReturnTransaction(saleR);
            rdao.addReturnProduct(t, 1);
            saleR=new ReturnTransactionModel();
            assertTrue("Error in test T7",  saleR.deleteReturnTransaction(1));
            saleR.setID(1);
            t.setAmount(2);
            saleR.getEntries().add(t);
            saleR.setTransactionID(saleDAO.getMaxSaleTransactionId());
            rdao = new ReturnProductDAO();
            retDAO.addReturnTransaction(saleR);
            rdao.addReturnProduct(t, 1);
            BalanceOperationDAOInterface balanceDAO = new BalanceOperationDAO();
            balanceDAO.addBalanceOperation(new BalanceOperationModel(3,null,3,null));
            returnDAO.setReturnBalanceOperation(saleR.getID(),3);
            saleR=new ReturnTransactionModel();
            //false if the transaction is already payed
            assertFalse("Error in test T6",  saleR.deleteReturnTransaction(returnDAO.getMaxReturnTransactionId()));
            //verify that in ticket there is no returnticket
            ReturnProductDAOInterface returnPDAO= new ReturnProductDAO();
            assert( returnPDAO.getReturnProducts(1).size()==0);
            //return not present in return_transaction
            assertNull("Error in test T9", returnDAO.getReturnTransaction(1));

            dbConnection.executeUpdate(query0);
            dbConnection.executeUpdate(query1);
            dbConnection.executeUpdate(query2);
            dbConnection.executeUpdate(query3);
            dbConnection.executeUpdate(query4);
            dbConnection.executeUpdate(query5);

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

            ReturnTransactionModel returnTransaction = new ReturnTransactionModel();
            returnTransaction.deleteReturnTransaction(1);
            assertNull(returnDAO.getReturnTransaction(1));
            assert(productTypeDAO.getProductByBarcode("8008008008006").getQuantity() == 79);
            ProductEntryDAO productEntryDAO = new ProductEntryDAO();
            assert(productEntryDAO.getProductEntryAvailability("000000000002") == 0);
            assert(saleDAO.getSaleTransaction(201).getPrice() == 1.50);
        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidTransactionIdException e) {
            e.printStackTrace();
        }
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
    }
    @Test
    public void testReturnCashPayment(){
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setId(1);
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setId(2);
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //returnID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCashPayment(null));
        //returnID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCashPayment(0));
        //returnID <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCashPayment(-1));
        //-1 transaction still open
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            saleDAO.addSaleTransaction(sale);
            productTypeDAO.updateProductType(new ProductTypeModel(productTypeModel1.getId(),productTypeModel1.getBarCode(),null,null,productTypeModel1.getQuantity()-4,null,null,null,null,null));
            saleR.setID(1);
            saleR.setTransactionID(saleDAO.getMaxSaleTransactionId());
            assertEquals("Error in test T4", -1, saleR.returnCashPayment(1), 0.0);
            //transaction not exist
            assertEquals("Error in test T5", -1, saleR.returnCashPayment(5), 0.0);
            saleR.getEntries().add(new TicketEntryModel("8008008008006",null,2,0.0,productTypeModel1.getPricePerUnit()));
            saleR.getEntries().forEach(element-> saleR.setReturnedValue(saleR.getReturnedValue()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            ReturnTransactionDAOInterface retDAO=new ReturnTransactionDAO();
            retDAO.addReturnTransaction(saleR);
            productTypeModel1=(ProductTypeModel) productTypeDAO.getProductByBarcode(productTypeModel1.getBarCode());
            productTypeDAO.updateProductType(new ProductTypeModel(productTypeModel1.getId(),productTypeModel1.getBarCode(),null,null,productTypeModel1.getQuantity()+2,null,null,null,null,null));
            saleR=new ReturnTransactionModel();
            //return cash
            assertEquals("Error in test T6", 2, saleR.returnCashPayment(1), 0.0);
         } catch (InvalidTransactionIdException | MissingDAOParameterException | InvalidDAOParameterException e) {
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
    public void testReturnCreditCardPayment(){
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeModel productTypeModel2 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setId(1);
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");

        productTypeModel2.setBarCode("0001110001116");
        productTypeModel2.setId(2);
        productTypeModel2.setPricePerUnit(0.5);
        productTypeModel2.setQuantity(70);
        productTypeModel2.setLocation("2-B-3");
        productTypeModel2.setNote("Notes2");
        productTypeModel2.setProductDescription("Matte");
        //credit card empty t1(null,"")
        assertThrows(InvalidCreditCardException.class,()->saleR.returnCreditCardPayment(null,""));
        //credit card null t2(null,null)
        assertThrows(InvalidCreditCardException.class,()->saleR.returnCreditCardPayment(null,null));
        //credit card not valid t3(null,0185062428408512)
        assertThrows(InvalidCreditCardException.class,()->saleR.returnCreditCardPayment(null,"0185062428408512"));
        //ID null t4(null,8185062428408512)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCreditCardPayment(null,"8185062428408512"));
        //ID 0 t5(0,8185062428408512)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCreditCardPayment(0,"8185062428408512"));
        //id <0 t6(-1,8185062428408512)
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnCreditCardPayment(-1,"8185062428408512"));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            saleDAO.addSaleTransaction(sale);
            productTypeDAO.updateProductType(new ProductTypeModel(productTypeModel1.getId(),productTypeModel1.getBarCode(),null,null,productTypeModel1.getQuantity()-3,null,null,null,null,null));
            TicketEntryDAOInterface ticketDAO = new TicketEntryDAO();
            sale.getEntries().forEach(element -> {
                try {
                    ticketDAO.addTicketEntry(element,sale.getTicketNumber());
                } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                    e.printStackTrace();
                }
            });
            saleR.setID(1);
            //-1 if transaction still open
            assertEquals("Error in test T7",-1,  saleR.returnCreditCardPayment(1,"8185062428408512"),0.0);
            saleR.getEntries().add(new TicketEntryModel("8008008008006",null,1,0.0,productTypeModel1.getPricePerUnit()));
            ReturnTransactionDAOInterface retDAO=new ReturnTransactionDAO();
            saleR.setTransactionID(saleDAO.getMaxSaleTransactionId());
            saleR.getEntries().forEach(element-> saleR.setReturnedValue(saleR.getReturnedValue()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            retDAO.addReturnTransaction(saleR);
            ReturnProductDAO retpDAO = new ReturnProductDAO();
            saleR.getEntries().forEach(element-> {
                try {
                    retpDAO.addReturnProduct(element,saleR.getID());
                } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                    e.printStackTrace();
                }
            });
            productTypeModel1=(ProductTypeModel) productTypeDAO.getProductByBarcode(productTypeModel1.getBarCode());
            productTypeDAO.updateProductType(new ProductTypeModel(productTypeModel1.getId(),productTypeModel1.getBarCode(),null,null,productTypeModel1.getQuantity()+1,null,null,null,null,null));

            saleR=new ReturnTransactionModel();
            int saleID = returnDAO.getMaxReturnTransactionId();
            //-1 if credit card not registered
            assertEquals("Error in test T8",-1,  saleR.returnCreditCardPayment(1,"2474813718374217"),0.0);
            //-1 if return transaction does not exist
            assertEquals("Error in test T9",-1,  saleR.returnCreditCardPayment(saleDAO.getMaxSaleTransactionId()+1, "5100293991053009"),0.0);
            //-1 credit card has not enough money
            assertEquals("Error in test T10",-1,  saleR.returnCreditCardPayment(saleID,"4716258050958645"),0.0);
            //return 1
            assertEquals("Error in test T11",1,  saleR.returnCreditCardPayment(saleID, "4485370086510891"),0.0);

        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidTransactionIdException | InvalidCreditCardException e) {
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
    public void testReturnProductRFID(){
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
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (123, '123456789128', 'Apple', 0.50, 10, 'Fresh and juicy!', 7, 'a', 5)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (124, '123456789135', 'Pear', 0.30, 10, 'Good for you!', 8, 's', 6)");
        dbConnection.executeUpdate("INSERT INTO product_type VALUES (125, '123456789142', 'Banana', 0.15, 10, 'Dont slip on it!', 1, 'b', 9)");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(201, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO sale_transaction VALUES(202, 1.50, 1, 0.0, sysdate(), null);");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (201, 123, 3, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction VALUES (202, 124, 3, 0.0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000001', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000002', 123, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000003', 124, 0)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000004', 124, 1)");
        dbConnection.executeUpdate("INSERT INTO product_entry VALUES('000000000005', 125, 1)");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (201, '000000000002')");
        dbConnection.executeUpdate("INSERT INTO product_in_transaction_rfid VALUES (202, '000000000003')");
        //returnID null t1(null,"0001110001116")
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProductRFID(null,"0001110001116"));
        //returnID 0 t2(0,"0001110001116")
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProductRFID(0,"0001110001116"));
        //returnID <0 t3(-1,"0001110001116")
        assertThrows(InvalidTransactionIdException.class,()->saleR.returnProductRFID(-1,"0001110001116"));
        //product code null t4(3,null)
        assertThrows(InvalidRFIDException.class,()->saleR.returnProductRFID(3,null));
        //product code ""t5(2,"")
        assertThrows(InvalidRFIDException.class,()->saleR.returnProductRFID(3,""));
        //product code invalid t6(2,"12")
        assertThrows(InvalidRFIDException.class,()->saleR.returnProductRFID(3,"12"));
        //product code invalid t7(2,"23A342123287")
        assertThrows(InvalidRFIDException.class,()->saleR.returnProductRFID(3,"23A342123287"));

        try {
            saleR.setID(1);
            //false if return transaction does not exist
            assertFalse("Error in test T8", saleR.returnProductRFID(1,"000000000005"));
            //false if the product does not exist
            assertFalse("Error in test T9", saleR.returnProductRFID(1,"000000000006"));
            //false if the product is present in the shop
            assertFalse("Error in test T10", saleR.returnProductRFID(1,"000000000005"));

            saleR.setTransactionID(3);
            //false if id of the sale not correspond
            assertFalse("Error in test T11", saleR.returnProductRFID(1,"000000000002"));
            saleR.setTransactionID(202);
            //false the sale not contain the product
            assertFalse("Error in test T12", saleR.returnProductRFID(1,"000000000002"));
            //false if the sale not contain the product with the RFID
            assertFalse("Error in test T13", saleR.returnProductRFID(1,"000000000001"));
            assertTrue("Error in test T14",saleR.getEntries().isEmpty());
            assertTrue("Error in test T15",saleR.returnProductRFID(1,"000000000003"));
            assertEquals("Error in test T16","123456789135", saleR.getEntries().get(0).getBarCode());
        } catch (InvalidTransactionIdException | InvalidRFIDException e) {
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