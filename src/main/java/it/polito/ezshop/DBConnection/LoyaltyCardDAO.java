package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.LoyaltyCardDAOInterface;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.LoyaltyCardModel;

import java.util.List;

public class LoyaltyCardDAO extends mysqlDAO implements LoyaltyCardDAOInterface {

    public LoyaltyCardDAO() {
        super();
    }

    @Override
    public boolean addCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (serialNumber == null)
            throw new MissingDAOParameterException("serialNumber is required " +
                    "in addCard in LoyaltyCardDAO");
        if (serialNumber.length() > 10)
            throw new InvalidDAOParameterException("serialNumber cannot be of length > 10" +
                    " in addCard in LoyaltyCardDAO \n Given instead: " + serialNumber);
        String query = "INSERT INTO loyalty_card VALUES('" + serialNumber + "', 0)";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateCard(LoyaltyCardModel card) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (card == null)
            throw new MissingDAOParameterException("LoyaltyCardModel object is required " +
                    "in updateCard in LoyaltyCardDAO");
        String serial = card.getSerialNumber();
        if (serial == null)
            throw new MissingDAOParameterException("LoyaltyCardModel.serialNumber is required " +
                    "in updateCard in LoyaltyCardDAO");
        if (serial.length() != 10)
            throw new InvalidDAOParameterException("LoyaltyCardModel.serialNumber cannot be of length > 10" +
                    " in updateCard in LoyaltyCardDAO \n Given instead: " + serial);
        Integer points = card.getPoints();
        if (points == null)
            throw new MissingDAOParameterException("LoyaltyCardModel.points is required " +
                    "in updateCard in LoyaltyCardDAO");
        if (points < 0)
            throw new InvalidDAOParameterException("LoyaltyCardModel.points cannot be < 0" +
                    " in updateCard in LoyaltyCardDAO \n Given instead: " + points);
        String query = "UPDATE loyalty_card SET points=" + points + " WHERE serial_number='" + serial + "';";
        return db.executeUpdate(query);
    }

    @Override
    public LoyaltyCardModel getCard(String serialNumber) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (serialNumber == null)
            throw new MissingDAOParameterException("serialNumber is required " +
                    "in getCard in LoyaltyCardDAO");
        if (serialNumber.length() > 10)
            throw new InvalidDAOParameterException("serialNumber cannot be of length > 10" +
                    " in getCard in LoyaltyCardDAO \n Given instead: " + serialNumber);
        String query = "SELECT * FROM loyalty_card WHERE serial_number='" + serialNumber + "';";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        return new LoyaltyCardModel(Integer.parseInt(tuple[1]), tuple[0]);
    }

    @Override
    public boolean resetLoyaltyCards() {
        String query = "DELETE FROM loyalty_card;";
        return db.executeUpdate(query);
    }
}
