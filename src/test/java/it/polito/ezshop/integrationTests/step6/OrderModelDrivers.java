package it.polito.ezshop.integrationTests.step6;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.OrderDAO;
import it.polito.ezshop.DBConnection.ProductEntryDAO;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.BalanceOperationModel;
import it.polito.ezshop.model.OrderModel;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class OrderModelDrivers {
    @Test
    public void testIssueOrderDriver() {
        String productCode = "8008008008006";
        int quantity = 30;
        double pricePerUnit = 0.5;

        OrderDAO orderDAO = new OrderDAO();
        OrderModel order = new OrderModel();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderModel.OrderStatus.ISSUED.name());

            order = (OrderModel) orderDAO.addOrder(order);
            assert (order != null);

            assert (order.getOrderId() > 0);

            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPayOrderForDriver() {
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;
        OrderDAO orderDAO = new OrderDAO();
        OrderModel order = new OrderModel();
        BalanceOperationDAO bDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        bDAO.resetBalanceOperations();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));

            BalanceOperationModel balance = new BalanceOperationModel(0, LocalDate.now(), pricePerUnit * quantity, BalanceOperationModel.Type.ORDER);
            bDAO.addBalanceOperation(balance);

            assertNotNull(balance);

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderModel.OrderStatus.ISSUED.name());
            order.setBalanceId(balance.getBalanceId());
            order = (OrderModel) orderDAO.addOrder(order);

            assertNotNull(order);

            order.setStatus(OrderModel.OrderStatus.PAYED.name());
            order.setQuantity(0);
            order.setPricePerUnit(0.0);
            order.setBalanceId(null);
            assert (orderDAO.updateOrder(order));

            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            bDAO.resetBalanceOperations();
        } catch (MissingDAOParameterException | InvalidDAOParameterException mpe) {
            mpe.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPayOrderDriver() {
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;
        OrderModel order = new OrderModel();
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO bDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        bDAO.resetBalanceOperations();
        productTypeDAO.resetProductTypes();


        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderModel.OrderStatus.ISSUED.name());
            order = (OrderModel) orderDAO.addOrder(order);
            order = (OrderModel) orderDAO.getOrder(order.getOrderId());

            assert (order != null);

            BalanceOperationModel balance = new BalanceOperationModel(0, LocalDate.now(), order.getPricePerUnit() * order.getQuantity(), BalanceOperationModel.Type.ORDER);
            balance = (BalanceOperationModel) bDAO.addBalanceOperation(balance);

            order.setStatus(OrderModel.OrderStatus.PAYED.name());
            order.setPricePerUnit(0.0);
            order.setQuantity(0);
            order.setBalanceId(balance.getBalanceId());
            order.setProductCode(null);

            assert (orderDAO.updateOrder(order));

            /* Reset */
            orderDAO.resetOrders();
            bDAO.resetBalanceOperations();
            productTypeDAO.resetProductTypes();

        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRecordOrderArrivalDriver() {
        String productCode = "8008008008006";
        int quantity = 80;
        double pricePerUnit = 0.5;
        OrderModel order = new OrderModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductTypeModel productTypeModel;

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderModel.OrderStatus.ISSUED.name());
            order = (OrderModel) orderDAO.addOrder(order);
            order = (OrderModel) orderDAO.getOrder(order.getOrderId());

            assert (order != null);

            order.setStatus(OrderModel.OrderStatus.COMPLETED.name());
            order.setPricePerUnit(0.0);
            order.setQuantity(0);
            order.setBalanceId(null);
            order.setProductCode(null);

            productTypeModel = (ProductTypeModel) productTypeDAO.getProductByBarcode(productCode);

            ProductTypeModel.updateQuantity(productTypeModel.getId(), order.getQuantity());
            productTypeModel.setLocation("1-A-2");

            Pattern pattern = Pattern.compile("^[0-9]+-[a-zA-Z]-[0-9]+$");
            Matcher matcher = pattern.matcher(productTypeModel.getLocation());

            assert (matcher.matches());
            assert (orderDAO.updateOrder(order));

            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
        } catch (InvalidDAOParameterException | MissingDAOParameterException | InvalidProductIdException ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAllOrdersDriver() {
        OrderModel ao = new OrderModel(0.5, 80, OrderModel.OrderStatus.PAYED, null, null, "8008008008006");
        OrderModel bo = new OrderModel(0.8, 50, OrderModel.OrderStatus.PAYED, null, null, "8008008008006");
        OrderModel co = new OrderModel(1.0, 10, OrderModel.OrderStatus.PAYED, null, null, "8008008008006");
        OrderDAO orderDAO = new OrderDAO();
        BalanceOperationDAO bDAO = new BalanceOperationDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        /* Reset */
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();
        bDAO.resetBalanceOperations();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5, 80, 0.2, "Freshness", 1, "F", 3));

            ao = (OrderModel) orderDAO.addOrder(ao);
            ao = (OrderModel) orderDAO.getOrder(ao.getOrderId());
            bo = (OrderModel) orderDAO.addOrder(bo);
            bo = (OrderModel) orderDAO.getOrder(bo.getOrderId());
            co = (OrderModel) orderDAO.addOrder(co);
            co = (OrderModel) orderDAO.getOrder(co.getOrderId());

            assert (ao != null);
            assert (bo != null);
            assert (co != null);

            ao.setStatus(OrderModel.OrderStatus.PAYED.name());
            ao.setPricePerUnit(0.0);
            ao.setQuantity(0);
            ao.setBalanceId(null);
            ao.setProductCode(null);

            bo.setStatus(OrderModel.OrderStatus.PAYED.name());
            bo.setPricePerUnit(0.0);
            bo.setQuantity(0);
            bo.setBalanceId(null);
            bo.setProductCode(null);

            co.setStatus(OrderModel.OrderStatus.PAYED.name());
            co.setPricePerUnit(0.0);
            co.setQuantity(0);
            co.setBalanceId(null);
            co.setProductCode(null);

            assert (orderDAO.updateOrder(ao));
            assert (orderDAO.updateOrder(bo));
            assert (orderDAO.updateOrder(co));

            List<Order> orderList = orderDAO.getOrders();

            assertNotNull(orderList);

            /* Reset */
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();
            bDAO.resetBalanceOperations();
        } catch (InvalidDAOParameterException | MissingDAOParameterException ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRecordOrderArrivalRFIDDriver() {
        String productCode = "8008008008006";
        String rfidCode = "000000010000";
        int quantity = 80;
        double pricePerUnit = 0.5;
        OrderModel order = new OrderModel();
        OrderDAO orderDAO = new OrderDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductTypeModel productTypeModel;
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();

        /* Reset */
        productEntryDAO.resetProductEntries();
        orderDAO.resetOrders();
        productTypeDAO.resetProductTypes();

        try {
            productTypeDAO.addProductType(new ProductTypeModel("8008008008006", "Apple", 0.5,
                    80, 0.2, "Freshness", 1, "F", 3));

            order.setOrderId(null);
            order.setProductCode(productCode);
            order.setQuantity(quantity);
            order.setPricePerUnit(pricePerUnit);
            order.setStatus(OrderModel.OrderStatus.ISSUED.name());
            order = (OrderModel) orderDAO.addOrder(order);
            order = (OrderModel) orderDAO.getOrder(order.getOrderId());

            assert (order != null);

            order.setStatus(OrderModel.OrderStatus.COMPLETED.name());
            order.setPricePerUnit(0.0);
            order.setBalanceId(null);

            productTypeModel = (ProductTypeModel) productTypeDAO.getProductByBarcode(productCode);

            List<String> allRFID = productEntryDAO.getProductEntries(order.getProductCode());

            productTypeModel.setLocation("1-A-2");
            ProductTypeModel.updateQuantity(productTypeModel.getId(), order.getQuantity());

            Pattern pattern = Pattern.compile("^[0-9]+-[a-zA-Z]-[0-9]+$");
            Matcher matcher = pattern.matcher(productTypeModel.getLocation());

            assert (matcher.matches());

            for (String rfid : OrderModel.generateRFID(rfidCode, order.getQuantity()))
                productEntryDAO.addProductEntry(productTypeModel.getBarCode(), rfid);

            assert (orderDAO.updateOrder(order));

            /* Reset */
            productEntryDAO.resetProductEntries();
            orderDAO.resetOrders();
            productTypeDAO.resetProductTypes();

        } catch (InvalidDAOParameterException | MissingDAOParameterException | InvalidProductIdException ex) {
            ex.printStackTrace();
            fail();
        }
    }
}
