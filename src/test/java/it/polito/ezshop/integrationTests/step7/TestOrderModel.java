package it.polito.ezshop.integrationTests.step7;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.OrderModel;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestOrderModel {
    @Test
    public void testIssueOrder() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        assertThrows(InvalidProductCodeException.class, () ->
                OrderModel.issueOrder(null, 1, 0.5));
        assertThrows(InvalidQuantityException.class, () ->
                OrderModel.issueOrder(productCode, -1, 1.0));
        assertThrows(InvalidPricePerUnitException.class, () ->
                OrderModel.issueOrder(productCode, quantity, -0.5));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            productTypeDAO.addProductType(product);
            OrderModel.issueOrder(productCode, 50, 0.5);

        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testPayOrderFor() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        assertThrows(InvalidProductCodeException.class, () ->
                OrderModel.payOrderFor(null, 1, 0.5));
        assertThrows(InvalidQuantityException.class, () ->
                OrderModel.payOrderFor(productCode, -1, 1.0));
        assertThrows(InvalidPricePerUnitException.class, () ->
                OrderModel.payOrderFor(productCode, quantity, -0.5));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            productTypeDAO.addProductType(product);
            OrderModel.payOrderFor(productCode, 50, 0.5);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        }

        String productCode2 = "0001110001116";
        product.setBarCode(productCode2);
        try {
            assert (OrderModel.payOrderFor(productCode2, 50, 0.5) == -1);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testPayOrder() {
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        Integer orderID;

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.payOrder(-1));

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            productTypeDAO.addProductType(product);
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);
            assert (OrderModel.payOrder(orderID));
            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testRecordOrderArrival() {
        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.recordOrderArrival(-1));
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;

        ProductTypeModel product = new ProductTypeModel();

        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        Integer orderID;

        /* Reset */
        ticketEntryDAO.resetTicketEntries();
        orderDAO.resetOrders();
        productEntryDAO.resetProductEntries();
        balanceOperationDAO.resetBalanceOperations();
        saleTransactionDAO.resetSaleTransactions();
        returnTransactionDAO.resetReturnTransactions();
        DbConnection dbConnection = DbConnection.getInstance();
        dbConnection.executeUpdate("DELETE FROM product_entry");
        productTypeDAO.resetProductTypes();

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            product.setLocation("1-C-2");
            ProductTypeModel.updatePosition(product.getId(), product.getLocation());
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);

            assert (!OrderModel.recordOrderArrival(orderID));
            assert (OrderModel.payOrder(orderID));
            OrderModel.recordOrderArrival(orderID);

            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            ticketEntryDAO.resetTicketEntries();
            orderDAO.resetOrders();
            productEntryDAO.resetProductEntries();
            balanceOperationDAO.resetBalanceOperations();
            saleTransactionDAO.resetSaleTransactions();
            returnTransactionDAO.resetReturnTransactions();
            dbConnection.executeUpdate("DELETE FROM product_entry");
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testRecordOrderArrivalRFID() {
        assertThrows(InvalidOrderIdException.class, () ->
                OrderModel.recordOrderArrival(-1));
        String productDes = "Apple";
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;
        String RFIDfrom = "000000010000";

        ProductTypeModel product = new ProductTypeModel();

        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ReturnTransactionDAO returnTransactionDAO = new ReturnTransactionDAO();
        SaleTransactionDAO saleTransactionDAO = new SaleTransactionDAO();
        BalanceOperationDAO balanceOperationDAO = new BalanceOperationDAO();
        TicketEntryDAO ticketEntryDAO = new TicketEntryDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        Integer orderID;

        /* Reset */
        productEntryDAO.resetProductEntries();
        ticketEntryDAO.resetTicketEntries();
        orderDAO.resetOrders();
        balanceOperationDAO.resetBalanceOperations();
        saleTransactionDAO.resetSaleTransactions();
        returnTransactionDAO.resetReturnTransactions();
        productTypeDAO.resetProductTypes();

        product.setProductDescription(productDes);
        product.setBarCode(productCode);
        product.setQuantity(quantity);
        product.setPricePerUnit(pricePerUnit);
        try {
            product = (ProductTypeModel) productTypeDAO.addProductType(product);
            product.setLocation("1-C-2");
            ProductTypeModel.updatePosition(product.getId(), product.getLocation());
            orderID = OrderModel.issueOrder(productCode, 50, 0.5);
            assert (orderID != null && orderID > 0);

            assert (!OrderModel.recordOrderArrival(orderID));
            assert (OrderModel.payOrder(orderID));
            OrderModel.recordOrderArrivalRFID(orderID, RFIDfrom);

            assert (!OrderModel.payOrder(512));
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            productEntryDAO.resetProductEntries();
            ticketEntryDAO.resetTicketEntries();
            orderDAO.resetOrders();
            balanceOperationDAO.resetBalanceOperations();
            saleTransactionDAO.resetSaleTransactions();
            returnTransactionDAO.resetReturnTransactions();
            productTypeDAO.resetProductTypes();
        }
    }

    @Test
    public void testGetAllOrders() {
        String productDes = "Apple";
        String productCode = "8008008008006";

        ProductTypeModel product = new ProductTypeModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        try {
            product.setProductDescription(productDes);
            product.setBarCode(productCode);
            product.setQuantity(80);
            product.setPricePerUnit(0.5);
            productTypeDAO.addProductType(product);

            OrderModel.issueOrder(productCode, 50, 0.5);
            OrderModel.issueOrder(productCode, 10, 0.4);
            OrderModel.issueOrder(productCode, 4, 0.2);
            OrderModel.issueOrder(productCode, 7, 0.3);

            List<Order> list = OrderModel.getAllOrders();

            assert (list != null);
            assert (list.size() == 4);
        } catch (Exception mpe) {
            mpe.printStackTrace();
            fail();
        } finally {
            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        }
    }
}
