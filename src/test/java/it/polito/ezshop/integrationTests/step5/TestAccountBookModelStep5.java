package it.polito.ezshop.integrationTests.step5;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.model.AccountBookModel;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TestAccountBookModelStep5 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query = "DELETE FROM balance_operation";
    AccountBookModel abm = new AccountBookModel();

    @Test
    public void testRecordBalanceUpdate(){
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate(query);
        try{
            assertFalse(AccountBookModel.recordBalanceUpdate(abm, -1));
            assertTrue(AccountBookModel.recordBalanceUpdate(abm, 10000));
            assertTrue(AccountBookModel.recordBalanceUpdate(abm, -1000));
            Double balance = 0.0;
            List<BalanceOperation> balanceOps = bodao.getBalanceOperations();
            for (BalanceOperation b : balanceOps) {
                if (b.getType().equals("DEBIT") || b.getType().equals("RETURN") || b.getType().equals("ORDER"))
                    balance -= b.getMoney();
                else
                    balance += b.getMoney();
            }
            assert(balance == 9000);
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetCreditsAndDebits(){
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate("DELETE from product_order");
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (123, 'CREDIT', 10000, '2020-12-19 19:38:14');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (124, 'DEBIT', 5000, '2021-01-05 11:27:57');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (125, 'ORDER', 2500, '2021-01-06 13:00:09');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (126, 'RETURN', 400, '2021-01-07 08:10:03');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (127, 'SALE', 2000, '2021-01-06 09:00:09');");
        assert(AccountBookModel.getCreditsAndDebits(null, null).size() == 5);
        assert(AccountBookModel.getCreditsAndDebits(LocalDate.parse("2020-12-19"), LocalDate.parse("2021-01-06")).size() == 4);
        assert(AccountBookModel.getCreditsAndDebits(LocalDate.parse("2021-01-06"), LocalDate.parse("2020-12-19")).size() == 4);
        assert(AccountBookModel.getCreditsAndDebits(null, LocalDate.parse("2021-01-05")).size() == 2);
        assert(AccountBookModel.getCreditsAndDebits(LocalDate.parse("2021-01-06"), null).size() == 3);

    }

    @Test
    public void testComputeBalance(){
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate(query);
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (123, 'CREDIT', 10000, '2020-12-19 19:38:14');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (124, 'DEBIT', 5000, '2021-01-05 11:27:57');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (125, 'ORDER', 2500, '2021-01-05 13:00:09');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (126, 'RETURN', 400, '2021-01-06 08:10:03');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (127, 'SALE', 2000, '2021-01-06 09:00:09');");
        assert(AccountBookModel.computeBalance(abm) == 4100);
        dbConnection.executeUpdate(query);
    }
}
