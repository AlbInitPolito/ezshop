package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.CustomerDAO;
import it.polito.ezshop.DBConnection.LoyaltyCardDAO;
import it.polito.ezshop.data.Customer;
import it.polito.ezshop.exceptions.*;

import java.util.Random;

public class LoyaltyCardModel {
    private Integer points;
    private String serialNumber;

    public LoyaltyCardModel(Integer points, String serialNumber) {
        this.points = points;
        this.serialNumber = serialNumber;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public static boolean checkSerialNumberFormat(String serialNumber) {
        if (serialNumber.length() != 10)
            return false;

        for (int i = 0; i < 10; i++)
            if (!Character.isDigit(serialNumber.charAt(i)))
                return false;

        return true;
    }

    public static String generateSerialNumber() {
        int leftLimit = 48; // '0'
        int rightLimit = 57; // '9'
        int length = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * This method returns a string containing the code of a new assignable card.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @return the code of a new available card. An empty string if the db is unreachable
     */
    public static String createCard() {
        String serialNumber;
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        LoyaltyCardModel lc = new LoyaltyCardModel(0, null);
        try {
            do {
                serialNumber = generateSerialNumber();
                lc.setSerialNumber(serialNumber);
            } while (lcdao.getCard(lc.getSerialNumber()) != null); // iterates until an unused serialNumber is generated

            if (lcdao.addCard(lc.getSerialNumber()))
                return serialNumber;
            else {
                System.err.println("Couldn't create new customer card");
                return null;
            }
        } catch (InvalidDAOParameterException | MissingDAOParameterException i) {
            i.printStackTrace();
            return null;
        }
    }

    /**
     * This method assigns a card with given card code to a customer with given identifier. A card with given card code
     * can be assigned to one customer only.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param customerCard the number of the card to be attached to a customer
     * @param customerId   the id of the customer the card should be assigned to
     * @return true if the operation was successful
     * false if the card is already assigned to another user, if there is no customer with given id, if the db is unreachable
     * @throws InvalidCustomerIdException   if the id is null, less than or equal to 0.
     * @throws InvalidCustomerCardException if the card is null, empty or in an invalid format
     */
    public static boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException {
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        CustomerDAO cdao = new CustomerDAO();
        if (customerId == null || customerId <= 0) {
            System.err.println("Invalid customer ID");
            throw new InvalidCustomerIdException();
        }

        if (customerCard == null || customerCard.equals("") || !LoyaltyCardModel.checkSerialNumberFormat(customerCard)) {
            System.err.println("Invalid customer card");
            throw new InvalidCustomerCardException();
        }

        Customer customer = new CustomerModel();
        customer.setId(customerId);
        try {
            customer = cdao.getCustomer(customer.getId());
            if (customer == null) {
                System.err.println("No customer with given ID");
                return false;
            }
            LoyaltyCardModel lc = new LoyaltyCardModel(0, customerCard);
            if (lcdao.getCard(lc.getSerialNumber()) == null) {
                System.err.println("No existing card with specified serial number");
                return false;
            }
            if (cdao.getCustomerByCard(lc.getSerialNumber()) != null) {
                System.err.println("Card with specified serial number already assigned to an existing user");
                return false;
            }
            customer.setCustomerCard(customerCard);
            return cdao.updateCustomer(customer);
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return false;
        }
    }

    /**
     * This method updates the points on a card adding to the number of points available on the card the value assumed by
     * <pointsToBeAdded>. The points on a card should always be greater than or equal to 0.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param customerCard    the card the points should be added to
     * @param pointsToBeAdded the points to be added or subtracted ( this could assume a negative value)
     * @return true if the operation is successful
     * false   if there is no card with given code,
     * if pointsToBeAdded is negative and there were not enough points on that card before this operation,
     * if we cannot reach the db.
     * @throws InvalidCustomerCardException if the card is null, empty or in an invalid format
     */
    public static boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException {
        if (customerCard == null || customerCard.equals("") || !checkSerialNumberFormat(customerCard)) {
            System.err.println("Invalid customer card");
            throw new InvalidCustomerCardException();
        }
        LoyaltyCardDAO lcdao = new LoyaltyCardDAO();
        LoyaltyCardModel lc = new LoyaltyCardModel(0, customerCard);
        try {
            lc = lcdao.getCard(lc.getSerialNumber());
            if (lc == null) {
                System.err.println("No card with given code");
                return false;
            }

            if (lc.getPoints() + pointsToBeAdded < 0) {
                System.err.println("Not enough points to complete operation");
                return false;
            }

            lc.setPoints(lc.getPoints() + pointsToBeAdded);
            lcdao.updateCard(lc);
            return true;
        } catch (MissingDAOParameterException | InvalidDAOParameterException m) {
            m.printStackTrace();
            return false;
        }
    }

}
