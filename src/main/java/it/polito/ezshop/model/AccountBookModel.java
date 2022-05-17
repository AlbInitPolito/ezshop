package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.BalanceOperationDAO;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountBookModel {

    private Double balance;

    public AccountBookModel() {
        this.balance = 0.0;
    }

    /**
     * This method record a balance update. <toBeAdded> can be both positive and negative. If positive the balance entry
     * should be recorded as CREDIT, if negative as DEBIT. The final balance after this operation should always be
     * positive.
     * It can be invoked only after a user with role "Administrator", "ShopManager" is logged in.
     *
     * @param abm       the AccountBookModel instance to perform the operation on
     * @param toBeAdded the amount of money (positive or negative) to be added to the current balance. If this value
     *                  is >= 0 than it should be considered as a CREDIT, if it is < 0 as a DEBIT
     * @return true if the balance has been successfully updated
     * false if toBeAdded + currentBalance < 0.
     */
    public static boolean recordBalanceUpdate(AccountBookModel abm, double toBeAdded) {
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        BalanceOperation balanceOp;
        if (toBeAdded + abm.balance < 0)
            return false;
        if (toBeAdded < 0)
            balanceOp = new BalanceOperationModel(0, LocalDate.now(), -toBeAdded, BalanceOperationModel.Type.DEBIT);
        else
            balanceOp = new BalanceOperationModel(0, LocalDate.now(), toBeAdded, BalanceOperationModel.Type.CREDIT);

        try {
            bodao.addBalanceOperation(balanceOp);
            abm.balance += toBeAdded;
            return true;
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) {
        BalanceOperationDAO bodao = new BalanceOperationDAO();
        List<BalanceOperation> balanceOps = bodao.getBalanceOperations();
        List<BalanceOperation> result = new ArrayList<>();

        if (from == null && to == null)
            return balanceOps;

        if (from != null && to != null && from.isAfter(to)) {
            LocalDate tmp = to;
            to = from;
            from = tmp;
        }

        if (from != null && to != null) {
            for (BalanceOperation bo : balanceOps) {
                if (!(bo.getDate().isBefore(from) || bo.getDate().isAfter(to))) {
                    result.add(bo);
                }
            }
        } else if (from == null) {
            for (BalanceOperation bo : balanceOps) {
                if (!bo.getDate().isAfter(to))
                    result.add(bo);
            }
        } else {
            for (BalanceOperation bo : balanceOps) {
                if (!bo.getDate().isBefore(from))
                    result.add(bo);
            }
        }

        return result;
    }

    /**
     * This method is essentially a getter for the Double attribute balance.
     *
     * @return the updated balance stored in the account book
     */
    public static Double computeBalance(AccountBookModel abm) {
        List<BalanceOperation> balanceOps;
        BalanceOperationDAO bdao = new BalanceOperationDAO();
        balanceOps = bdao.getBalanceOperations();
        abm.balance = 0.0;
        for (BalanceOperation b : balanceOps) {
            if (b.getType().equals("DEBIT") || b.getType().equals("RETURN") || b.getType().equals("ORDER"))
                abm.balance -= b.getMoney();
            else
                abm.balance += b.getMoney();
        }
        return abm.balance;
    }
}
