package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.data.*;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class Testezshop20210614 {
    private static EZShopInterface ezshop;
    private int adminId;
    private String adminBaseUsername = "ADMIN";
    private String adminBasePwd = "ADMIN_PWD";


    private String username1 = "TestUsr1";
    private String username2 = "TestUsr2";
    private String username3 = "TestUsr3";
    private String userPwd = "TestUsrPwd";
    private String cashier = "Cashier";
    private String shopManager = "ShopManager";
    private String admin = "Administrator";

    private String productDescr1 = "testProduct1";
    private String productDescr2 = "testProduct2";
    private String barCode = "000012354788";
    private String barCode2 = "000055555555";
    private String invalidBarCode = "12354780";
    private double pricePerUnit = 0.50;
    private double pricePerUnit2 = 1.50;
    private double orderPricePerUnit = 0.25;
    private double orderPricePerUnit2 = 1.00;

    private String note1 = "test description";
    private String note2 = "description product";
    private String note3 = "type type";
    private String emptyNote = "";

    private int quantity = 10;
    private int quantity2 = 1;

    private String location1 = "10-A-1";
    private String location2 = "1-Z-10";
    private String invalidLocation1 = "A-A-0";
    private String invalidLocation2 = "0-A-A";
    private String invalidLocation3 = "A-A-A";

    private String customerName1 = "testCustomerName 1";
    private String customerName2 = "testCustomerName 2";
    private String customerCard1 = "1000011110";
    private String customerCard2 = "1000011111";
    private String invalidCustomerCard = "100001111a";
    private int point1 = 0;
    private int point2 = 100;

    private double discountRate = 0.5;
    private double invalidDiscountRate = 1.01;

    private double cash = 5.00;
    private String creditCard150 = "4485370086510891";
    private String creditCard10 = "5100293991053009";
    private String creditCard0 = "4716258050958645";
    private String notRegisteredCreditCard = "4485232344883462";
    private String invalidCreditCard = "4485370086510898";

    private final String BALANCE_ORDER = "ORDER";
    private final String BALANCE_SALE = "SALE";
    private final String BALANCE_RETURN = "RETURN";
    private final String BALANCE_CREDIT = "CREDIT";
    private final String BALANCE_DEBIT = "DEBIT";

    private final String ORDER_ISSUED = "ISSUED";
    private final String ORDER_PAYED = "PAYED";
    private final String ORDER_COMPLETED = "COMPLETED";

    private String RFIDfrom1 = "000000001000";
    private String RFIDfromInvalid = "abc";
    private String RFIDfromInvalid2 = "0000000010000000";
    private String RFID1 = "000000001001";
    private String RFIDDiff = "000001001002";
    private String RFID2 = "000000001002";

    @BeforeClass
    public static void setUpEzShop() {
        ezshop = new EZShop();
    }

    @AfterClass
    public static void clearEzShop(){
        ezshop.reset();
    }

    private String getErrorMsg(String testName, String msg) {
        return "Error in test " + testName + ": " + msg;
    }

    private boolean isValidCreditCard(String creditCard){
        try {
            Pattern pattern = Pattern.compile("[0-9]+");
            if (!pattern.matcher(creditCard).matches())
                return false;
            int last = 0;
            //pad with zeros to reach 14 digits
            switch (creditCard.length()) {
                case 13:
                case 14:
                case 15:
                case 16:
                case 19:
                    last = creditCard.charAt(creditCard.length()-1);
                    break;
                default:
                    //invalid
                    return false;
            }
            int sum = 0;
            int val = 0;
            int size = creditCard.length()-1;
            for (int i = creditCard.length()-2; i >= 0; i--) {
                if ((size-i) % 2 == 0) {
                    sum += Character.getNumericValue(creditCard.charAt(i));
                } else {
                    val = Character.getNumericValue(creditCard.charAt(i));
                    val = val * 2;
                    if(val > 9)
                        val -= 9;
                    sum += val;
                }
            }
            return last == (sum % 10);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int computePointsFromPrice(double price){
        return (int) (price/10);
    }

    private boolean isValidCustomerCard(String card) {
        Pattern pattern = Pattern.compile("[\\d]{10}");
        return pattern.matcher(card).matches();
    }

    private boolean isBarCodeValid(String code) {
        try {
            Pattern pattern = Pattern.compile("[0-9]+");
            if (!pattern.matcher(code).matches())
                return false;
            //pad with zeros to reach 14 digits
            switch (code.length()) {
                case 8:
                    code = "000000" + code;
                    break;
                case 12:
                    code = "00" + code;
                    break;
                case 13:
                    code = "0" + code;
                    break;
                case 14:
                    break;
                default:
                    //invalid
                    return false;
            }
            int sum = 0;
            for (int i = 0; i < 13; i++) {
                if ((i+1) % 2 == 0) {
                    sum += (Character.getNumericValue(code.charAt(i)) * 3);
                } else {
                    sum += Character.getNumericValue(code.charAt(i));
                }
            }
            return Character.getNumericValue(code.charAt(13)) == ((10 - (sum % 10)) % 10);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isValidPosition(String newPos) {
        if(newPos == null || newPos.isEmpty())
            return false;
        Pattern pattern = Pattern.compile("[a-z,A-Z]+-[\\d]+");
        return pattern.matcher(newPos).matches();
    }

    @Before
    public void setup() {
        ezshop.reset();
        try {
            adminId = ezshop.createUser(adminBaseUsername,adminBasePwd,admin);
            ezshop.login(adminBaseUsername,adminBasePwd);
        } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRecordOrderArrivalRFID(){
        try{
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            ezshop.updatePosition(id, location1);
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            Assert.assertTrue(getErrorMsg("testRecordOrderArrivalRFID", "The operation should not fail"),ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNotLogged","Product code should not be considered invalid"));
        }
        catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The order id should not be considered as invalid"));
        }
        catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The logged user should be authorized"));
        }
        catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The product location should not be considered as invalid"));
        }
        catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The RFIDfrom should not be considered as invalid"));
        }
        catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The product description should not be considered as invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The product price should not be considered as invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The product quantity should not be considered as invalid"));
        }
        catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFID", "The product id should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidOrderIdException.class)
    public void testRecordOrderArrivalRFIDNegative() throws InvalidOrderIdException {
        try {
            ezshop.recordOrderArrivalRFID(-1, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNegative", "The order id should be considered as invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNegative", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNegative", "The product location should not be considered as invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNegative","RFID should not be considered invalid"));
        }
    }

    @Test(expected = InvalidOrderIdException.class)
    public void testRecordOrderArrivalRFIDZero() throws InvalidOrderIdException {
        try {
            ezshop.recordOrderArrivalRFID(0, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDZero", "The order id should be considered as invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDZero", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDZero", "The product location should not be considered as invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDZero","RFID should not be considered invalid"));
        }
    }

    @Test(expected = InvalidOrderIdException.class)
    public void testRecordOrderArrivalRFIDNull() throws InvalidOrderIdException {
        try {
            ezshop.recordOrderArrivalRFID(null, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNull", "The order id should be considered as invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNull", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNull", "The product location should not be considered as invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNull","RFID should not be considered invalid"));
        }
    }

    @Test(expected = InvalidLocationException.class)
    public void testRecordOrderArrivalRFIDNoLoc() throws InvalidLocationException {
        try{
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc","RFID should not be considered invalid"));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The order id should not be considered as invalid"));
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The product type should not be considered as invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The product price should not be considered as invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The product quantity should not be considered as invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The product code should not be considered as invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDNoLoc", "The logged user should be authorized"));
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void testRecordOrderRFIDNotLogged() throws UnauthorizedException {
        try{
            ezshop.logout();
            ezshop.recordOrderArrivalRFID(100, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotLogged", "The operation should have failed"));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotLogged", "The order id should not be considered as invalid"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotLogged", "The product location should not be considered as invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotLogged","RFID should not be considered invalid"));
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void testRecordOrderRFIDNotAuthorized() throws UnauthorizedException {
        try{
            ezshop.logout();
            ezshop.createUser(username1,userPwd,cashier);
            ezshop.login(username1,userPwd);
            ezshop.recordOrderArrivalRFID(100, RFIDfrom1);
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The operation should have failed"));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The order id should not be considered as invalid"));
        } catch (InvalidRoleException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The role should not be considered as invalid"));
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The password should not be considered as invalid"));
        } catch (InvalidUsernameException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The username should not be considered as invalid"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized", "The product location should not be considered as invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderRFIDNotAuthorized","RFID should not be considered invalid"));
        }
    }

    @Test (expected = InvalidRFIDException.class)
    public void testRecordOrderArrivalRFIDInvalidRFID() throws InvalidRFIDException{
        try{
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            ezshop.updatePosition(id, location1);
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfromInvalid);
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The operation should have failed."));    
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID","Product code should not be considered invalid"));
        } 
        catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The order id should not be considered as invalid"));
        }
        catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The logged user should be authorized"));
        }
        catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The product location should not be considered as invalid"));
        }
        catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The product description should not be considered as invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The product price should not be considered as invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The product quantity should not be considered as invalid"));
        }
        catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDInvalidRFID", "The product id should not be considered as invalid"));
        }
    }

    @Test
    public void testRecordOrderArrivalRFIDOrderIDNotExisting(){
        try{ 
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            ezshop.updatePosition(id, location1);
            Assert.assertFalse(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The operation should fail."), ezshop.recordOrderArrivalRFID(100, RFIDfrom1));    
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting","RFID should not be considered invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting","Product code should not be considered invalid"));
        }
        catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The order id should not be considered as invalid"));
        }
        catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The logged user should be authorized"));
        }
        catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The product location should not be considered as invalid"));
        }
        catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The product description should not be considered as invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The product price should not be considered as invalid"));
        } 
        catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testRecordOrderArrivalRFIDOrderIDNotExisting", "The product id should not be considered as invalid"));
        }
    }

    @Test
    public void testAddProductToSaleRFID() {
        try {
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int prodId = ezshop.createProductType(productDescr1,barCode,pricePerUnit, note1);
            ezshop.updatePosition(prodId,location1);
            ezshop.updateQuantity(prodId,0);          
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1);            
            int transactionId = ezshop.startSaleTransaction();
            Assert.assertTrue(getErrorMsg("testAddProductToSaleRFID", "This operation should not fail"), ezshop.addProductToSaleRFID(transactionId,RFID1));
            Assert.assertFalse(getErrorMsg("testAddProductToSaleRFID", "This operation should fail"), ezshop.addProductToSaleRFID(transactionId,RFIDDiff));
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product description should not be considered invalid"));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID", "The order id should not be considered as invalid"));
        } 
        catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product price should not be considered invalid"));
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product id should not be considered invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product amount should not be considered invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product code should not be considered invalid"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","Product location should not be considered invalid"));
        }
        catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFID","RFID should not be considered invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testAddProductToSaleRFIDNegativeTId() throws InvalidTransactionIdException {
        try {
            ezshop.addProductToSaleRFID(-1, RFID1);
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNegativeTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNegativeTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNegativeTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNegativeTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testAddProductToSaleRFIDZeroTId() throws InvalidTransactionIdException {
        try {
            ezshop.addProductToSaleRFID(0, RFID1);
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDZeroTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDZeroTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDZeroTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDZeroTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testAddProductToSaleRFIDNullTId() throws InvalidTransactionIdException {
        try {
            ezshop.addProductToSaleRFID(null, RFID1);
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNullTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNullTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNullTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNullTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testAddProductToSaleRFIDEmptyRFID() throws InvalidRFIDException {
        try {
            ezshop.addProductToSaleRFID(100, "");
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDEmptyRFID", "This operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDEmptyRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDEmptyRFID", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDEmptyRFID", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testAddProductToSaleRFIDInvalidRFID() throws InvalidRFIDException {
        try {
            ezshop.addProductToSaleRFID(100, RFIDfromInvalid);
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDInvalidRFID", "This operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDInvalidRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDInvalidRFID", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDInvalidRFID", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void testAddProductToSaleRFIDNotLogged() throws UnauthorizedException {
        try {
            ezshop.logout();
            ezshop.addProductToSaleRFID(100, RFID1);
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNotLogged", "The operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNotLogged","Transaction id should not be considered invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNotLogged","Product code should not be considered invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testAddProductToSaleRFIDNotLogged", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testDeleteProductFromSaleRFIDNegativeTId() throws InvalidTransactionIdException {
        try {
            ezshop.deleteProductFromSaleRFID(-1, RFID1);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNegativeTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNegativeTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNegativeTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNegativeTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testDeleteProductFromSaleRFIDZeroTId() throws InvalidTransactionIdException {
        try {
            ezshop.deleteProductFromSaleRFID(0, RFID1);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDZeroTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDZeroTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDZeroTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDZeroTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testDeleteProductFromSaleRFIDNullTId() throws InvalidTransactionIdException {
        try {
            ezshop.deleteProductFromSaleRFID(null, RFID1);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullTId", "This operation should fail"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullTId","Product code should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullTId", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullTId", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testDeleteProductFromSaleRFIDEmptyRFID() throws InvalidRFIDException {
        try {
            ezshop.deleteProductFromSaleRFID(100, "");
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDEmptyRFID", "This operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDEmptyRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDEmptyRFID", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDEmptyRFID", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testDeleteProductFromSaleRFIDNullRFID() throws InvalidRFIDException {
        try {
            ezshop.deleteProductFromSaleRFID(100, null);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullRFID", "This operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullRFID", "Logged user should be authorized"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNullRFID", "The product quantity should not be considered as invalid"));
        }
        
    }

    @Test(expected = InvalidRFIDException.class)
    public void testDeleteProductFromSaleRFIDInvalidRFID() throws InvalidRFIDException {
        try {
            ezshop.deleteProductFromSaleRFID(100, RFIDfromInvalid);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDInvalidRFID", "This operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDInvalidRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDInvalidRFID", "Logged user should be authorized"));
        }
        catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDInvalidRFID", "The product quantity should not be considered as invalid"));
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void testDeleteProductFromSaleRFIDNotLogged() throws UnauthorizedException {
        try {
            ezshop.logout();
            ezshop.deleteProductFromSaleRFID(100, RFID1);
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNotLogged", "The operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNotLogged","Transaction id should not be considered invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNotLogged","Product quantity should not be considered invalid"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFIDNotLogged", "The RFIDfrom should not be considered as invalid"));
        }
    }

    @Test
    public void testDeleteProductFromSaleRFID() {
        try {
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int prodId = ezshop.createProductType(productDescr1,barCode,pricePerUnit, note1);
            ezshop.updatePosition(prodId,location1);
            ezshop.updateQuantity(prodId,quantity);          
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1);
            Assert.assertFalse(getErrorMsg("testDeleteProductFromSaleRFID", "This operation should return false"), ezshop.deleteProductFromSaleRFID(404, RFIDDiff));          
            int transactionId = ezshop.startSaleTransaction();
            ezshop.addProductToSaleRFID(transactionId,RFID1);
            Assert.assertTrue(getErrorMsg("testDeleteProductFromSaleRFID", "This operation should not fail"), ezshop.deleteProductFromSaleRFID(transactionId,RFID1));
            Assert.assertFalse(getErrorMsg("testDeleteProductFromSaleRFID", "This operation should return false"), ezshop.deleteProductFromSaleRFID(transactionId, RFIDDiff));
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product description should not be considered invalid"));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID", "The order id should not be considered as invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product price should not be considered invalid"));
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product id should not be considered invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product amount should not be considered invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product code should not be considered invalid"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","Product location should not be considered invalid"));
        }
        catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testDeleteProductFromSaleRFID","RFID should not be considered invalid"));
        }
    }

    @Test
    public void testReturnProductRFID(){
        try{
            Assert.assertFalse(getErrorMsg("testReturnProductRFID","Cannot return product without a transaction"),ezshop.returnProductRFID(100, RFID1));

            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            ezshop.updatePosition(id, location1);
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1);
            
            int transactionId = ezshop.startSaleTransaction();
            ezshop.addProductToSaleRFID(transactionId, RFID1);
            ezshop.endSaleTransaction(transactionId);
            SaleTransaction saleTransaction = ezshop.getSaleTransaction(transactionId);
            ezshop.receiveCashPayment(saleTransaction.getTicketNumber(), saleTransaction.getPrice());
            int returnId = ezshop.startReturnTransaction(saleTransaction.getTicketNumber());
            Assert.assertTrue(getErrorMsg("testReturnProductRFID","Return value should have been true"),ezshop.returnProductRFID(returnId,RFID1));
            Assert.assertFalse(getErrorMsg("testReturnProductRFID","Return value should have been false"),ezshop.returnProductRFID(returnId,RFIDDiff));
            Assert.assertFalse(getErrorMsg("testReturnProductRFID","Return value should have been false"),ezshop.returnProductRFID(returnId,RFID2));
        } catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "The order id should not be considered as invalid"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Transaction id should not be considered invalid"));
        } catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product quantity should not be considered invalid"));
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product id should not be considered invalid"));
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product description should not be considered invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product code should not be considered invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product price should not be considered invalid"));
        } catch (InvalidPaymentException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Payment should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "The logged user should be authorized"));
        } catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product location should not be considered invalid"));
        } catch (InvalidRFIDException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFID", "Product RFID should not be considered invalid"));  
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testReturnProductRFIDNegativeTId() throws InvalidTransactionIdException {
        try{
            ezshop.returnProductRFID(-1,RFID1);
            Assert.fail(getErrorMsg("testReturnProductRFIDNegativeTId","The operation should have failed"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNegativeTId", "Product RFID should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNegativeTId", "The logged user should be authorized"));
        }
        
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testReturnProductRFIDNullTId() throws InvalidTransactionIdException {
        try{
            ezshop.returnProductRFID(null,RFID1);
            Assert.fail(getErrorMsg("testReturnProductRFIDNullTId","The operation should have failed"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNullTId", "Product RFID should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNullTId", "The logged user should be authorized"));
        }
    }

    @Test(expected = InvalidTransactionIdException.class)
    public void testReturnProductRFIDZeroTId() throws InvalidTransactionIdException {
        try{
            ezshop.returnProductRFID(0,RFID1);
            Assert.fail(getErrorMsg("testReturnProductRFIDZeroTId","The operation should have failed"));
        } catch (InvalidRFIDException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDZeroTId", "Product RFID should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDZeroTId", "The logged user should be authorized"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testReturnProductEmptyRFID() throws InvalidRFIDException {
        try{
            ezshop.returnProductRFID(100,"");
            Assert.fail(getErrorMsg("testReturnProductEmptyRFID","The operation should have failed"));
        }  catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductEmptyRFID", "Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductEmptyRFID", "The logged user should be authorized"));
        }
    }

    @Test(expected = InvalidRFIDException.class)
    public void testReturnProductNullRFID() throws InvalidRFIDException {
        try{
            ezshop.returnProductRFID(100,null);
            Assert.fail(getErrorMsg("testReturnProductNullRFID","The operation should have failed"));
        }  catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductNullRFID", "Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductNullRFID", "The logged user should be authorized"));
        }
    }


    @Test(expected = InvalidRFIDException.class)
    public void testReturnProductInvalidRFID() throws InvalidRFIDException {
        try{
            ezshop.recordBalanceUpdate(pricePerUnit*quantity);
            int id = ezshop.createProductType(productDescr1, barCode, pricePerUnit, note1);
            ezshop.updatePosition(id, location1);
            int orderId = ezshop.issueOrder(barCode, quantity, orderPricePerUnit);
            ezshop.payOrder(orderId);
            ezshop.recordOrderArrivalRFID(orderId, RFIDfrom1);
            
            int transactionId = ezshop.startSaleTransaction();
            ezshop.addProductToSaleRFID(transactionId, RFID1);
            ezshop.endSaleTransaction(transactionId);
            SaleTransaction saleTransaction = ezshop.getSaleTransaction(transactionId);
            ezshop.receiveCashPayment(saleTransaction.getTicketNumber(), saleTransaction.getPrice());
            int returnId = ezshop.startReturnTransaction(saleTransaction.getTicketNumber());
            
            ezshop.returnProductRFID(returnId,RFIDfromInvalid);
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID","The operation should have failed"));
        }  catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Transaction id should not be considered invalid"));
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "The logged user should be authorized"));
        }

        catch (InvalidOrderIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "The order id should not be considered as invalid"));
        }  catch (InvalidQuantityException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product quantity should not be considered invalid"));
        } catch (InvalidProductIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product id should not be considered invalid"));
        } catch (InvalidProductDescriptionException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product description should not be considered invalid"));
        } catch (InvalidProductCodeException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product code should not be considered invalid"));
        } catch (InvalidPricePerUnitException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product price should not be considered invalid"));
        } catch (InvalidPaymentException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Payment should not be considered invalid"));
        }  catch (InvalidLocationException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductInvalidRFID", "Product location should not be considered invalid"));
        }
    }

    @Test(expected = UnauthorizedException.class)
    public void testReturnProductRFIDNotLogged() throws UnauthorizedException {
        try {
            ezshop.logout();
            ezshop.returnProductRFID(100,RFID1);
            Assert.fail(getErrorMsg("testReturnProductRFIDNotLogged", "The operation should fail"));
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNotLogged", "Transaction id should not be considered invalid"));
        } catch (InvalidRFIDException e){
            e.printStackTrace();
            Assert.fail(getErrorMsg("testReturnProductRFIDNotLogged", "Product RFID should not be considered invalid"));
        
        }
    }
}
