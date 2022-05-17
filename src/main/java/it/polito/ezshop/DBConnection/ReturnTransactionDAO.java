package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ReturnTransactionModel;

import java.util.List;

public class ReturnTransactionDAO extends mysqlDAO implements ReturnTransactionDAOInterface {

    public ReturnTransactionDAO() {
        super();
    }

    @Override
    public Integer getMaxReturnTransactionId() {
        String query = "SELECT MAX(id) FROM return_transaction";
        List<String[]> result = db.executeQuery(query);
        //if (result.size() == 0)
        //    return null;
        String[] tuple = result.get(0);
        if (tuple[0] == null)
            return 0;
        return Integer.parseInt(tuple[0]);
    }

    @Override
    public ReturnTransactionModel addReturnTransaction(ReturnTransactionModel ret) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (ret == null)
            throw new MissingDAOParameterException("ReturnTransactionModel object is required" +
                    " in addReturnTransaction in ReturnTransactionDAO");
        Integer rid = ret.getID();
        String query;
        if (rid != null) {
            if (rid <= 0)
                throw new InvalidDAOParameterException("ReturnTransactionModel.ID must be > 0 when is not null" +
                        " in addReturnTransaction in ReturnTransactionDAO \n Given instead: " + rid);
            query = "INSERT INTO return_transaction VALUES(" + rid;
        } else query = "INSERT INTO return_transaction VALUES(null";
        Integer sale = ret.getTransactionID();
        if (sale == null)
            throw new MissingDAOParameterException("ReturnTransactionModel.transactionID is required" +
                    " in addReturnTransaction in ReturnTransactionDAO");
        if (sale <= 0)
            throw new InvalidDAOParameterException("ReturnTransactionModel.transactionID must be > 0" +
                    " in addReturnTransaction in ReturnTransactionDAO \n Given instead: " + sale);
        query = query + ","+ret.getReturnedValue()+"," + sale + ",null);";
        ReturnTransactionModel r = null;
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            int maxreturn = getMaxReturnTransactionId();
            opquery = (db.executeQuery("SELECT * FROM return_transaction WHERE id=" + maxreturn + ";")).get(0);
            if (opquery == null) return null;
            int id = Integer.parseInt(opquery[0]);
            Double value;
            if (opquery[1] == null)
                value = null;
            else
                value = Double.parseDouble(opquery[1]);
            int sid = Integer.parseInt(opquery[2]);
            r = new ReturnTransactionModel(id, value, sid, null);
        }
        return r;
    }

    @Override
    public boolean removeReturnTransaction(Integer returnID) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnID == null)
            throw new MissingDAOParameterException("returnID must be not null" +
                    " in removeReturnTransaction in ReturnTransactionDAO");
        if (returnID <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in removeReturnTransaction in ReturnTransactionDAO \n Given instead: " + returnID);
        String query = "DELETE FROM return_transaction WHERE id=" + returnID + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateReturnTransaction(ReturnTransactionModel transaction) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (transaction == null)
            throw new MissingDAOParameterException("ReturnTransactionModel object must be not null" +
                    " in updateReturnTransaction in ReturnTransactionDAO");
        Integer rid = transaction.getID();
        if (rid == null)
            throw new MissingDAOParameterException("ReturnTransactionModel.ID must be not null" +
                    " in updateReturnTransaction in ReturnTransactionDAO");
        if (rid <= 0)
            throw new InvalidDAOParameterException("ReturnTransactionModel.ID must be > 0" +
                    " in updateReturnTransaction in ReturnTransactionDAO \n Given instead: " + rid);
        Double returned_value = transaction.getReturnedValue();
        if (returned_value == null)
            throw new MissingDAOParameterException("ReturnTransactionModel.returnedValue is required" +
                    " in updateReturnTransaction in ReturnTransactionDAO");
        String query = "UPDATE return_transaction SET returned_value=" + returned_value + " WHERE id=" + rid + ";";
        return db.executeUpdate(query);
    }

    /*@Override
    public boolean setReturnBalanceOperation(Integer returnId, int balanceId) {
        if ( balanceId <= 0 )
            ;
        if ( returnId == null )
            ;
        if ( returnId <= 0 )
            ;
        if ( ( balanceId <= 0 ) || ( returnId == null ) || ( returnId <= 0 ) )
            return false;
        String query = "UPDATE return_transaction SET balance_operation=" + balanceId + " WHERE id=" + returnId + ";";
        return db.executeUpdate(query);
    }*/

    @Override
    public ReturnTransactionModel getReturnTransaction(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in getReturnTransaction in ReturnTransactionDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in getReturnTransaction in ReturnTransactionDAO \n Given instead: " + returnId);
        String query = "SELECT * FROM return_transaction WHERE id=" + returnId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        Integer rid = Integer.parseInt(tuple[0]);
        Double value;
        if (tuple[1] == null)
            value = null;
        else
            value = Double.parseDouble(tuple[1]);
        Integer sid = Integer.parseInt(tuple[2]);
        return new ReturnTransactionModel(rid, value, sid, null);
    }

    @Override
    public boolean setReturnBalanceOperation(Integer returnId, int balanceId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (balanceId <= 0)
            throw new MissingDAOParameterException("balanceId is required" +
                    " in setReturnBalanceOperation in ReturnTransactionDAO");
        if (returnId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in setReturnBalanceOperation in ReturnTransactionDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in setReturnBalanceOperation in ReturnTransactionDAO \n Given instead: " + returnId);
        String query = "UPDATE return_transaction SET balance_operation=" + balanceId + " WHERE id=" + returnId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public BalanceOperation getReturnBalanceOperation(Integer returnId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (returnId == null)
            throw new MissingDAOParameterException("returnId is required" +
                    " in getReturnBalanceOperation in ReturnTransactionDAO");
        if (returnId <= 0)
            throw new InvalidDAOParameterException("returnId must be > 0" +
                    " in getReturnBalanceOperation in ReturnTransactionDAO \n Given instead: " + returnId);
        String query = "SELECT balance_operation FROM return_transaction WHERE id=" + returnId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        if(tuple[0]==null)
            return null;
        int bid = Integer.parseInt(tuple[0]);
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        try {
            return bdao.getBalanceOperation(bid);
        } catch (MissingDAOParameterException e) {
            //e.printStackTrace();
            //return null;
        }
        return null;
    }

    @Override
    public boolean resetReturnTransactions() {
        String query = "DELETE FROM return_transaction;";
        return db.executeUpdate(query);
    }
}
