package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.LoyaltyCardModel;

public interface LoyaltyCardDAOInterface {

    /** ----------------------------------------------------------------------------------------------------------
     * This method creates a new LoyaltyCard entry in DB
     *
     * @param serialNumber the LoyaltyCard serial number, must be not null and a number string of length <= 10
     *
     * @return BalanceOperation object with new given serial number and 0 points,
     *         null if insert goes wrong
     *
     * @throws MissingDAOParameterException if serialNumber is null
     * @throws InvalidDAOParameterException if serialNumber is of length > 10
     * ----------------------------------------------------------------------------------------------------------
     */
    boolean addCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** -------------------------------------------------------------------------------------------------------------------------
     * This method changes a LoyaltyCard entry for given parameters only
     *
     * @param card .serialNumber the LoyaltyCard serial number, must be not null and a number string of length <= 10,
     *             .points the new card points, must be not null and >= 0
     *
     * @return true if update succeeds,
     *         false if update fails
     *
     * @throws MissingDAOParameterException if card is null
     * @throws MissingDAOParameterException if card.serialNumber is null
     * @throws InvalidDAOParameterException if card.serialNumber is of length > 10
     * @throws MissingDAOParameterException if card.points is null
     * @throws InvalidDAOParameterException if card.points is >= 0
     * -------------------------------------------------------------------------------------------------------------------------
     */
    boolean updateCard(LoyaltyCardModel card) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** ----------------------------------------------------------------------------------------------------------
     * This method returns the LoyaltyCard in DB with given serial number
     *
     * @param serialNumber the LoyaltyCard serial number, must be not null and a number string of length <= 10
     *
     * @return LoyaltyCard with corresponding id in DB,
     *         null if card does not exist
     *
     * @throws MissingDAOParameterException if serialNumber is null
     * @throws InvalidDAOParameterException if serialNumber is of length > 10
     * ----------------------------------------------------------------------------------------------------------
     */
    LoyaltyCardModel getCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException;

    /** --------------------------------------------------------
     * This method removes all Loyalty_card entries from DB
     *
     * @return true if remove succeeds,
     *         false if remove fails
     *
     * ---------------------------------------------------------
     */
    boolean resetLoyaltyCards();
}
