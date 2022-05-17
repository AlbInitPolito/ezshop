package it.polito.ezshop.integrationTests.step4;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.model.BalanceOperationModel;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AccountBookModelDriversStep4 {

    DbConnection dbConnection = DbConnection.getInstance();
    String query = "DELETE FROM balance_operation";
    Double balance;

    @Test
    public void testRecordBalanceUpdateDriver(){
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate(query);
        BalanceOperation balanceOp1 = new BalanceOperationModel(0, LocalDate.now(), 10000, BalanceOperationModel.Type.CREDIT);
        BalanceOperation balanceOp2 = new BalanceOperationModel(1, LocalDate.now(), 5000, BalanceOperationModel.Type.DEBIT);

        try{
            BalanceOperation result = bodao.addBalanceOperation(balanceOp1);
            assertNotNull(result);
            assert(result.getDate().equals(LocalDate.now()));
            assert(result.getMoney() == 10000);
            assert(result.getType().equals(BalanceOperationModel.Type.CREDIT.name()));
            result = bodao.addBalanceOperation(balanceOp2);
            assertNotNull(result);
            assert(result.getDate().equals(LocalDate.now()));
            assert(result.getMoney() == 5000);
            assert(result.getType().equals(BalanceOperationModel.Type.DEBIT.name()));
        } catch(Exception e){
            e.printStackTrace();
            fail();
        }
        dbConnection.executeUpdate(query);
    }

    @Test
    public void testGetCreditsAndDebitsDriver(){
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate(query);
        LocalDate from = null;
        LocalDate to = LocalDate.now();
        List<BalanceOperation> balanceOps = bodao.getBalanceOperations();
        assert(balanceOps.size() == 0);
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (123, 'CREDIT', 10000, '2020-12-19 19:38:14');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (124, 'DEBIT', 5000, '2021-01-05 11:27:57');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (125, 'DEBIT', 2500, '2021-01-05 13:00:09');");
        balanceOps = bodao.getBalanceOperations();
        assert(balanceOps.size() == 3);

        dbConnection.executeUpdate(query);
    }

    @Test
    public void testComputeBalanceDriver(){
        dbConnection.executeUpdate(query);
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (123, 'CREDIT', 10000, '2020-12-19 19:38:14');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (124, 'DEBIT', 5000, '2021-01-05 11:27:57');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (125, 'ORDER', 2500, '2021-01-05 13:00:09');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (126, 'RETURN', 400, '2021-01-06 08:10:03');");
        dbConnection.executeUpdate("INSERT INTO ezshopdb.balance_operation VALUES (127, 'SALE', 2000, '2021-01-06 09:00:09');");
        List<BalanceOperation> balanceOps = bodao.getBalanceOperations();
        assert(balanceOps.size() == 5);
        balance = 0.0;

        for (BalanceOperation b : balanceOps) {
            if (b.getType().equals("DEBIT") || b.getType().equals("RETURN") || b.getType().equals("ORDER"))
                balance -= b.getMoney();
            else
                balance += b.getMoney();
        }
        assert(balance == 4100);
        dbConnection.executeUpdate(query);
    }

}
