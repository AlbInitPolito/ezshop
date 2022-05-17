package it.polito.ezshop.integrationTests.step9;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.DBConnection.TicketEntryDAO;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.SaleTransactionDAOInterface;
import it.polito.ezshop.data.TicketEntryDAOInterface;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.SaleTransactionModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Test;


import static org.junit.Assert.*;

public class TestSaleTransactionModel {

    SaleTransactionDAOInterface saleDAO= new SaleTransactionDAO();
    SaleTransactionModel sale = new SaleTransactionModel();
    DbConnection dbConnection = DbConnection.getInstance();
    String query0 = "DELETE FROM return_product";
    String query1 = "DELETE FROM return_transaction";
    String query2 = "DELETE FROM product_in_transaction";
    String query3 = "DELETE FROM sale_transaction";
    String query4 = "DELETE FROM product_type";
    String query5 = "DELETE FROM balance_operation";


    @Test
    public void testEndSaleTransaction () {

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
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->sale.endSaleTransaction(-1));

        try {
            //false if this.id==null && sale== null  T4(4)
            assertFalse("Error in test T4",sale.endSaleTransaction(4));

            sale.startSaleTransaction();
            sale.setPaymentType(1);
            //false if this.id != id and sale == null
            assertFalse("Error in test T5",sale.endSaleTransaction(2));
            saleDAO.addSaleTransaction(sale);
            sale.startSaleTransaction();
            //transaction is close
            assertFalse("Error in test T6",sale.endSaleTransaction(1));
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,2,0.0,productTypeModel1.getPricePerUnit()));
            assertTrue("Error in test T7",sale.endSaleTransaction(sale.getTicketNumber()));
            //verify if the transaction is in db
            assertNotNull("Error in test T8", saleDAO.getSaleTransaction(2));
            //verify if the ticket is present
            TicketEntryDAOInterface tickDAO = new TicketEntryDAO();
            assertNotNull("Error in test T8",tickDAO.getSaleTicketEntries(2));
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
    public void testReceiveCashPayment (){

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

        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,1,0.0,productTypeModel2.getPricePerUnit()));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            int max = saleDAO.getMaxSaleTransactionId();
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

            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.5,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,2,0.0,productTypeModel2.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            saleDAO.addSaleTransaction(sale);
            max= sale.getTicketNumber();

            //return -1 if the cash is not enough t8(max,1)
            assertEquals("Error T8", -1, sale.receiveCashPayment(max, 1.0), 0.0);
            //return rest if the transaction is correct done no rest t9(max,3)
            assertEquals("Error T9", 0, sale.receiveCashPayment(max, 3.0), 0.0);
            //rest of 70cent t10(max,3.70)
            assertEquals("Error T10", 0.70, Math.round(sale.receiveCashPayment(max, 3.7)*100.0)/100.0, 0.0);

        } catch (InvalidTransactionIdException | MissingDAOParameterException | InvalidDAOParameterException | InvalidPaymentException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    // ha all'interno saleTransaction (getTransaction, updateTransaction) creditcard (loadcreditcard,luhn, haveenoughmoney) e balanceDAO (addBalance)
    @Test
    public void testRceiveCreditCardPayment (){
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
        try {

            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,1,0.0,productTypeModel2.getPricePerUnit()));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            int max = saleDAO.getMaxSaleTransactionId();
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
            assertFalse("Error! in test T7", sale.receiveCreditCardPayment(max+1,"8185062428408512"));
            sale.startSaleTransaction();
            //sale exist but is open t8(sale.getticket,8185062428408512)
            assertFalse("Error in test T8", sale.receiveCreditCardPayment(sale.getTicketNumber(),"8185062428408512"));
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.5,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,2,0.0,productTypeModel2.getPricePerUnit()));
            sale.getEntries().forEach(element -> sale.setPrice(sale.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate())))));
            saleDAO.addSaleTransaction(sale);
            max= sale.getTicketNumber();
            sale= new SaleTransactionModel();
            //false if the credit card does not exist in the file t9(max,4485370086510871)
            assertFalse("Error in test T9", sale.receiveCreditCardPayment(max,"7045888851714816"));
            //false if the credit card does not have enough money t10(max,4716258050958645)
            assertFalse("Error in test T10", sale.receiveCreditCardPayment(max,"4716258050958645"));
            //true se l'operazione va a buon fine T11() max,5100293991053009)
            assertTrue("Error in test T11", sale.receiveCreditCardPayment(max,"4485370086510891"));

        } catch (InvalidTransactionIdException | MissingDAOParameterException | InvalidDAOParameterException | InvalidCreditCardException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    // saleTransactionDAO (getSaleTransaction)
    @Test
    public void testComputePointForSale () {
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
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,1,0.0,productTypeModel2.getPricePerUnit()));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            int max = saleDAO.getMaxSaleTransactionId();
            //false if the ticketid==null&&sale==null t4(max+1)
            assertEquals("Error in test T4", -1, sale.computePointsForSale(max + 1));
            sale.startSaleTransaction(); //t5 not the same transaction
            assertEquals("Error in test T5", -1, sale.computePointsForSale(sale.getTicketNumber()+1));
            //t6 transaction open
            assertEquals("Error in test T6", 0, sale.computePointsForSale(sale.getTicketNumber()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,4,0.0,productTypeModel1.getPricePerUnit()));
            int index= sale.getTicketNumber();
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //transaction close
            assertEquals("Error in test T7", 0, sale.computePointsForSale(index));


        } catch (InvalidTransactionIdException | InvalidDAOParameterException | MissingDAOParameterException e) {
            e.printStackTrace();
        }
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    // saleTransactionDAO (getSaleTransaction)
    @Test
    public void testApplyDiscountToSale () {
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
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,1,0.0,productTypeModel2.getPricePerUnit()));
            sale.setPaymentType(1);//sale 1 already payed
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //false sale==null this.ticket ==null t6(3,0.5)
            assertFalse("Error in test T6", sale.applyDiscountRateToSale(3,0.5));
            sale.startSaleTransaction(); //t5 not the same transaction
            //false this.ticket!= transactionid sale==null t7(6,0.6)
            assertFalse("Error in test T7", sale.applyDiscountRateToSale(6,0.6));
            //false transaction already payed t8(1,0.4)
            assertFalse("Error in test T8", sale.applyDiscountRateToSale(1,0.4));
            //true transaction open t9(2,0.3)
            assertTrue("Error in test T9", sale.applyDiscountRateToSale(sale.getTicketNumber(),0.3));
            //verify
            assertEquals("Error in test T10",0.3,sale.getDiscountRate(),0.0);
            //true transaction close t11(1,0.2)
            assertTrue("Error in test T11", sale.applyDiscountRateToSale(2,0.2));
            //verify
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //sale with id = 2 is never saved into db as "endsaletransaction" is never used for it
            assertEquals("Error in test T12",0.2,saleDAO.getSaleTransaction(2).getDiscountRate(),0.0);
        } catch (InvalidTransactionIdException | InvalidDAOParameterException | MissingDAOParameterException | InvalidDiscountRateException e) {
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
    public void testDeleteSaleTransaction () {
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
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteSaleTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteSaleTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->sale.deleteSaleTransaction(-1));

        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            //transaction not exist t4(3)
            assertFalse("Error in test T4",sale.deleteSaleTransaction(3));
            sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            sale.setPaymentType(1);//sale 1 already payed

            //transaction not exist t5(4)
            assertFalse("Error in test T5",sale.deleteSaleTransaction(4));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //transaction already payed
            assertFalse("Error in test T6",sale.deleteSaleTransaction(saleDAO.getMaxSaleTransactionId()));
            sale.startSaleTransaction();
            //transaction open
            assertTrue("Error in test T7",sale.deleteSaleTransaction(2));
            sale.startSaleTransaction();
            TicketEntryModel ticket = new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit());
            sale.getEntries().add(ticket);
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            //transaction close
            TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
            ticketEntryDAO.addTicketEntry(ticket, saleDAO.getMaxSaleTransactionId());
            assertTrue("Error in test T8",sale.deleteSaleTransaction(2));
            //verify
            assertNull("Error in test T9", saleDAO.getSaleTransaction(2));
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
    public void testGetSaleTransaction (){
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
        //transactionID null t1(null)
        assertThrows(InvalidTransactionIdException.class,()->sale.getSaleTransaction(null));
        //transactionID 0 t2(0)
        assertThrows(InvalidTransactionIdException.class,()->sale.getSaleTransaction(0));
        //transaction id <0 t3(-1)
        assertThrows(InvalidTransactionIdException.class,()->sale.getSaleTransaction(-1));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            productTypeDAO.addProductType(productTypeModel2);
            SaleTransaction saleNull=sale.getSaleTransaction(4);
            assertNull("Error with test 4",saleNull);
            int index= sale.startSaleTransaction();
            sale.getEntries().add(new TicketEntryModel(productTypeModel2.getBarCode(),null,4,0.0,productTypeModel2.getPricePerUnit()));
            saleDAO.addSaleTransaction(sale);
            sale= new SaleTransactionModel();
            int indexSale= sale.getSaleTransaction(index).getTicketNumber();
            assertEquals("Error with test T5",index,indexSale);
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

}
