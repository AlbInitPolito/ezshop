package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

import java.util.ArrayList;
import java.util.List;

public class TicketEntryModel implements it.polito.ezshop.data.TicketEntry {

    private String barCode;
    private String productDescription;
    private int amount;
    private double discountRate;
    private double pricePerUnit;
    private List<String> RFIDs;

    public TicketEntryModel(String barCode, String productDescription, int amount, double discountRate, double pricePerUnit) {
        this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.discountRate = discountRate;
        this.pricePerUnit = pricePerUnit;
        this.RFIDs = new ArrayList<>();
    }

    public TicketEntryModel() {

    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public List<String> getRFIDs() { return RFIDs; }

    public boolean contains(String RFID) {return RFIDs.contains(RFID);}

    public void addRFID(String RFID) {
        if(this.RFIDs == null)
            this.RFIDs = new ArrayList<>();
        RFIDs.add(RFID);
        amount++;
    }

    public void removeRFID(String RFID) {
        RFIDs.remove(RFID);
        amount--;
    }
}
