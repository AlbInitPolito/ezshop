package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.OrderDAOInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends mysqlDAO implements OrderDAOInterface {

    public OrderDAO() {
        super();
    }

    @Override
    public Order addOrder(Order order) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (order == null)
            throw new MissingDAOParameterException("Order object is required" +
                    " in addOrder in OrderDAO");
        double price = order.getPricePerUnit();
        String barcode = order.getProductCode();
        if (barcode == null)
            throw new MissingDAOParameterException("Order.productCode is required" +
                    " in addOrder in OrderDAO");
        if (barcode.length() > 14)
            throw new InvalidDAOParameterException("Order.productCode length cannot be > 14" +
                    " in addOrder in OrderDAO \n Given instead: " + barcode);
        int quantity = order.getQuantity();
        if (price <= 0)
            throw new MissingDAOParameterException("Order.price is required" +
                    " in addOrder in OrderDAO");
        ProductTypeDAOInterface pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductByBarcode(barcode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return null;
        }
        if (prod == null)
            return null;
        if (quantity <= 0)
            throw new MissingDAOParameterException("Order.quantity is required" +
                    " in addOrder in OrderDAO");
        Order o = null;
        String query = "INSERT INTO product_order VALUES(null, " +
                price + ", " + quantity + ", '" + OrderModel.OrderStatus.ISSUED.name() + "', null, " + prod.getId() + ");";
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            if (opquery == null) return null;
            opquery = (db.executeQuery("SELECT * FROM product_order WHERE id=" + opquery[0] + ";")).get(0);
            if (opquery == null) return null;
            Integer id = Integer.parseInt(opquery[0]);
            price = Double.parseDouble(opquery[1]);
            quantity = Integer.parseInt(opquery[2]);
            String status = opquery[3];
            String product_type = opquery[5];
            o = new OrderModel(price, quantity, OrderModel.OrderStatus.valueOf(status), null, id, product_type);
        }
        return o;
    }

    @Override
    public boolean updateOrder(Order order) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (order == null)
            throw new MissingDAOParameterException("Order object is required" +
                    " in updateOrder in OrderDAO");
        Integer oid = order.getOrderId();
        String status = order.getStatus();
        Integer balanceid = order.getBalanceId();
        if (oid == null)
            throw new MissingDAOParameterException("Order.orderID is required" +
                    " in updateOrder in OrderDAO");
        if (oid <= 0)
            throw new InvalidDAOParameterException("Order.orderID must be > 0" +
                    " in updateOrder in OrderDAO \n Given instead: " + oid);
        if ((status == null) && (balanceid == null))
            throw new MissingDAOParameterException("At least one parameter (Order.status " +
                    "or Order.balanceID) is required in updateOrder in OrderDAO");
        String query = "";
        if (status != null) {
            query = query + " status='" + status + "'";
        }
        if (balanceid != null) {
            if (balanceid <= 0)
                throw new InvalidDAOParameterException("Order.balanceID must be > 0" +
                        " in updateOrder in OrderDAO \n Given instead: " + balanceid);
            if (query.length() > 0)
                query = query + ", ";
            query = query + " balance_operation=" + balanceid;
        }
        query = "UPDATE product_order SET " + query + " WHERE id=" + oid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<Order> getOrders() {
        String query = "SELECT * FROM product_order";
        List<String[]> result = db.executeQuery(query);
        List<Order> orders = new ArrayList<>();
        Order o;
        int id;
        double price;
        int quantity;
        String status;
        int balanceid;
        int product;
        for (String[] tuple : result) {
            id = Integer.parseInt(tuple[0]);
            price = Double.parseDouble(tuple[1]);
            quantity = Integer.parseInt(tuple[2]);
            status = tuple[3];
            if (tuple[4] == null)
                balanceid = 0;
            else
                balanceid = Integer.parseInt(tuple[4]);
            product = Integer.parseInt(tuple[5]);
            ProductTypeDAO pdao = new ProductTypeDAO();
            ProductType prod = null;
            try {
                prod = pdao.getProductById(product);
            } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
                //e.printStackTrace();
                //continue;
            }
            o = new OrderModel(price, quantity, OrderModel.OrderStatus.valueOf(status), balanceid, id, prod.getBarCode());
            orders.add(o);
        }
        return orders;
    }

    @Override
    public Order getOrder(Integer orderId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (orderId == null)
            throw new MissingDAOParameterException("Order.orderID is required" +
                    " in getOrder in OrderDAO");
        if (orderId <= 0)
            throw new InvalidDAOParameterException("Order.orderID must be > 0" +
                    " in getOrder in OrderDAO \n Given instead: " + orderId);
        String query = "SELECT * FROM product_order WHERE id=" + orderId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        orderId = Integer.parseInt(tuple[0]);
        Double price = Double.parseDouble(tuple[1]);
        Integer quantity = Integer.parseInt(tuple[2]);
        String status = tuple[3];
        int balanceid;
        if (tuple[4] == null)
            balanceid = 0;
        else
            balanceid = Integer.parseInt(tuple[4]);
        int product = Integer.parseInt(tuple[5]);
        ProductTypeDAO pdao = new ProductTypeDAO();
        ProductType prod = null;
        try {
            prod = pdao.getProductById(product);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            //e.printStackTrace();
            //return null;
        }
        return new OrderModel(price, quantity, OrderModel.OrderStatus.valueOf(status), balanceid, orderId, prod.getBarCode());
    }

    @Override
    public boolean resetOrders() {
        String query = "DELETE FROM product_order;";
        return db.executeUpdate(query);
    }
}
