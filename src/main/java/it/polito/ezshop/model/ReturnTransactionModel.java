package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnTransactionModel {
    private Integer ID;
    private Double returnedValue;
    private Integer transactionID;
    private List<TicketEntry> entries;

    public ReturnTransactionModel() {
        this.ID = null;
        this.returnedValue = 0.0;
        this.transactionID = null;
        this.entries = new ArrayList<>();
    }

    public ReturnTransactionModel(Integer ID, Double returnedValue, Integer transactionID, List<TicketEntry> entries) {
        this.ID = ID;
        this.returnedValue = returnedValue;
        this.transactionID = transactionID;
        this.entries = entries;
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Double getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(Double returnedValue) {
        this.returnedValue = returnedValue;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public List<TicketEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    public Integer startReturnTransaction(Integer saleNumber) throws InvalidTransactionIdException {
        //throw exception
        if (saleNumber == null || saleNumber <= 0)
            throw new InvalidTransactionIdException();

        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        try {
            sale = saleDAO.getSaleTransaction(saleNumber);
        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! Problem with DB");
            return -1;
        }
        //return -1 if the transaction does not exist
        if (sale == null) {
            System.out.print("Error! The Sale Transaction does not exist");
            return -1;
        }

        //return -1 if there is a problem with db
        if (returnDAO.getMaxReturnTransactionId() == null) {
            System.out.print("Error with the loading of the ID");
            return -1;
        }

        this.setID(returnDAO.getMaxReturnTransactionId() + 1);
        this.setTransactionID(saleNumber);
        return this.getTransactionID();
    }

    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        //throw exceptions
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0)
            throw new InvalidQuantityException();

        if (this.getID() == null || !this.getID().equals(returnId)) {
            System.out.print("Error! The return Transaction is not open");
            return false;
        }
        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        ProductType obj;
        ProductTypeDAOInterface productDAO = new ProductTypeDAO();
        try {
            obj = productDAO.getProductByBarcode(productCode);
            sale = saleDAO.getSaleTransaction(this.getTransactionID());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error with DB");
            return false;
        }

        //return false if the product does not exist
        if (obj == null) {
            System.out.print("Error! The product does not exist");
            return false;
        }
        //return false if the Sale transaction does not exist
        if (sale == null) {
            System.out.print("Error! The Sale transaction does not exist");
            return false;
        }
        //return false if the sale transaction does not contain the product
        int index = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(productCode);
        if (index == -1) {
            System.out.print("Error! the sale transaction does not contain the product");
            return false;
        }
        //return false if the amount is too much
        TicketEntry ticket = sale.getEntries().get(index);
        if (ticket.getAmount() < amount) {
            System.out.print("Error! The quantity is too much");
            return false;
        }

        //add the product in the transaction
        ticket.setAmount(amount);
        entries.add(ticket);
        return true;
    }

    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException {
        //throw exception
        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }

        //return false if the returnId not correspond to a returnTransaction open
        if (this.getID() == null || !this.getID().equals(returnId)) {
            System.out.print("Error! the returnTransaction is not open");
            return false;
        }
        //reset the returnTransaction
        if (!commit) {
            this.ID = null;
            this.returnedValue = 0.0;
            this.transactionID = null;
            this.entries = new ArrayList<>();
            return true;
        }

        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
        ProductTypeDAOInterface productDAO = new ProductTypeDAO();
        TicketEntryDAO ticketDAO = new TicketEntryDAO();
        TicketEntryRfidDAO ticketEntryRfidDAO = new TicketEntryRfidDAO();
        ReturnProductRfidDAO returnProductRfidDAO = new ReturnProductRfidDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();

        double total = 0;

        try {
            //add returnTransaction to DB
            this.setID(returnDAO.addReturnTransaction(this).getID());
            SaleTransaction sale = saleDAO.getSaleTransaction(this.getTransactionID());
            ProductType product;
            //Update Sale Transaction and Product Type
            // ----------------------old version ----------------------------------------
            //List<String> list = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList());
            // --------------------------------------------------------------------------

            List<TicketEntry> list = sale.getEntries();

            if (list.size() == 0) {
                /**
                 * comportamento nuovo
                 */
                double toBeRemoved = 0.0;
                List<String> listRFID = returnProductRfidDAO.getReturnProductRfids(returnId);
                for (String RFID : listRFID) {
                    productEntryDAO.updateProductEntry(RFID, true);
                    ticketEntryRfidDAO.removeTicketEntryRfid(RFID, sale.getTicketNumber());
                    String barcode = productEntryDAO.getProductEntryBarcode(RFID);
                    ProductType prod = productTypeDAO.getProductByBarcode(barcode);
                    toBeRemoved += ((ProductTypeModel) prod).getPricePerUnit() * (1 - ((ProductTypeModel) prod).getDiscountRate());
                    returnProductRfidDAO.addReturnProductRfid(returnId, RFID);
                }
                for (TicketEntry t : this.getEntries()) {
                    ProductType tmpProduct = productDAO.getProductByBarcode(t.getBarCode());
                    productDAO.updateProductType(new ProductTypeModel(tmpProduct.getId(), tmpProduct.getBarCode(), null, tmpProduct.getPricePerUnit(), tmpProduct.getQuantity() + t.getAmount(),
                            0.0, null, null, null, null));
                }
                toBeRemoved= toBeRemoved * (1 - sale.getDiscountRate());
                total = sale.getPrice() - toBeRemoved;
                this.setReturnedValue(toBeRemoved);
            } else {
                /**
                 * comportamento vecchio
                 */
                List<String> listBarcode = list.stream().map(TicketEntry::getBarCode).collect(Collectors.toList());

                for (TicketEntry ticket : entries) {
                    int index = listBarcode.indexOf(ticket.getBarCode());
                    //get out from the sale the amount of product for calculate the total
                    sale.getEntries().get(index).setAmount(sale.getEntries().get(index).getAmount() - ticket.getAmount());
                    //update the amount of product in table product_in_transaction
                    ticketDAO.updateTicketEntry(new TicketEntryModel(ticket.getBarCode(), null, sale.getEntries().get(index).getAmount(), 0.0, sale.getEntries().get(index).getPricePerUnit()), this.getTransactionID());
                    product = productDAO.getProductByBarcode(ticket.getBarCode());
                    productDAO.updateProductType(new ProductTypeModel(product.getId(), product.getBarCode(), null, product.getPricePerUnit(), product.getQuantity() + ticket.getAmount(), 0.0, null, null, null, null));
                    ReturnProductDAOInterface returnPDAO = new ReturnProductDAO();
                    returnPDAO.addReturnProduct(ticket, returnId);
                }

                for (TicketEntry element : sale.getEntries()) {
                    total = total + (element.getAmount() * (element.getPricePerUnit() * (1 - element.getDiscountRate())));
                }
                total = total * (1 - sale.getDiscountRate());
                //set the value to return
                this.setReturnedValue(sale.getPrice() - total);
            }

            // ------------------------old version--------------------------------------------------
            /*
            for (TicketEntry ticket : entries) {
                int index = list.indexOf(ticket.getBarCode());
                //get out from the sale the amount of product for calculate the total
                sale.getEntries().get(index).setAmount(sale.getEntries().get(index).getAmount() - ticket.getAmount());
                //update the amount of product in table product_in_transaction
                ticketDAO.updateTicketEntry(new TicketEntryModel(ticket.getBarCode(), null, sale.getEntries().get(index).getAmount(), 0.0, sale.getEntries().get(index).getPricePerUnit()), this.getTransactionID());
                product = productDAO.getProductByBarcode(ticket.getBarCode());
                productDAO.updateProductType(new ProductTypeModel(product.getId(), product.getBarCode(), null, product.getPricePerUnit(), product.getQuantity() + ticket.getAmount(), 0.0, null, null, null, null));
                ReturnProductDAOInterface returnPDAO = new ReturnProductDAO();
                returnPDAO.addReturnProduct(ticket, returnId);
            }

            //recalculate the total
            double total = 0;
            for (TicketEntry element : sale.getEntries()) {
                total = total + (element.getAmount() * (element.getPricePerUnit() * (1 - element.getDiscountRate())));
            }
            total = total * (1 - sale.getDiscountRate());
            //set the value to return
            this.setReturnedValue(sale.getPrice() - total);
            */
            // --------------------------------------------------------------------------------------

            this.setTransactionID(null);
            returnDAO.updateReturnTransaction(this);
            //update the SAle with the new total
            saleDAO.updateSaleTransaction(new SaleTransactionModel(sale.getTicketNumber(), null, total, 0, null, null, sale.getEntries()));
            this.ID = null;
            this.returnedValue = 0.0;
            this.transactionID = null;
            this.entries = new ArrayList<>();
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! Problem with DB");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException {
        //throw exception
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        //return false is the transaction does not exist
        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        ReturnProductRfidDAOInterface returnDAORFID = new ReturnProductRfidDAO();
        ReturnTransactionModel ret;
        try {
            ret = returnDAO.getReturnTransaction(returnId);

            //if the transaction is not close return false
            if (ret == null) {
                if (this.getID() == null || !this.getID().equals(returnId)) {
                    System.out.print("Error! Transaction does not exist");
                } else {
                    System.out.print("Error! The transaction is still open");
                }
                return false;
            }
            if (returnDAO.getReturnBalanceOperation(ret.getID()) != null) {
                System.out.print("Error! Transaction already payed");
                return false;
            }
            SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
            List<String> listRFID = returnDAORFID.getReturnProductRfids(returnId);
            double total = 0;
            SaleTransaction sale = saleDAO.getSaleTransaction(ret.getTransactionID());
            if(!listRFID.isEmpty()) {
                ProductEntryDAOInterface productEntryDAO = new ProductEntryDAO();
                TicketEntryRfidDAOInterface ticketRFIDDAO = new TicketEntryRfidDAO();
                ProductTypeDAOInterface productDAO = new ProductTypeDAO();
                double toAdd=0;
                for ( String element: listRFID) {
                    //reset product to not feasible
                    productEntryDAO.updateProductEntry(element, false);
                    //add the ticket of the sale
                    ticketRFIDDAO.addTicketEntryRfid(element,ret.getTransactionID());
                    String barcode = productEntryDAO.getProductEntryBarcode(element);
                    ProductType tmpProduct = productDAO.getProductByBarcode(barcode);
                    //amount to add to the sale price
                    toAdd += tmpProduct.getPricePerUnit();
                    //eliminate the product from the quantity of the product
                    productDAO.updateProductType(new ProductTypeModel(tmpProduct.getId(), tmpProduct.getBarCode(), null, tmpProduct.getPricePerUnit(), tmpProduct.getQuantity() - 1,
                            0.0, null, null, null, null));
                    ReturnProductRfidDAOInterface returnProductRFIDDAO = new ReturnProductRfidDAO();
                    //eliminate the ticket of the return
                    returnProductRFIDDAO.removeReturnProductRfid(returnId,element);
                }
                //remove the transaction
                returnDAO.removeReturnTransaction(returnId);
                // totale return transaction
                    toAdd = toAdd *(1-sale.getDiscountRate());
                    total = sale.getPrice() + toAdd;
            } else {
                //take the saleTransaction bonded with the returnTransaction
                ProductTypeDAOInterface productDAO = new ProductTypeDAO();
                TicketEntryDAOInterface ticketDAO = new TicketEntryDAO();
                ReturnProductDAO retpDAO = new ReturnProductDAO();
                ProductType product;
                //make a list of barcode of the entrance of sale Transaction
                List<String> list = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList());
                ret.setEntries(retpDAO.getReturnProducts(ret.getID()));
                for (TicketEntry ticket : ret.getEntries()) {
                    //index of the product in the entries of the sale
                    int index = list.indexOf(ticket.getBarCode());
                    //modify the entrance of the sale for the computation of the totalAmount
                    sale.getEntries().get(index).setAmount(sale.getEntries().get(index).getAmount() + ticket.getAmount());
                    //modify the ticketEntry in the table product_in_transaction for the sale transaction
                    ticketDAO.updateTicketEntry(new TicketEntryModel(ticket.getBarCode(), null, sale.getEntries().get(index).getAmount(), -1, -1), ret.getTransactionID());
                    product = productDAO.getProductByBarcode(ticket.getBarCode());
                    //modify the quantity in ProductType
                    productDAO.updateProductType(new ProductTypeModel(product.getId(), product.getBarCode(), null, null, product.getQuantity() - ticket.getAmount(), null, null, null, null, null));
                    //remove the ticketEntry in product_in_transaction for the returnTransaction
                    ReturnProductDAOInterface returnproductDAO = new ReturnProductDAO();
                    returnproductDAO.removeReturnProduct(ticket.getBarCode(), returnId);
                }
                //remove the transaction
                returnDAO.removeReturnTransaction(returnId);
                //compute the total
                double discount;

                for (TicketEntry obj : sale.getEntries()) {
                    if (obj.getDiscountRate() != 0.0)
                        discount = obj.getDiscountRate();
                    else
                        discount = ((ProductTypeModel) productDAO.getProductByBarcode(obj.getBarCode())).getDiscountRate();
                    total = total + (obj.getAmount() * (obj.getPricePerUnit() * (1 - discount)));
                }

                total = total * (1 - sale.getDiscountRate());

            }
            saleDAO.updateSaleTransaction(new SaleTransactionModel(sale.getTicketNumber(), null, total, 0, null, null, sale.getEntries()));

        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! Problem with DB");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException {
        //throw exception
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        //return -1 if the transaction is still open
        if (this.getID() != null && this.getID().equals(returnId)) {
            System.out.print("Error! Transaction still open");
            return -1;
        }

        //return -1 if the transaction does not exist
        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        ReturnTransactionModel ret;
        try {
            ret = returnDAO.getReturnTransaction(returnId);
            if (ret == null) {
                System.out.print("Error! The transaction does not exist");
                return -1;
            }

            //save the transaction in balance
            BalanceOperationDAO operation = new BalanceOperationDAO();
            BalanceOperation balance = operation.addBalanceOperation(new BalanceOperationModel(0, null, ret.getReturnedValue(), BalanceOperationModel.Type.RETURN));
            returnDAO.setReturnBalanceOperation(ret.getID(), balance.getBalanceId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! Problem with DB");
            return -1;
        }

        return ret.getReturnedValue();
    }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        //throw exception
        if (creditCard == null || creditCard.equals("") || !CreditCardModel.luhnAlgorithm(creditCard))
            throw new InvalidCreditCardException();
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();


        //return -1 if the transaction is still open
        if (this.getID() != null && this.getID().equals(returnId)) {
            System.out.print("Error! Transaction still open");
            return -1;
        }

        //todo path credit card
        List<CreditCardModel> listCard = CreditCardModel.loadCreditCardsFromFile("files\\creditcardsFORTESTING.txt");
        //return false if there is problem with the file of the cards
        if (listCard == null) {
            System.out.print("Error! Problem with credit card");
            return -1;
        }
        //return -1 if the credit card is not registered
        int index = listCard.stream().map(CreditCardModel::getCreditCardNumber).collect(Collectors.toList()).indexOf(creditCard);
        if (index == -1) {
            System.out.print("Error! Credit card not registered");
            return -1;
        }


        ReturnTransactionDAOInterface returnDAO = new ReturnTransactionDAO();
        ReturnTransactionModel ret;
        try {
            ret = returnDAO.getReturnTransaction(returnId);
            //return -1 if the transaction does not exist
            if (ret == null) {
                System.out.print("Error! The transaction does not exist");
                return -1;
            }

            //return -1 if the creditCard has no enough money
            CreditCardModel card = listCard.get(index);
            if (!card.hasEnoughMoney(ret.getReturnedValue())) {
                System.out.print("Error! Credit card has not enough money");
                return -1;
            }

            // Update the credit of Credit Card
            //todo credit card path
            if (!card.executePayment("files\\creditcardsFORTESTING.txt", ret.getReturnedValue())) {
                System.out.print("Error! Problem with credit card");
                return -1;
            }

            //save the transaction in balance
            BalanceOperationDAO operation = new BalanceOperationDAO();
            BalanceOperation balance = operation.addBalanceOperation(new BalanceOperationModel(0, null, ret.getReturnedValue(), BalanceOperationModel.Type.RETURN));
            returnDAO.setReturnBalanceOperation(ret.getID(), balance.getBalanceId());
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! Problem with DB");
            return -1;
        }

        return ret.getReturnedValue();
    }

    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (RFID == null || RFID.equals("") || RFID.length() != 12)
            throw new InvalidRFIDException();
        for (char element : RFID.toCharArray()) {
            if (!Character.isDigit(element))
                throw new InvalidRFIDException();
        }
        if (this.getID() == null || !this.getID().equals(returnId)) {
            System.out.print("Error! The return Transaction is not open");
            return false;
        }
        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        int index;
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        String barcode;
        try {
            barcode = productEntryDAO.getProductEntryBarcode(RFID);
            //return false if the product does not exist
            if (barcode == null) {
                System.out.print("Error! The product does not exist");
                return false;
            }
            if (productEntryDAO.getProductEntryAvailability(RFID) == 1) {
                System.out.print("Error! The product is already present in the shop");
                return false;
            }
            sale = saleDAO.getSaleTransaction(this.getTransactionID());

            //return false if the Sale transaction does not exist
            if (sale == null) {
                System.out.print("Error! The Sale transaction does not exist");
                return false;
            }
            //return false if the sale transaction does not contain the product
            index = sale.getEntries().stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(barcode);
            if (index == -1) {
                System.out.print("Error! the sale transaction does not contain the product");
                return false;
            }
            TicketEntryRfidDAO ticketRFIDDAO = new TicketEntryRfidDAO();
            if (!ticketRFIDDAO.getSaleTicketEntriesRfid(this.getTransactionID()).contains(RFID)) {
                System.out.print("Error! the sale transaction does not contain the product RFID");
                return false;
            }

        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
            return false;
        }

        // OLD VERSION ------------------------------------
//        TicketEntry ticket = sale.getEntries().get(index);
//        ticket.setAmount(1);
//        entries.add(ticket);
//        return true;
        // ------------------------------------------------

        /*
         * In questo modo vengono aggiunti anche gli RFID nella return
         */
        TicketEntryModel ticket = new TicketEntryModel();
        boolean notFound = true;

        for (TicketEntry t : this.getEntries()) {
            if (t.getBarCode().equals(barcode)) {
                ((TicketEntryModel) t).addRFID(RFID);
                notFound = false;
                break;
            }
        }

        if (notFound) {
            ticket.setBarCode(barcode);
            ticket.setAmount(0);
            ticket.addRFID(RFID);
            entries.add(ticket);
        }

        return true;
    }
}