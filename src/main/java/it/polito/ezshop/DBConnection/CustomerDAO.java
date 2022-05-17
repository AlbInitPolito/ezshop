package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.CustomerDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.CustomerModel;
import it.polito.ezshop.model.LoyaltyCardModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends mysqlDAO implements CustomerDAOInterface {

    public CustomerDAO() {
        super();
    }

    @Override
    public Customer addCustomer(Customer customer) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (customer == null)
            throw new MissingDAOParameterException("Customer object is required " +
                    "in addCustomer in CustomerDAO");
        String name = customer.getCustomerName();
        //String card = customer.getCustomerCard();
        if (name == null)
            throw new MissingDAOParameterException("Customer.customerName is required " +
                    "in addCustomer in CustomerDAO");
        if (name.length() > 80)
            throw new InvalidDAOParameterException("Customer.customerName length cannot be > 80 " +
                    "in addCustomer in CustomerDAO");
        Customer c = null;
        String query = "INSERT INTO customer VALUES(";
        query = query + "null, '" + name + "' ";
        /*if ( card != null ) {
            if ( card.length() != 10 )
                return null;
            try{  Long.parseLong(card); }
            catch ( NumberFormatException e ){ e.printStackTrace(); return null; }
            query = query + ", " + card;
        }
        else*/
        query = query + ", null";
        query = query + ");";
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            if (opquery == null) return null;
            opquery = (db.executeQuery("SELECT * FROM customer WHERE id=" + opquery[0] + ";")).get(0);
            if (opquery == null) return null;
            c = new CustomerModel();
            c.setId(Integer.parseInt(opquery[0]));
            c.setCustomerName(opquery[1]);
            /*if( opquery[2] == null )
                c.setCustomerCard(null);
            else*/
            c.setCustomerCard(opquery[2]);
        }
        return c;
    }

    @Override
    public boolean removeCustomer(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (customerId == null)
            throw new MissingDAOParameterException("Customer.customerID is required " +
                    "in removeCustomer in CustomerDAO");
        if (customerId == 0)
            throw new InvalidDAOParameterException("Customer.customerID cannot be <= 0 " +
                    "in removeCustomer in CustomerDAO");
        String query = "DELETE FROM customer WHERE id=" + customerId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateCustomer(Customer customer) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (customer == null)
            throw new MissingDAOParameterException("Customer object is required " +
                    "in updateCustomer in CustomerDAO");
        Integer cid = customer.getId();
        String name = customer.getCustomerName();
        String card = customer.getCustomerCard();
        if (cid == null)
            throw new MissingDAOParameterException("Customer.customerID is required" +
                    " in updateCustomer in CustomerDAO");
        if (cid <= 0)
            throw new InvalidDAOParameterException("Customer.customerID cannot be <= 0" +
                    " in updateCustomer in CustomerDAO \n Given instead: " + cid);
        if ((name == null) && (card == null))
            throw new MissingDAOParameterException("At least one parameter (Customer.customerName " +
                    "or Customer.loyaltyCard) is required in updateCustomer in CustomerDAO");
        String query = "";
        if (card != null) {
            if (card.length() > 10)
                throw new InvalidDAOParameterException("Customer.loyaltyCard length cannot be of more than 10" +
                        " in updateCustomer in CustomerDAO \n Given instead: " + card);
            query = query + " loyalty_card='" + card + "'";
        }
        if (name != null) {
            if (name.length() > 80)
                throw new InvalidDAOParameterException("Customer.customerName length cannot be > 80 " +
                        "in updateCustomer in CustomerDAO");
            if (query.length() > 0)
                query = query + ", ";
            query = query + " name='" + name + "'";
        }
        query = "UPDATE customer SET " + query + " WHERE id=" + cid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<Customer> getCustomers() {
        String query = "SELECT * FROM customer";
        List<String[]> result = db.executeQuery(query);
        List<Customer> customers = new ArrayList<>();
        Customer c;
        int cid;
        String name;
        String loyalty_card;
        for (String[] tuple : result) {
            cid = Integer.parseInt(tuple[0]);
            name = tuple[1];
            if (tuple[2] == null)
                loyalty_card = null;
            else
                loyalty_card = tuple[2];
            c = new CustomerModel();
            c.setId(cid);
            c.setCustomerName(name);
            c.setCustomerCard(loyalty_card);
            customers.add(c);
        }
        return customers;
    }

    @Override
    public Customer getCustomer(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (customerId == null)
            throw new MissingDAOParameterException("Customer.customerID is required" +
                    " in getCustomer in CustomerDAO");
        if (customerId <= 0)
            throw new InvalidDAOParameterException("Customer.customerID cannot be <= 0" +
                    " in getCustomer in CustomerDAO \n Given instead: " + customerId);
        String query = "SELECT * FROM customer WHERE id=" + customerId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        int cid = Integer.parseInt(tuple[0]);
        String name = tuple[1];
        String loyalty_card;
        if (tuple[2] == null)
            loyalty_card = null;
        else
            loyalty_card = tuple[2];
        Customer c = new CustomerModel();
        c.setId(cid);
        c.setCustomerName(name);
        c.setCustomerCard(loyalty_card);
        return c;
    }

    @Override
    public Customer getCustomerByCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (serialNumber == null)
            throw new MissingDAOParameterException("serialNumber is required" +
                    " in getCustomerByCard in CustomerDAO");
        if (serialNumber.length() > 10)
            throw new InvalidDAOParameterException("serialNumber cannot be of length > 10" +
                    " in getCustomerByCard in CustomerDAO \n Given instead: " + serialNumber);
        String query = "SELECT * FROM customer WHERE loyalty_card='" + serialNumber + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        Integer cid = Integer.parseInt(tuple[0]);
        String name = tuple[1];
        String loyalty_card = tuple[2];
        Customer c = new CustomerModel();
        c.setId(cid);
        c.setCustomerName(name);
        c.setCustomerCard(loyalty_card);
        return c;
    }

    @Override
    public boolean detachCustomerCard(Integer customerId) throws MissingDAOParameterException, InvalidDAOParameterException{
        if (customerId == null)
            throw new MissingDAOParameterException("customerId is required" +
                    " in detachCustomerCard in CustomerDAO");
        if (customerId <= 0)
            throw new InvalidDAOParameterException("customerId must be > 0" +
                    " in detachCustomerCard in CustomerDAO \n Given instead: " + customerId);
        String query = "UPDATE customer SET loyalty_card=null WHERE id="+customerId;
        return db.executeUpdate(query);
    }

    @Override
    public boolean resetCustomers() {
        String query = "DELETE FROM customer;";
        return db.executeUpdate(query);
    }
}
