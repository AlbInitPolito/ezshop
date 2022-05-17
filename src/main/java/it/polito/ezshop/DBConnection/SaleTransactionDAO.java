package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.SaleTransactionModel;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionDAO extends mysqlDAO implements SaleTransactionDAOInterface {

    public SaleTransactionDAO() {
        super();
    }

    @Override
    public Integer getMaxSaleTransactionId() {
        String query = "SELECT MAX(id) FROM sale_transaction";
        List<String[]> result = db.executeQuery(query);
        //if (result.size() == 0)
        //    return null;
        String[] tuple = result.get(0);
        if (tuple[0] == null)
            return 0;
        return Integer.parseInt(tuple[0]);
    }

    @Override
    public SaleTransaction addSaleTransaction(SaleTransaction sale) throws MissingDAOParameterException, InvalidDAOParameterException {
        String query;
        if (sale == null)
            query = "INSERT INTO sale_transaction VALUES(null, null, null, null, sysdate(), null);";
        else {
            Integer sid = sale.getTicketNumber();
            if (sid == null)
                throw new MissingDAOParameterException("SaleTransaction.ID is required if SaleTransaction object is not null" +
                        " in addSaleTransaction in SaleTransactionDAO");
            if (sid <= 0)
                throw new InvalidDAOParameterException("SaleTransaction.ID must be > 0 if SaleTransaction object is not null" +
                        " in addSaleTransaction in SaleTransactionDAO \n Given instead: " + sid);
            query = "INSERT INTO sale_transaction VALUES(" + sid;
            double cost = sale.getPrice();
            Integer type = sale.getPaymentType();
            double discount = sale.getDiscountRate();
            query = query + "," + cost;
            if (type != null) {
                if (type < 0)
                    throw new InvalidDAOParameterException("SaleTransaction.paymentType must be >= 0 when is not null" +
                            " in addSaleTransaction in SaleTransactionDAO \n Given instead: " + type);
                query = query + "," + type;
            } else
                query = query + ",null";
            if (discount > 0) {
                query = query + "," + discount;
            } else
                query = query + ",0.0";
            query = query + ", sysdate(), null);";
        }
        SaleTransaction s = null;
        boolean result = db.executeUpdate(query);
        String[] opquery;
        if (result) {
            int maxsale = getMaxSaleTransactionId();
            opquery = (db.executeQuery("SELECT * FROM sale_transaction WHERE id=" + maxsale + ";")).get(0);
            if (opquery == null) return null;
            int id = Integer.parseInt(opquery[0]);
            double cost;
            Integer type;
            double discount;
            java.util.Date da;
            Time ti;
            if (opquery[1] == null)
                cost = 0.0;
            else
                cost = Double.parseDouble(opquery[1]);
            if (opquery[2] == null)
                type = null;
            else
                type = Integer.parseInt(opquery[2]);
            if (opquery[3] == null)
                discount = 0.0;
            else
                discount = Double.parseDouble(opquery[3]);
            if (opquery[4] == null) {
                da = null;
                ti = null;
            } else {
                LocalDateTime d = LocalDateTime.parse(opquery[4], DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"));
                da = Date.valueOf(d.toLocalDate());
                ti = Time.valueOf(d.toLocalTime());
            }
            s = new SaleTransactionModel(id, type, cost, discount, da, ti, null);
        }
        return s;
    }

    @Override
    public boolean removeSaleTransaction(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in removeSaleTransaction in SaleTransactionDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in removeSaleTransaction in SaleTransactionDAO \n Given instead: " + saleId);
        String query = "DELETE FROM sale_transaction WHERE id=" + saleId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public boolean updateSaleTransaction(SaleTransaction sale) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (sale == null)
            throw new MissingDAOParameterException("SaleTransaction object is required" +
                    " in updateSaleTransaction in SaleTransactionDAO");
        Integer sid = sale.getTicketNumber();
        if (sid == null)
            throw new MissingDAOParameterException("SaleTransaction.ID is required" +
                    " in updateSaleTransaction in SaleTransactionDAO");
        if (sid <= 0)
            throw new InvalidDAOParameterException("SaleTransaction.ID must be > 0" +
                    " in updateSaleTransaction in SaleTransactionDAO \n Given instead: " + sid);
        double cost = sale.getPrice();
        Integer type = sale.getPaymentType();
        double discount = sale.getDiscountRate();
        if ((type == null) && (discount < 0) && (sale.getDate() == null) && (cost <= 0))
            throw new MissingDAOParameterException("At least one parameter ( SaleTransaction.paymentType " +
                    "or SaleTransaction.discountRate or SaleTransaction.date or SaleTransaction.price is required" +
                    " in updateSaleTransaction in SaleTransactionDAO");
        Date date;
        Time time;
        LocalDateTime dateTime = null;
        if (sale.getDate() != null) {
            date = new Date(sale.getDate().getTime());
            if (sale.getTime() != null)
                time = sale.getTime();
            else
                throw new MissingDAOParameterException("SaleTransaction.time is required if SaleTransaction.date is not null" +
                        " in updateSaleTransaction in SaleTransactionDAO");
            LocalDate ldate = date.toLocalDate();
            LocalTime ltime = time.toLocalTime();
            dateTime = ldate.atTime(ltime);
        }
        String query = "";
        if (cost > 0)
            query = query + " cost=" + cost;
        if (type != null) {
            if (type < 0)
                throw new InvalidDAOParameterException("SaleTransaction.paymentType must be > 0" +
                        " in updateSaleTransaction in SaleTransactionDAO \n Given instead: " + type);
            if (query.length() > 0)
                query = query + ", ";
            query = query + "payment_type=" + type;
        }
        if (discount >= 0) {
            if (query.length() > 0)
                query = query + ", ";
            query = query + "discount_rate=" + discount;
        }
        if (dateTime != null) {
            if (query.length() > 0)
                query = query + ", ";
            String d = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"));
            query = query + "date_time='" + d + "'";
        }
        query = "UPDATE sale_transaction SET " + query + " WHERE id=" + sid + ";";
        return db.executeUpdate(query);
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (saleId == null)
            throw new MissingDAOParameterException("SaleTransaction.ID is required" +
                    " in getSaleTransaction in SaleTransactionDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("SaleTransaction.ID must be > 0" +
                    " in getSaleTransaction in SaleTransactionDAO \n Given instead: " + saleId);
        String query = "SELECT * FROM sale_transaction WHERE id=" + saleId + ";";
        List<String[]> result = db.executeQuery(query);
        if (result.size() == 0)
            return null;
        String[] tuple = result.get(0);
        int id = Integer.parseInt(tuple[0]);
        double cost;
        Integer type;
        double discount;
        java.util.Date da;
        Time ti;
        if (tuple[1] == null)
            cost = 0.0;
        else
            cost = Double.parseDouble(tuple[1]);
        if (tuple[2] == null)
            type = null;
        else
            type = Integer.parseInt(tuple[2]);
        if (tuple[3] == null)
            discount = 0.0;
        else
            discount = Double.parseDouble(tuple[3]);
        if (tuple[4] == null) {
            da = null;
            ti = null;
        } else {
            LocalDateTime d = LocalDateTime.parse(tuple[4], DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"));
            da = Date.valueOf(d.toLocalDate());
            ti = Time.valueOf(d.toLocalTime());
        }
        TicketEntryDAO tdao = new TicketEntryDAO();
        List<TicketEntry> prods = tdao.getSaleTicketEntries(saleId);
        return new SaleTransactionModel(id, type, cost, discount, da, ti, prods);
    }

    @Override
    public List<SaleTransaction> getSaleTransactions() {
        String query = "SELECT * FROM sale_transaction";
        List<String[]> result = db.executeQuery(query);
        List<SaleTransaction> sales = new ArrayList<>();
        int id;
        double cost;
        Integer type;
        double discount;
        LocalDateTime d;
        java.util.Date da;
        Time ti;
        for (String[] tuple : result) {
            id = Integer.parseInt(tuple[0]);
            if (tuple[1] == null)
                cost = 0.0;
            else
                cost = Double.parseDouble(tuple[1]);
            if (tuple[2] == null)
                type = null;
            else
                type = Integer.parseInt(tuple[2]);
            if (tuple[3] == null)
                discount = 0.0;
            else
                discount = Double.parseDouble(tuple[3]);
            if (tuple[4] == null) {
                da = null;
                ti = null;
            } else {
                d = LocalDateTime.parse(tuple[4], DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"));
                da = Date.valueOf(d.toLocalDate());
                ti = Time.valueOf(d.toLocalTime());
            }
            TicketEntryDAO tdao = new TicketEntryDAO();
            List<TicketEntry> prods = null;
            try {
                prods = tdao.getSaleTicketEntries(id);
            } catch (InvalidDAOParameterException | MissingDAOParameterException e) {
                //e.printStackTrace();
                //continue;
            }
            sales.add(new SaleTransactionModel(id, type, cost, discount, da, ti, prods));
        }
        return sales;
    }

    @Override
    public boolean setSaleBalanceOperation(Integer saleId, int balanceId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (balanceId <= 0)
            throw new MissingDAOParameterException("balanceId is required" +
                    " in setSaleBalanceOperation in SaleTransactionDAO");
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in setSaleBalanceOperation in SaleTransactionDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in setSaleBalanceOperation in SaleTransactionDAO \n Given instead: " + saleId);
        String query = "UPDATE sale_transaction SET balance_operation=" + balanceId + " WHERE id=" + saleId + ";";
        return db.executeUpdate(query);
    }

    @Override
    public BalanceOperation getSaleBalanceOperation(Integer saleId) throws MissingDAOParameterException, InvalidDAOParameterException {
        if (saleId == null)
            throw new MissingDAOParameterException("saleId is required" +
                    " in getSaleBalanceOperation in SaleTransactionDAO");
        if (saleId <= 0)
            throw new InvalidDAOParameterException("saleId must be > 0" +
                    " in getSaleBalanceOperation in SaleTransactionDAO \n Given instead: " + saleId);
        String query = "SELECT balance_operation FROM sale_transaction WHERE id=" + saleId + ";";
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
    public boolean resetSaleTransactions() {
        String query = "DELETE FROM sale_transaction;";
        return db.executeUpdate(query);
    }
}
