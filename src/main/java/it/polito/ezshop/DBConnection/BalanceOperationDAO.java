package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.BalanceOperationDAOInterface;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.BalanceOperationModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BalanceOperationDAO extends mysqlDAO implements BalanceOperationDAOInterface {

    public BalanceOperationDAO() {
        super();
    }

    @Override
    public BalanceOperation addBalanceOperation(BalanceOperation operation) throws MissingDAOParameterException {
        if (operation == null)
            throw new MissingDAOParameterException("BalanceOperation object is required " +
                    "in addBalanceOperation in BalanceOperationDAO");
        if (operation.getType() == null)
            throw new MissingDAOParameterException("BalanceOperation.Type is required " +
                    "in addBalanceOperation in BalanceOperationDAO");
        if (operation.getMoney() <= 0)
            throw new MissingDAOParameterException("BalanceOperation.Money is required " +
                    "in addBalanceOperation in BalanceOperationDAO");
        BalanceOperation op = null;
        String query = "INSERT INTO balance_operation VALUES(";
        query = query + "null, '" + operation.getType() + "', " + operation.getMoney() + ", sysdate()";
        query = query + ");";
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            opquery = (db.executeQuery("SELECT last_insert_id()")).get(0);
            if (opquery == null) return null;
            opquery = (db.executeQuery("SELECT * FROM balance_operation WHERE id=" + opquery[0] + ";")).get(0);
            if (opquery == null) return null;
            BalanceOperationModel.Type t = BalanceOperationModel.Type.valueOf(opquery[1]);
            op = new BalanceOperationModel(Integer.parseInt(opquery[0]),
                    LocalDate.parse(opquery[3].split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-d")),
                    Double.parseDouble(opquery[2]), t);
        }
        return op;
    }

    @Override
    public boolean removeBalanceOperation(int balanceId) throws MissingDAOParameterException {
        if (balanceId <= 0)
            throw new MissingDAOParameterException("BalanceOperation.BalanceId is required " +
                    "in removeBalanceOperation in BalanceOperationDAO");
        String query = "DELETE FROM balance_operation WHERE id=" + balanceId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateBalanceOperation(BalanceOperation operation) throws MissingDAOParameterException {
        if (operation == null)
            throw new MissingDAOParameterException("BalanceOperation object is required " +
                    "in updateBalanceOperation in BalanceOperationDAO");
        int opid = operation.getBalanceId();
        String type = operation.getType();
        double money = operation.getMoney();
        if (opid <= 0)
            throw new MissingDAOParameterException("BalanceOperation.Id is required " +
                    "in updateBalanceOperation in BalanceOperationDAO");
        if ((type == null) && (money <= 0))
            throw new MissingDAOParameterException("At least one parameter (BalanceOperation.Type or" +
                    " BalanceOperation.Money) is required in updateBalanceOperation in BalanceOperationDAO");
        String query = "";
        if (type != null)
            query = query + " type='" + type + "'";
        if (money > 0) {
            if (query.length() > 0)
                query = query + ", ";
            query = query + " amount=" + money;
        }
        query = "UPDATE balance_operation SET " + query + " WHERE id=" + opid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public List<BalanceOperation> getBalanceOperations() {
        String query = "SELECT * FROM balance_operation";
        List<String[]> result = db.executeQuery(query);
        List<BalanceOperation> ops = new ArrayList<>();
        BalanceOperation op;
        int id;
        double amount;
        String type;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate d;
        for (String[] tuple : result) {
            id = Integer.parseInt(tuple[0]);
            type = tuple[1];
            amount = Double.parseDouble(tuple[2]);
            d = LocalDate.parse(tuple[3].split(" ")[0], format);
            op = new BalanceOperationModel(id, d, amount, BalanceOperationModel.Type.valueOf(type));
            ops.add(op);
        }
        return ops;
    }

    public BalanceOperation getBalanceOperation(int balanceId) throws MissingDAOParameterException {
        if (balanceId <= 0)
            throw new MissingDAOParameterException("BalanceOperation.Id is required " +
                    "in getBalanceOperation in BalanceOperationDAO");
        String query = "SELECT * FROM balance_operation WHERE id=" + balanceId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-d");
        int opid = Integer.parseInt(tuple[0]);
        BalanceOperationModel.Type type = BalanceOperationModel.Type.valueOf(tuple[1]);
        double amount = Double.parseDouble(tuple[2]);
        LocalDate d = LocalDate.parse(tuple[3].split(" ")[0], format);
        return new BalanceOperationModel(opid, d, amount, type);
    }

    @Override
    public boolean resetBalanceOperations() {
        String query = "DELETE FROM balance_operation;";
        return db.executeUpdate(query);
    }
}
