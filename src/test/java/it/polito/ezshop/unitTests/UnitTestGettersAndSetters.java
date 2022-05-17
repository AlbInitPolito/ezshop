package it.polito.ezshop.unitTests;

import it.polito.ezshop.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class UnitTestGettersAndSetters {

    @Test
    public void testGettersAndSetters() {

        /* ------- UserModel ------- */
        UserModel user = new UserModel(null, null, "Cashier");
        user.setId(1234);
        user.setUsername("Testing username");
        user.setPassword("Testing password");
        user.setRole("Administrator");
        assert (1234 == user.getId());
        assert (user.getUsername().equals("Testing username"));
        assert (user.getPassword().equals("Testing password"));
        assert (user.getRole().equals("Administrator"));
        /* ------------------------- */

        /* ----- CustomerModel ----- */
        CustomerModel customer = new CustomerModel();
        customer.setCustomerName("Testing customer name");
        customer.setId(1234);
        assert (customer.getCustomerName().equals("Testing customer name"));
        assert (customer.getId() == 1234);
        /* ------------------------- */

        /* ---- LoyaltyCardModel --- */
        LoyaltyCardModel lc = new LoyaltyCardModel(0, null);
        lc.setPoints(1234);
        lc.setSerialNumber("Testing serial number");
        assert (lc.getPoints() == 1234);
        assert (lc.getSerialNumber().equals("Testing serial number"));
        /* ------------------------- */

        /* ---- TicketEntryModel --- */
        TicketEntryModel ticketEntry = new TicketEntryModel();
        ticketEntry.setAmount(1234);
        ticketEntry.setProductDescription("Testing product description");
        ticketEntry.setBarCode("Testing barcode");
        ticketEntry.setPricePerUnit(1234.56);
        ticketEntry.setDiscountRate(789.01);
        assert (ticketEntry.getAmount() == 1234);
        assert (ticketEntry.getProductDescription().equals("Testing product description"));
        assert (ticketEntry.getBarCode().equals("Testing barcode"));
        assert (ticketEntry.getPricePerUnit() == 1234.56);
        assert (ticketEntry.getDiscountRate() == 789.01);
        /* ------------------------- */

        /* - BalanceOperationModel - */
        BalanceOperationModel balanceOperation = new BalanceOperationModel(0, null, 0.0, null);
        balanceOperation.setBalanceId(1234);
        balanceOperation.setMoney(1234.56);
        balanceOperation.setDate(LocalDate.now());
        balanceOperation.setType("CREDIT");
        assert (balanceOperation.getBalanceId() == 1234);
        assert (balanceOperation.getMoney() == 1234.56);
        assert (balanceOperation.getDate().equals(LocalDate.now()));
        assert (balanceOperation.getType().equals("CREDIT"));
        /* ------------------------- */

        /* ------ OrderModel ------- */
        OrderModel order1 = new OrderModel();
        order1.setPricePerUnit(1234.56);
        order1.setQuantity(1234);
        order1.setStatus("PAYED");
        order1.setOrderId(1234);
        order1.setBalanceId(5678);
        order1.setProductCode("6291041500213");
        assert (order1.getPricePerUnit() == 1234.56);
        assert (order1.getQuantity() == 1234);
        assert (order1.getStatus().equals("PAYED"));
        assert (order1.getOrderId() == 1234);
        assert (order1.getBalanceId() == 5678);
        assert (order1.getProductCode().equals("6291041500213"));

        OrderModel order2 = new OrderModel(1234.56, 1234, OrderModel.OrderStatus.valueOf("PAYED"), 5678, 1234, "6291041500213");
        assert (order2.getPricePerUnit() == 1234.56);
        assert (order2.getQuantity() == 1234);
        assert (order2.getStatus().equals("PAYED"));
        assert (order2.getOrderId() == 1234);
        assert (order2.getBalanceId() == 5678);
        assert (order2.getProductCode().equals("6291041500213"));

        order2.setStatus("ISSUED");
        assert (order2.getStatus().equals("ISSUED"));
        order2.setStatus("ORDERED");
        assert (order2.getStatus().equals("ORDERED"));
        order2.setStatus("PAYED");
        assert (order2.getStatus().equals("PAYED"));
        order2.setStatus("COMPLETED");
        assert (order2.getStatus().equals("COMPLETED"));
        assertThrows(IllegalArgumentException.class, () -> order2.setStatus("BIAGIO"));
        /* ------------------------- */

        /* ---- ProductTypeModel --- */
        ProductTypeModel productType = new ProductTypeModel();
        productType.setId(1234);
        productType.setBarCode("6291041500213");
        productType.setProductDescription("Testing product description");
        productType.setPricePerUnit(1234.56);
        productType.setQuantity(1234);
        productType.setDiscountRate(1234.56);
        productType.setNote("Testing note");
        productType.setAisleID(5678);
        productType.setRackID("F");
        productType.setLevelID(9012);
        assert (productType.getId() == 1234);
        assert (productType.getBarCode().equals("6291041500213"));
        assert (productType.getProductDescription().equals("Testing product description"));
        assert (productType.getPricePerUnit() == 1234.56);
        assert (productType.getQuantity() == 1234);
        assert (productType.getDiscountRate() == 1234.56);
        assert (productType.getNote().equals("Testing note"));
        assert (productType.getAisleID() == 5678);
        assert (productType.getRackID().equals("F"));
        assert (productType.getLevelID() == 9012);
        productType.setLocation("1234-F-5678");
        assert (productType.getLocation().equals("1234-F-5678"));

        ProductTypeModel productType2 = new ProductTypeModel(1234, "6291041500213", "Testing product description", 1234.56, 1234,
                1234.56, "Testing note", 5678, "F", 9012);
        assert (productType2.getId() == 1234);
        assert (productType2.getBarCode().equals("6291041500213"));
        assert (productType2.getProductDescription().equals("Testing product description"));
        assert (productType2.getPricePerUnit() == 1234.56);
        assert (productType2.getQuantity() == 1234);
        assert (productType2.getDiscountRate() == 1234.56);
        assert (productType2.getNote().equals("Testing note"));
        assert (productType2.getAisleID() == 5678);
        assert (productType2.getRackID().equals("F"));
        assert (productType2.getLevelID() == 9012);
        productType2.setLocation("1234-F-5678");
        assert (productType2.getLocation().equals("1234-F-5678"));

        ProductTypeModel productType3 = new ProductTypeModel("6291041500213", "Testing product description", 1234.56, 1234,
                1234.56, "Testing note", 5678, "F", 9012);
        assert (productType3.getBarCode().equals("6291041500213"));
        assert (productType3.getProductDescription().equals("Testing product description"));
        assert (productType3.getPricePerUnit() == 1234.56);
        assert (productType3.getQuantity() == 1234);
        assert (productType3.getDiscountRate() == 1234.56);
        assert (productType3.getNote().equals("Testing note"));
        assert (productType3.getAisleID() == 5678);
        assert (productType3.getRackID().equals("F"));
        assert (productType3.getLevelID() == 9012);
        productType3.setLocation("1234-F-5678");
        assert (productType3.getLocation().equals("1234-F-5678"));
        productType3.setLocation(null);
        assert (productType3.getRackID() == null);
        /* ------------------------- */

        /* ---- CreditCardModel ---- */
        CreditCardModel creditCard = new CreditCardModel();
        creditCard.setCreditCardBalance(1234.56);
        creditCard.setCreditCardNumber("Testing credit card number");
        assert (creditCard.getCreditCardBalance() == 1234.56);
        assert (creditCard.getCreditCardNumber().equals("Testing credit card number"));
        /* ------------------------- */

        /* -- SaleTransactionModel - */
        SaleTransactionModel saleTransaction = new SaleTransactionModel();
        saleTransaction.setTicketNumber(1234);
        saleTransaction.setDate(Date.valueOf(LocalDate.now()));
        saleTransaction.setPaymentType(1);
        saleTransaction.setDiscountRate(1234.56);
        saleTransaction.setTime(Time.valueOf(LocalTime.now()));
        saleTransaction.setPrice(3456.78);
        assert (saleTransaction.getTicketNumber() == 1234);
        assert (saleTransaction.getDate().equals(Date.valueOf(LocalDate.now())));
        assert (saleTransaction.getPaymentType() == 1);
        assert (saleTransaction.getDiscountRate() == 1234.56);
        assert (saleTransaction.getTime().equals(Time.valueOf(LocalTime.now())));
        assert (saleTransaction.getPrice() == 3456.78);
        /* ------------------------- */

        /* - ReturnTransactionModel */
        ReturnTransactionModel returnTransaction = new ReturnTransactionModel(null, null, null, null);
        returnTransaction.setTransactionID(1234);
        returnTransaction.setReturnedValue(1234.56);
        returnTransaction.setID(5678);
        assert (returnTransaction.getTransactionID() == 1234);
        assert (returnTransaction.getReturnedValue() == 1234.56);
        assert (returnTransaction.getID() == 5678);
        /* ------------------------ */
    }

    @Test
    public void testNullOrInvalidSetters() {

        /* UserModel */
        UserModel user = new UserModel("username", "password", "Administrator");
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        assert (user.getId() == null);
        assert (user.getUsername() == null);
        assert (user.getPassword() == null);
        assert (user.getRole() == null);
        assertThrows(IllegalArgumentException.class, () -> {
            user.setRole("Invalid role");
        });

        UserModel user0 = new UserModel(null, null, null);

        /* CustomerModel */
        CustomerModel customer = new CustomerModel();
        customer.setCustomerName(null);
        customer.setId(null);
        assert (customer.getCustomerName() == null);
        assert (customer.getId() == null);

        /* LoyaltyCardModel */
        LoyaltyCardModel loyaltyCard = new LoyaltyCardModel(100, "serial number");
        loyaltyCard.setPoints(null);
        loyaltyCard.setSerialNumber(null);
        assert (loyaltyCard.getPoints() == null);
        assert (loyaltyCard.getSerialNumber() == null);

        /* TicketEntryModel */
        TicketEntryModel ticketEntry = new TicketEntryModel("8100844239540563", "Prod", 5, 0.5, 0.5);
        ticketEntry.setBarCode(null);
        ticketEntry.setProductDescription(null);
        assert (ticketEntry.getBarCode() == null);
        assert (ticketEntry.getProductDescription() == null);

        /* BalanceOperationModel */
        BalanceOperationModel balanceOperation = new BalanceOperationModel(0, LocalDate.now(), 0, BalanceOperationModel.Type.CREDIT);
        balanceOperation.setDate(null);
        balanceOperation.setType(null);
        assert (balanceOperation.getDate() == null);
        assert (balanceOperation.getType() == null);

        /* SaleTransactionModel */
        SaleTransactionModel saleTransaction = new SaleTransactionModel(1234, 2, 10.2, 19, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), null);
        saleTransaction.setTicketNumber(null);
        saleTransaction.setPaymentType(null);
        saleTransaction.setDate(null);
        saleTransaction.setTime(null);
        assert (saleTransaction.getTicketNumber() == null);
        assert (saleTransaction.getPaymentType() == null);
        assert (saleTransaction.getDate() == null);
        assert (saleTransaction.getTime() == null);

        /* ReturnTransactionModel */
        ReturnTransactionModel returnTransaction = new ReturnTransactionModel(1000, 10.0, 1002, null);
        returnTransaction.setID(null);
        returnTransaction.setReturnedValue(null);
        returnTransaction.setTransactionID(null);
        assert (returnTransaction.getID() == null);
        assert (returnTransaction.getReturnedValue() == null);
        assert (returnTransaction.getTransactionID() == null);

        returnTransaction = new ReturnTransactionModel();
        assert (returnTransaction.getID() == null);
        assert (returnTransaction.getReturnedValue() == 0.0);
        assert (returnTransaction.getTransactionID() == null);

        /* OrderModel */
        OrderModel order = new OrderModel();
        order.setBalanceId(null);
        order.setOrderId(null);
        order.setProductCode(null);
        order.setStatus(null);
        order.setPricePerUnit(0);
        order.setQuantity(0);
        assert (order.getBalanceId() == null);
        assert (order.getOrderId() == null);
        assert (order.getProductCode() == null);
        assert (order.getStatus() == null);
        assert (order.getQuantity() == 0);
        assert (order.getPricePerUnit() == 0);

        /* ProductTypeModel */
        ProductTypeModel productType = new ProductTypeModel();
        productType.setId(null);
        productType.setQuantity(null);
        productType.setAisleID(null);
        productType.setLevelID(null);
        productType.setBarCode(null);
        productType.setProductDescription(null);
        productType.setNote(null);
        productType.setRackID(null);
        productType.setPricePerUnit(null);
        productType.setDiscountRate(null);
        assert (productType.getId() == null);
        assert (productType.getQuantity() == null);
        assert (productType.getAisleID() == null);
        assert (productType.getLevelID() == null);
        assert (productType.getBarCode() == null);
        assert (productType.getProductDescription() == null);
        assert (productType.getNote() == null);
        assert (productType.getRackID() == null);
        assert (productType.getPricePerUnit() == null);
        assert (productType.getDiscountRate() == null);
        assertThrows(NumberFormatException.class, () -> {
            productType.setLocation("Invalid location");
        });
        productType.setLocation(null);
        assert (productType.getLocation() == null);

        /* CreditCardModel */
        CreditCardModel creditCard = new CreditCardModel();
        creditCard.setCreditCardBalance(null);
        creditCard.setCreditCardNumber(null);
        assert (creditCard.getCreditCardBalance() == null);
        assert (creditCard.getCreditCardNumber() == null);
    }

}
