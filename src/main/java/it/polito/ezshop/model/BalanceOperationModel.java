package it.polito.ezshop.model;

import java.time.LocalDate;

public class BalanceOperationModel implements it.polito.ezshop.data.BalanceOperation {

    public enum Type {
        CREDIT,
        DEBIT,
        RETURN,
        SALE,
        ORDER
    }

    private int balanceID;
    private LocalDate date;
    private double amount;
    private Type type;

    public BalanceOperationModel(int id, LocalDate date, double amount, Type type) {
        this.balanceID = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public int getBalanceId() {
        return this.balanceID;
    }

    @Override
    public void setBalanceId(int balanceId) {
        this.balanceID = balanceId;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public double getMoney() {
        return this.amount;
    }

    @Override
    public void setMoney(double money) {
        this.amount = money;
    }

    @Override
    public String getType() {
        if (this.type != null)
            return this.type.name();
        else
            return null;
    }

    /**
     * This method sets the type of the BalanceOperation to the specified parameter
     *
     * @param type is a String stating the type of the operation. The method is case-insensitive, as it converts the parameter
     *             to uppercase in order to refer to the correct enum value
     */
    @Override
    public void setType(String type) {
        if (type != null)
            this.type = Type.valueOf(type.toUpperCase());
        else
            this.type = null;
    }

}
