package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.DBConnection.SaleTransactionDAO;
import it.polito.ezshop.data.SaleTransactionDAOInterface;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeModel;
import it.polito.ezshop.model.SaleTransactionModel;
import it.polito.ezshop.model.TicketEntryModel;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class TestStartSaleTransactionModel {
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
    public void startSaleTransaction (){

        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        assertNotNull("Error in test T1", sale.startSaleTransaction());
        assertFalse("Error in test T2",sale.startSaleTransaction() <=0);
        int max= saleDAO.getMaxSaleTransactionId();
        assertTrue("Error in test T3", sale.startSaleTransaction()>max);
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    //productDAO (update,getproduct)
    @Test
    public void testAddProductToSaleTransaction() {

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
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);

    }
    @Test
    public void testDeleteProductFromSale (){

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
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,3,0.0,productTypeModel1.getPricePerUnit()));
            saleDAO.addSaleTransaction(sale);
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
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
    }
    @Test
    public void testApplyDiscountRateToProduct () {
        dbConnection.executeUpdate(query0);
        dbConnection.executeUpdate(query1);
        dbConnection.executeUpdate(query2);
        dbConnection.executeUpdate(query3);
        dbConnection.executeUpdate(query4);
        dbConnection.executeUpdate(query5);
        ProductTypeModel productTypeModel1 = new ProductTypeModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        productTypeModel1.setBarCode("8008008008006");
        productTypeModel1.setPricePerUnit(1.0);
        productTypeModel1.setQuantity(80);
        productTypeModel1.setLocation("1-A-4");
        productTypeModel1.setNote("Notes");
        productTypeModel1.setProductDescription("Franci");
        //transactionID null t1(null,123456789128,1)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToProduct(null,"123456789128",1));
        //transactionID 0 t2(0,123456789128,0)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToProduct(0,"123456789128",0));
        //transaction id <0 t3(-1,123456789128,7)
        assertThrows(InvalidTransactionIdException.class,()->sale.applyDiscountRateToProduct(-1,"123456789128",7));
        //product code null t4(3,null,4)
        assertThrows(InvalidProductCodeException.class,()->sale.applyDiscountRateToProduct(4,null,4));
        //productcode ""t5(4,"",2)
        assertThrows(InvalidProductCodeException.class,()->sale.applyDiscountRateToProduct(4,"",2));
        //product code not valid t6(1,023456789128,8)
        assertThrows(InvalidProductCodeException.class,()->sale.applyDiscountRateToProduct(1,"023456789128",8));
        //discount <0 t7(2,"0001110001116",-1)
        assertThrows(InvalidDiscountRateException.class,()->sale.applyDiscountRateToProduct(2,"0001110001116",-1));
        //discount>1 t8(1,"0001110001116",1.2)
        assertThrows(InvalidDiscountRateException.class,()->sale.applyDiscountRateToProduct(1,"0001110001116",1.2));
        try {
            productTypeDAO.addProductType(productTypeModel1);
            //false transaction null
            Assert.assertFalse("Error in test T9",sale.applyDiscountRateToProduct(4,"8008008008006",0.2));
            sale.startSaleTransaction();
            //false transaction not open
            Assert.assertFalse("Error in test T10",sale.applyDiscountRateToProduct(3,"8008008008006",0.1));
            //false product not exist
            Assert.assertFalse("Error in test T11",sale.applyDiscountRateToProduct(1,"0001110001116",0.2));
            sale.getEntries().add(new TicketEntryModel(productTypeModel1.getBarCode(),null,2,0.0,productTypeModel1.getPricePerUnit()));

            //false product not present in transaction
            Assert.assertFalse("Error in test T12",sale.applyDiscountRateToProduct(1,"0001110001116",0.3));
            //true product add
            assertTrue("Error in test T13",sale.applyDiscountRateToProduct(1,"8008008008006",0.3));
            //verify
            assertEquals("Error in test T14", 0.3, sale.getEntries().get(0).getDiscountRate(), 0.0);

        } catch (MissingDAOParameterException | InvalidDAOParameterException | InvalidTransactionIdException | InvalidProductCodeException | InvalidDiscountRateException e) {
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
