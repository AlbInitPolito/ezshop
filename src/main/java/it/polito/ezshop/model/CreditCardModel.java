package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidCreditCardException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

public class CreditCardModel {

    final public static Integer CREDIT_CARD_LENGTH = 16;
    private String creditCardNumber;
    private Double creditCardBalance;

    /**
     * Constructor
     *
     * @param creditCardNumber  creditCardNumber
     * @param creditCardBalance creditCardBalance
     */
    public CreditCardModel(String creditCardNumber, Double creditCardBalance) {
        this.creditCardNumber = creditCardNumber;
        this.creditCardBalance = creditCardBalance;
    }

    /**
     * Constructor
     */
    public CreditCardModel() {
    }

    /**
     * Get the Credit Card Number
     *
     * @return creditCardNumber creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Set the Credit Card Number
     *
     * @param creditCardNumber creditCardNumber
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Get the Credit Card Balance
     *
     * @return creditCardBalance creditCardBalance
     */
    public Double getCreditCardBalance() {
        return creditCardBalance;
    }

    /**
     * Set the Credit Card Balance
     *
     * @param creditCardBalance creditCardBalance
     */
    public void setCreditCardBalance(Double creditCardBalance) {
        this.creditCardBalance = creditCardBalance;
    }

    /**
     * Method to know whether the Credit Card has enough money to pay
     *
     * @param sum sum
     * @return whether the balance is enough to satisfy the payment's request or not
     */
    public boolean hasEnoughMoney(Double sum) {
        return this.creditCardBalance >= sum;
    }

    /**
     * Generate a valid Credit Card Number
     *
     * @return ccNumber a valid Credit Card Number (String)
     */
    public static String generateValidCreditCardNumber() {
        Integer[] creditCardNumber = new Integer[CREDIT_CARD_LENGTH];
        int tmpCCN;
        int creditCardNumberAccumulator = 0;
        StringBuilder outCCN = new StringBuilder();
        Random rand = new Random();
        int maximum = 9;

        for (int i = 0; i < CREDIT_CARD_LENGTH - 1; i++) {
            creditCardNumber[i] = rand.nextInt(maximum);
            tmpCCN = creditCardNumber[i];
            if (i % 2 == 0)
                tmpCCN *= 2;
            if (tmpCCN > 9)
                tmpCCN -= 9;
            creditCardNumberAccumulator += tmpCCN;
        }
        creditCardNumber[CREDIT_CARD_LENGTH - 1] = (creditCardNumberAccumulator % 10 == 0 ? 0 : 10 - (creditCardNumberAccumulator % 10));

        for (int i = 0; i < CREDIT_CARD_LENGTH; i++)
            outCCN.append(creditCardNumber[i]);

        return outCCN.toString();
    }

    /**
     * Load credit cards from the .txt file
     * FOR TESTING PURPOSE USE
     * *************** pathname = C:\Users\anima\IdeaProjects\ezshop\files\creditcardsFORTESTING.txt
     *
     * @param pathname of the existing file
     * @return the list of CreditCardModel or null if the file doesn't exist
     */
    public static List<CreditCardModel> loadCreditCardsFromFile(String pathname) {
        List<CreditCardModel> ccList = new ArrayList<>();
        String bufferIN;

        File file = new File(pathname);
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(file));
            while ((bufferIN = buffReader.readLine()) != null) {
                if (bufferIN.contains("#"))
                    continue;
                try {
                    if (bufferIN.split(";")[0].length() != 16)
                        return null;
                    Long.parseLong(bufferIN.split(";")[0]);
                    ccList.add(new CreditCardModel(bufferIN.split(";")[0], Double.parseDouble(bufferIN.split(";")[1])));
                } catch (NumberFormatException nfe) {
                    return null;
                }
            }
        } catch (IOException exp) {
            return null;
        }

        return ccList;
    }

    /**
     * Execute a payment guaranteeing the persistence on the file
     * containing all the Credit Cards.
     *
     * @param amount amount
     * @return whether the payment has been successfully completed or not
     */
    public boolean executePayment(String pathname, Double amount) {
        File file = new File(pathname);
        List<String> fileList = new ArrayList<>();
        String bufferIN;

        /* Amount has to be a positive value */
        if (amount == null || amount < 0)
            return false;

        /* Check if the payment can be successfully completed */
        if (!this.hasEnoughMoney(amount))
            return false;

        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(file));
            while ((bufferIN = buffReader.readLine()) != null)
                fileList.add(bufferIN);

            FileWriter fw = new FileWriter(pathname, false);
            for (String s : fileList) {
                if (!s.contains("#"))
                    if (s.split(";")[0].equals(this.creditCardNumber))
                        s = s.split(";")[0] + ";" + (this.creditCardBalance - amount);
                s += "\n";
                fw.append(s);
            }
            fw.close();
        } catch (IOException exp) {
            return false;
        }

        return true;
    }

    /**
     * The Luhn Algorithm is used to know
     * whether a CreditCard Number is valid or not.
     *
     * @param creditCardNumber creditCardNumber
     * @return whether the Credit Card Number is valid or not
     * @throws InvalidCreditCardException if the credit card number is empty, null or if luhn algorithm does not validate the credit card.
     */
    public static boolean luhnAlgorithm(String creditCardNumber) throws InvalidCreditCardException {
        if (creditCardNumber == null)
            throw new InvalidCreditCardException();

        Integer[] CCNumber = new Integer[creditCardNumber.length()];
        Integer luhnAccumulator = 0;
        long creditCardNumberInt;
        long tmpAccumulator;

        /* InvalidCreditCardException */
        if (creditCardNumber.length() != CREDIT_CARD_LENGTH)
            throw new InvalidCreditCardException();
        try {
            creditCardNumberInt = Long.parseLong(creditCardNumber);
        } catch (NumberFormatException nfe) {
            throw new InvalidCreditCardException();
        }

        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            tmpAccumulator = creditCardNumberInt % 10;
            CCNumber[i] = (int) tmpAccumulator;
            creditCardNumberInt /= 10;
        }

        for (int i = CCNumber.length - 1; i >= 0; i--)
            if (i % 2 == 0)
                CCNumber[i] *= 2;
        for (int i = CCNumber.length - 1; i >= 0; i--) {
            if (CCNumber[i] > 9) {
                do {
                    luhnAccumulator += CCNumber[i] % 10;
                    CCNumber[i] /= 10;
                }
                while (CCNumber[i] > 9);
            }
            luhnAccumulator += CCNumber[i];
        }
        return luhnAccumulator % 10 == 0;
    }
}
