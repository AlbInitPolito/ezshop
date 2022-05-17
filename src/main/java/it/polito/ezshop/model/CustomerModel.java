package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.LoyaltyCardDAO;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.*;

import java.util.List;

public class CustomerModel implements it.polito.ezshop.data.Customer {

    private String customerName;
    private LoyaltyCardModel loyaltyCard;
    private Integer customerID;

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Indirect getter method, calls the getter for the String attribute 'serialNumber' of the class
     * LoyaltyCard
     *
     * @return the String attribute serialNumber of the LoyaltyCard instance linked to this customer
     */
    @Override
    public String getCustomerCard() {
        if(this.loyaltyCard == null)
            return null;
        else
            return this.loyaltyCard.getSerialNumber();
    }

    @Override
    public void setCustomerCard(String customerCard) {
        if (customerCard == null) {
            this.loyaltyCard = null;
            return;
        }
        //LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        LoyaltyCardModel lc;
        /*if (customerCard.equals(" "))
            this.loyaltyCard = new LoyaltyCardModel(0, "0000000000");
        else {
            lc = new LoyaltyCardModel(0, customerCard);
            try {
                this.loyaltyCard = lcdao.getCard(lc.getSerialNumber());
            } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
                m.printStackTrace();
            }
        }*/
        lc = new LoyaltyCardModel(null, customerCard);
        this.loyaltyCard = lc;
    }

    @Override
    public Integer getId() {
        return this.customerID;
    }

    @Override
    public void setId(Integer id) {
        this.customerID = id;
    }

    /**
     * Indirect getter method, calls the getter for the Integer attribute 'points' of the class LoyaltyCard
     *
     * @return the Integer attribute 'points' of the LoyaltyCard instance linked to this customer
     */
    @Override
    public Integer getPoints() {
        return this.loyaltyCard.getPoints();
    }

    /**
     * Indirect setter method, calls the setter for the Integer attribute 'points' of the class LoyaltyCard, passing
     * the parameter 'points'
     *
     * @param points the value to assign to the Integer attribute 'points' of the LoyaltyCard instance linked to this
     *               customer
     */
    @Override
    public void setPoints(Integer points) {
        this.loyaltyCard.setPoints(points);
    }

    /**
     * This method saves a new customer into the system. The customer's name should be unique.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param customerName the name of the customer to be registered
     * @return the id (>0) of the new customer if successful, -1 otherwise
     * @throws InvalidCustomerNameException if the customer name is empty or null
     */
    public static Integer defineCustomer(String customerName) throws InvalidCustomerNameException {
        if (customerName == null || customerName.equals(""))
            throw new InvalidCustomerNameException();

        if(customerName.length() > 80)
            return -1;

        CustomerModel customer = new CustomerModel();
        customer.setCustomerName(customerName);
        CustomerDAO cdao = new CustomerDAO();
        try {
            Customer result = cdao.addCustomer(customer);
            if (result == null)
                return -1;
            else
                return result.getId();
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return -1;
        }
    }

    /**
     * This method returns a customer with given id.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param id the id of the customer
     * @return the customer with given id
     * null if that user does not exists
     * @throws InvalidCustomerIdException if the id is null, less than or equal to 0.
     */
    public static Customer getCustomer(Integer id) throws InvalidCustomerIdException {
        if (id == null || id <= 0) {
            System.err.println("Invalid customer ID");
            throw new InvalidCustomerIdException();
        }

        CustomerModel customer = new CustomerModel();
        customer.setId(id); // Other parameters are useless
        CustomerDAO cdao = new CustomerDAO();
        try {
            return cdao.getCustomer(customer.getId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns a list containing all registered users.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @return the list of all the customers registered
     */
    public static List<Customer> getAllCustomers() {
        CustomerDAO cdao = new CustomerDAO();
        return cdao.getCustomers();
    }

    /**
     * This method deletes a customer with given id from the system.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param id the id of the customer to be deleted
     * @return true if the customer was successfully deleted
     * false if the user does not exists or if we have problems to reach the db
     * @throws InvalidCustomerIdException if the id is null, less than or equal to 0.
     */
    public static boolean deleteCustomer(Integer id) throws InvalidCustomerIdException {
        if (id == null || id <= 0) {
            System.err.println("Invalid customer ID");
            throw new InvalidCustomerIdException();
        }

        CustomerModel customer = new CustomerModel();
        customer.setId(id); // Other parameters are useless
        CustomerDAO cdao = new CustomerDAO();
        try {
            if(cdao.getCustomer(id) == null)
                return false;

            return cdao.removeCustomer(customer.getId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return false;
        }
    }

    /**
     * This method updates the data of a customer with given <id>. This method can be used to assign/delete a card to a
     * customer. If <newCustomerCard> has a numeric value than this value will be assigned as new card code, if it is an
     * empty string then any existing card code connected to the customer will be removed and, finally, it it assumes the
     * null value then the card code related to the customer should not be affected from the update. The card code should
     * be unique and should be a string of 10 digits.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param id              the id of the customer to be updated
     * @param newCustomerName the new name to be assigned
     * @param newCustomerCard the new card code to be assigned. If it is empty it means that the card must be deleted,
     *                        if it is null then we don't want to update the cardNumber
     * @return true if the update is successful
     * false if the update fails ( cardCode assigned to another user, db unreacheable)
     * @throws InvalidCustomerIdException   if the customer ID is null or not positive
     * @throws InvalidCustomerNameException if the customer name is empty or null
     * @throws InvalidCustomerCardException if the customer card is empty, null or if it is not in a valid format (string with 10 digits)
     */
    public static boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException {
        if (id == null || id <= 0) {
            System.err.println("Invalid customer ID");
            throw new InvalidCustomerIdException();
        }
        if (newCustomerName == null || newCustomerName.equals("")) {
            System.err.println("Invalid customer name");
            throw new InvalidCustomerNameException();
        }
        if(newCustomerName.length() > 80)
            return false;

        if (newCustomerCard != null && !newCustomerCard.equals("") && !LoyaltyCardModel.checkSerialNumberFormat(newCustomerCard)) {
            System.err.println("Invalid customer card number");
            throw new InvalidCustomerCardException();
        }

        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        LoyaltyCardModel lc = new LoyaltyCardModel(0, newCustomerCard);

        try {
            if (newCustomerCard != null && !newCustomerCard.equals("") && cdao.getCustomerByCard(lc.getSerialNumber()) != null) {
                System.err.println("Card already assigned to different customer");
                return false;
            }

            CustomerModel customer = new CustomerModel();
            customer.setId(id);
            customer.setCustomerName(newCustomerName);

            if(newCustomerCard != null && newCustomerCard.equals("")) {
                customer.setCustomerCard(null);
                cdao.detachCustomerCard(id);
            }
            else
                customer.setCustomerCard(newCustomerCard);

            if(newCustomerCard != null && !newCustomerCard.equals(""))
                lcdao.addCard(newCustomerCard);
            return cdao.updateCustomer(customer);
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return false;
        }
    }
}
