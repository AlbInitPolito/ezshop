package it.polito.ezshop.NFRTests;

import it.polito.ezshop.data.*;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class testNFR2 {

    private static EZShopInterface ezshop = new EZShop();

    @Before
    public void setUpEzShop() {
        ezshop = new EZShop();
        try {
            ezshop.createUser("testadmin", "testadmin", "Administrator");
            ezshop.login("testadmin", "testadmin");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @After
    public void clearEzShop(){
        ezshop.reset();
    }

    @Test(timeout=500)
    public void testReset() {
        ezshop.reset();
    }

    @Test(timeout=500)
    public void testCreateUser() {
        try {
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            assert (userID > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteUser() {
        try {
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            assert (ezshop.deleteUser(userID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetAllUsers() {
        try {
            ezshop.createUser("admin", "admin", "Administrator");
            assert (ezshop.getAllUsers().size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetUser() {
        try {
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            assert (ezshop.getUser(userID)!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testUpdateUserRights() {
        try {
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            assert (ezshop.updateUserRights(userID, "Cashier"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testLogin() {
        try {
            ezshop.reset();
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            assert(ezshop.login("admin", "admin")!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testLogout() {
        try {
            Integer userID = ezshop.createUser("admin", "admin", "Administrator");
            ezshop.login("admin", "admin");
            assert (ezshop.logout());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testCreateProductType() {
        try {
            assert (ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!") > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testUpdateProduct() {
        try {
            Integer prodID = ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezshop.updateProduct(prodID, "Pear", "0001110001116",
                    0.8, "Green and fresh!"));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteProduct() {
        try {
            Integer prodID1 = ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezshop.deleteProductType(prodID1));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetAllProductTypes() {
        try {
            ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezshop.getAllProductTypes().size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetProductTypeByBarCode() {
        try {
            ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert(ezshop.getProductTypeByBarCode("8008008008006")!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetProductTypesByDescription() {
        try {
            ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            assert (ezshop.getProductTypesByDescription("Pizza").size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testUpdateQuantity() {
        try {
            Integer prodID = ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezshop.updateQuantity(prodID, 100));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testUpdatePosition() {
        try {
            Integer prodID = ezshop.createProductType("Apple", "8008008008006",
                    0.1, "Fresh and green!");
            assert (ezshop.updatePosition(prodID, "1-F-5"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testIssueOrder() {
        try {
            ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            assert(ezshop.issueOrder("8008008008006", 80, 0.5)>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testPayOrderFor() {
        try {
            ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezshop.recordBalanceUpdate(5000.0);
            assert(ezshop.payOrderFor("8008008008006", 80, 0.5)>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testPayOrder() {
        try {
            ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezshop.recordBalanceUpdate(5000.0);
            Integer orderID = ezshop.issueOrder("8008008008006",  80, 0.5);
            assert (ezshop.payOrder(orderID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testRecordOrderArrival() {
        try {
            Integer prodID = ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezshop.updatePosition(prodID, "1-A-4");
            ezshop.recordBalanceUpdate(5000.0);
            Integer orderID = ezshop.issueOrder("8008008008006",
                    80, 0.5);
            ezshop.payOrder(orderID);
            assert (ezshop.recordOrderArrival(orderID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetAllOrders() {
        try {
            ezshop.createProductType("Pizza Margherita", "8008008008006",
                    0.1, "1!");
            ezshop.issueOrder("8008008008006",
                    80, 0.5);
            assert (ezshop.getAllOrders().size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDefineCustomer() {
        try {
            Integer customerID = ezshop.defineCustomer("Giorgia");
            assert (customerID > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testModifyCustomer() {
        try {
            Integer customerID = ezshop.defineCustomer("Giorgia");
            assert (ezshop.modifyCustomer(customerID, "Giorgie", null));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteCustomer() {
        try {
            Integer customerID = ezshop.defineCustomer("Giorgia");
            assert (ezshop.deleteCustomer(customerID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetCustomer() {
        try {
            Integer customerID = ezshop.defineCustomer("Giorgia");
            assert(ezshop.getCustomer(customerID)!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetAllCustomers() {
        try {
            ezshop.defineCustomer("Giorgia");
            assert (ezshop.getAllCustomers().size() == 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testCreateCard() {
        try {
            String card = ezshop.createCard();
            assert (card != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testAttachCardToCustomer() {
        try {
            Integer customerID = ezshop.defineCustomer("Giorgia");
            String card = ezshop.createCard();
            assert (ezshop.attachCardToCustomer(card, customerID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testModifyPointsOnCard() {
        try {
            ezshop.defineCustomer("Giorgia");
            String card = ezshop.createCard();
            assert (ezshop.modifyPointsOnCard(card, 500));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testStartSaleTransaction() {
        try {
            assert(ezshop.startSaleTransaction()>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testAddProductToSale() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            assert (ezshop.addProductToSale(saleID, "8008008008006", 50));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteProductFromSale() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezshop.deleteProductFromSale(saleID, "8008008008006", 50));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testApplyDiscountRateToProduct() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezshop.applyDiscountRateToProduct(saleID, "8008008008006", 0.5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testApplyDiscountRateToSale() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            assert (ezshop.applyDiscountRateToSale(saleID, 0.5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testComputePointsForSale() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            assert(ezshop.computePointsForSale(saleID)>=0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testEndSaleTransaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            assert (ezshop.endSaleTransaction(saleID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteSaleTransaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            assert (ezshop.deleteSaleTransaction(saleID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetSaleTransaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            assert(ezshop.getSaleTransaction(saleID)!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testStartReturnTrasaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            assert(ezshop.startReturnTransaction(saleID)>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testReturnProduct() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            Integer returnID = ezshop.startReturnTransaction(saleID);
            assert (ezshop.returnProduct(returnID, "8008008008006", 5));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testEndReturnTransaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            Integer returnID = ezshop.startReturnTransaction(saleID);
            ezshop.returnProduct(returnID, "8008008008006", 5);
            assert (ezshop.endReturnTransaction(returnID, true));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testDeleteReturnTransaction() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            Integer returnID = ezshop.startReturnTransaction(saleID);
            ezshop.returnProduct(returnID, "8008008008006", 5);
            ezshop.endReturnTransaction(returnID, true);
            assert (ezshop.deleteReturnTransaction(returnID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testReceiveCashPayment() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            SaleTransaction sT = ezshop.getSaleTransaction(saleID);
            assert(ezshop.receiveCashPayment(sT.getTicketNumber(), 150.0)>=0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testReceiveCreditCardPayment() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            SaleTransaction sT = ezshop.getSaleTransaction(saleID);
            assertTrue (ezshop.receiveCreditCardPayment(sT.getTicketNumber(), "4485370086510891"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testReturnCashPayment() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            SaleTransaction sT = ezshop.getSaleTransaction(saleID);
            assert (ezshop.receiveCashPayment(sT.getTicketNumber(), 100.0)>0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testReturnCreditCardPayment() {
        try {
            Integer saleID = ezshop.startSaleTransaction();
            Integer prodID = ezshop.createProductType("Apple", "8008008008006", 0.5, "NL");
            ezshop.updatePosition(prodID, "1-A-5");
            ezshop.updateQuantity(prodID, 8000);
            ezshop.addProductToSale(saleID, "8008008008006", 50);
            ezshop.endSaleTransaction(saleID);
            SaleTransaction sT = ezshop.getSaleTransaction(saleID);
            assert (ezshop.receiveCreditCardPayment(sT.getTicketNumber(), "3017786004753256"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testRecordBalanceUpdate() {
        try {
            assert (ezshop.recordBalanceUpdate(500.0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testGetCreditsAndDebits() {
        try {
            ezshop.recordBalanceUpdate(500.0);
            List<BalanceOperation> list = ezshop.getCreditsAndDebits(LocalDate.of(2020,5,1), LocalDate.now());
            assert (list.size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(timeout=500)
    public void testComputeBalance() {
        try {
            ezshop.recordBalanceUpdate(500.0);
            assert (ezshop.computeBalance() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
