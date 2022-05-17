package it.polito.ezshop.model;

import it.polito.ezshop.DBConnection.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SaleTransactionModel implements it.polito.ezshop.data.SaleTransaction {


    private Integer ticketNumber;
    private Date date;
    //payment Type is 0 when the transaction is not payed, 1 if payed with cash, 2 if payed with card
    private Integer paymentType;
    private double discountRate;
    private Time time;
    private double price;
    private List<TicketEntry> entries;

    public SaleTransactionModel() {
        this.discountRate = 0.0;
        this.time = null;
        this.entries = new ArrayList<>();
        this.paymentType = 0;
        this.ticketNumber = null;
        this.date = null;
        this.price = 0.0;
    }

    public SaleTransactionModel(Integer ticketNumber, Integer paymentType, double price, double discountRate, Date date, Time time, List<TicketEntry> entries) {
        this.ticketNumber = ticketNumber;
        this.date = date;
        this.paymentType = paymentType;
        this.discountRate = discountRate;
        this.time = time;
        this.price = price;
        this.entries = entries;
    }

    @Override //
    public Integer getPaymentType() {

        return this.paymentType;
    }

    @Override
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public Integer getTicketNumber() {
        return this.ticketNumber;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return this.entries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public double getDiscountRate() {
        return this.discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }


    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException {
        //all the throw for the exceptions
        if (cash <= 0)
            throw new InvalidPaymentException();
        if (ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();

        SaleTransactionDAO saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        double rest;

        try {
            sale = saleDAO.getSaleTransaction(ticketNumber);

            //return -1 if sale does not exist
            if (sale == null) {
                //return -1 if the sale is not ended
                if (this.getTicketNumber()!= null && this.getTicketNumber().equals(ticketNumber)) {
                    System.out.print("Error! Transaction not Ended");
                    return -1;
                }
                System.out.print("Error! The sale does not exist");
                return -1;
            }
            //compute the total
            //for each element in the list control the discount
            //if there is the discount in the sale apply it otherwise apply the other (if there is not the discount
            //would be 0.0
            //get the rest
            rest = cash - sale.getPrice();

            //return -1 if the cash is not enough
            if (rest < 0) {
                System.out.print("Error! Not enough cash");
                return -1;
            }

            //update the saleTransaction
            saleDAO.updateSaleTransaction(new SaleTransactionModel(sale.getTicketNumber(), 1, sale.getPrice(), -1, null, null, null));

            //introduce a new balance Operation
            BalanceOperationDAO operation = new BalanceOperationDAO();
            operation.addBalanceOperation(new BalanceOperationModel(0, null, sale.getPrice(), BalanceOperationModel.Type.SALE));

        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! The DB has an error");
            e.printStackTrace();
            return -1;
        }
        return rest;
    }

    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException {
        //throw the exception
        if (creditCard == null || creditCard.equals("") || !CreditCardModel.luhnAlgorithm(creditCard))
            throw new InvalidCreditCardException();

        if (ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();


        SaleTransactionDAO saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;

        try {
            sale = saleDAO.getSaleTransaction(ticketNumber);


            //return -1 if sale does not exist
            if (sale == null) {
                //return -1 if the sale is not ended
                if (this.getTicketNumber()!= null && this.getTicketNumber().equals(ticketNumber)) {
                    System.out.print("Error! Transaction not Ended");
                    return false;
                }
                System.out.print("Error! The sale does not exist");
                return false;
            }


            //return false if there is problem with the file of the cards
            List<CreditCardModel> listCard = CreditCardModel.loadCreditCardsFromFile("files\\creditcardsFORTESTING.txt");
            if (listCard == null) {
                System.out.print("Error! Problem with file");
                return false;
            }
            //return -1 if the credit card is not registered
            int index = listCard.stream().map(CreditCardModel::getCreditCardNumber).collect(Collectors.toList()).indexOf(creditCard);

            if (index==-1) {
                System.out.print("Error! Credit card not registered");
                return false;
            }
            //compute the total
            //for each element in the list control the discount
            //if there is the discount in the sale apply it otherwise apply the other (if there is not the discount
            //would be 0.0
            //return -1 if the creditCard has no enough money
            CreditCardModel card = listCard.get(index);
            if (!card.hasEnoughMoney(sale.getPrice())) {
                System.out.print("Error! Credit card has not enough money");
                return false;
            }

            //update the saleTransaction
            saleDAO.updateSaleTransaction(new SaleTransactionModel(sale.getTicketNumber(), 2, sale.getPrice(), 0, null, null, null));


            // Update the credit of Credit Card
            if (!card.executePayment("files\\creditcardsFORTESTING.txt", sale.getPrice())) {
                System.out.print("Error! Problem with credit card");
                return false;
            }

            //save the transaction in balance
            BalanceOperationDAO operation = new BalanceOperationDAO();
            operation.addBalanceOperation(new BalanceOperationModel(0, null, sale.getPrice(), BalanceOperationModel.Type.SALE));

        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! The DB has an error");
            return false;
        }
        return true;
    }

    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        //throw exceptions
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0)
            throw new InvalidQuantityException();


        //return false if the transactionId does not correspond to the transaction who is open
        if (!transactionId.equals(this.getTicketNumber())) {
            System.out.print("Error! The transaction is not open!");
            return false;
        }
        //Take the product from DB
        ProductTypeDAOInterface dao = new ProductTypeDAO();
        ProductType product;
        try {
            product = dao.getProductByBarcode(productCode);


            //if the product not exist return false
            if (product == null) {
                System.out.print("Error! Product not available!");
                return false;
            }

            //return false if the quantity is not available
            int quantity = product.getQuantity();
            if (quantity - amount < 0) {
                System.out.print("Error! Quantity not available!");
                return false;
            }

            TicketEntry ticket = new TicketEntryModel(productCode, product.getProductDescription(), amount, 0.0, product.getPricePerUnit());
            entries.add(ticket);

            //delete the product from the ProductDB
            dao.updateProductType(new ProductTypeModel(product.getId(),product.getBarCode(),null,product.getPricePerUnit(),product.getQuantity()-amount,0.0,null,null,null,null));

        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! The DB has an error");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{

        boolean newEntry = true;
        if(transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if(RFID == null || RFID.equals("") || RFID.length() != 12)
            throw new InvalidRFIDException();

        for (int i = 0; i < 12; i++){
            if (!Character.isDigit(RFID.charAt(i)))
                throw new InvalidRFIDException();
        }

        if(!transactionId.equals(this.getTicketNumber()))
            return false;

        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        String barcode;
        try{
            barcode = productEntryDAO.getProductEntryBarcode(RFID);
            if(barcode == null)
                return false; // the RFID does not exist

            if(productEntryDAO.getProductEntryAvailability(RFID) == 0)
                return false; // product not available

            ProductType product = productTypeDAO.getProductByBarcode(barcode);

            if(product == null)
                return false;

            if(product.getQuantity() == 0)
                throw new InvalidQuantityException();

            for(TicketEntry t : this.entries){
                if(t.getBarCode().equals(barcode)){
                    ((TicketEntryModel) t).addRFID(RFID);
                    newEntry = false;
                    break;
                }
            }

            if(newEntry){
                TicketEntryModel ticket = new TicketEntryModel(barcode, product.getProductDescription(), 0, 0.0, product.getPricePerUnit());
                ticket.addRFID(RFID);
                entries.add(ticket);
            }

            // Update both the ProductType entry, decreasing the available amount by 1, and the ProductEntry entry, setting it as unavailable
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()-1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry(RFID, false);

        } catch(MissingDAOParameterException | InvalidDAOParameterException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException {
        //throw exception
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0)
            throw new InvalidQuantityException();


        //return false if the transaction is not open
        if (this.getTicketNumber()==null || !this.getTicketNumber().equals(transactionId)) {
            System.out.print("Error! The transaction in not open");
            return false;
        }


        ProductTypeDAO productDAO = new ProductTypeDAO();
        ProductType product;
        try {

            product=productDAO.getProductByBarcode(productCode);
            //if the product does not exist or there is problem with DB
            if (product == null) {
                System.out.print("Error! The element does not exist");
                return false;
            }
            //take the index of the product
            int index = entries.stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(productCode);

            //if the product is not present in the transaction
            if (index == -1) {
                System.out.print("Error! Element not present in transaction");
                return false;
            }
            int productAmount = entries.get(index).getAmount();

            //return false if the quantity is not valid
            if (productAmount < amount) {
                System.out.print("Error! Amount too big");
                return false;
            }

            // modify the quantity or remove the product from the entries
            if (productAmount == amount)
                entries.remove(index);
            else
                entries.get(index).setAmount(productAmount - amount);

            //add product to productDB
            ProductTypeModel toUpdate = new ProductTypeModel();
            toUpdate.setId(productDAO.getProductByBarcode(productCode).getId());
            toUpdate.setQuantity(product.getQuantity() + amount);
            productDAO.updateProductType(toUpdate);

        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! Problem with DB");
            return false;
        }
        return true;
    }

    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException{
        if(transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if(RFID == null || RFID.equals("") || RFID.length() != 12)
            throw new InvalidRFIDException();

        for (int i = 0; i < 12; i++){
            if (!Character.isDigit(RFID.charAt(i)))
                throw new InvalidRFIDException();
        }

        if(this.getTicketNumber() == null || !this.getTicketNumber().equals(transactionId))
            return false;

        ProductTypeDAO productTypeDAO = new ProductTypeDAO();
        ProductEntryDAO productEntryDAO = new ProductEntryDAO();
        String barcode;
        ProductType product;
        try{
            barcode = productEntryDAO.getProductEntryBarcode(RFID);
            if(barcode == null)
                return false; // The product entry does not exist

            if(productEntryDAO.getProductEntryAvailability(RFID) != 0)
                return false; // The product entry is still available, which means that it is not part of the transaction

            product = productTypeDAO.getProductByBarcode(barcode);
            if(product == null)
                return false; // The barcode of the entry does not match an existing product type

            int index = entries.stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(barcode);

            if(index == -1)
                return false; // The product type is not part of the ticket entries for the transaction

            if(!((TicketEntryModel) entries.get(index)).contains(RFID))
                return false; // The product type is part of the transaction, but not the specific product entry with the provided RFID

            if(entries.get(index).getAmount() == 1)
                entries.remove(index); // If quantity is 1, remove the entry altogether
            else {
                ((TicketEntryModel) entries.get(index)).removeRFID(RFID); // If quantity is more than 1, only remove the RFID (and update the quantity, done by TicketEntryModel.removeRFID())
            }

            // Update both the ProductType entry, increasing the available amount by 1, and the ProductEntry entry, setting it as available
            productTypeDAO.updateProductType(new ProductTypeModel(product.getId(), barcode, null, product.getPricePerUnit(), product.getQuantity()+1, 0.0, null, null, null, null));
            productEntryDAO.updateProductEntry(RFID, true);

        } catch(MissingDAOParameterException | InvalidDAOParameterException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException {
        //throw exceptions
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (discountRate < 0 || discountRate >= 1.0)
            throw new InvalidDiscountRateException();

        try {
            SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();
            SaleTransaction sale = saleDAO.getSaleTransaction(transactionId);

            //return false if the transaction is not open
            if (this.getTicketNumber() == null || !this.getTicketNumber().equals(transactionId)) {
                if (sale == null) {
                    System.out.print("Error! The transaction does not exist");
                    return false;
                } else if (sale.getPaymentType() != 0) {
                    System.out.print("Error! The transaction is already been payed");
                    return false;
                }
            }

            if(this.getTicketNumber()!= null && this.getTicketNumber().equals(transactionId)) {
                this.setDiscountRate(discountRate);
                this.setPrice(this.getPrice()*(1-this.getDiscountRate()));
            }else {
                saleDAO.updateSaleTransaction(new SaleTransactionModel(transactionId, null,sale.getPrice()*(1-discountRate) , discountRate, null, null, null));
            }
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error! Problem with DB");
            return false;
        }

        return true;
    }

    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException {
        //throw exception
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();


        int points = 0;
        double transactionAmount=0;
        try {
            SaleTransactionDAO saleDAO = new SaleTransactionDAO();
            SaleTransaction sale= saleDAO.getSaleTransaction(transactionId);


            //return false if the transaction not exists
            if ((this.getTicketNumber()==null ||!this.getTicketNumber().equals(transactionId)) && sale == null) {
                System.out.print("Error! Transaction not open");
                return -1;
            }

            //if the transaction is open
            if (this.getTicketNumber()!= null && this.getTicketNumber().equals(transactionId))
                    sale = this;
            //compute the total
            for (TicketEntry obj : sale.getEntries()) {
                transactionAmount=transactionAmount+(obj.getAmount()*(obj.getPricePerUnit()*(1-obj.getDiscountRate())));
            }
            transactionAmount=transactionAmount*(1-sale.getDiscountRate());

        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! Problem with DB");
            return -1;
        }

        //compute the points
        while (transactionAmount > 10) {
            points++;
            transactionAmount = transactionAmount - 10;
        }
        return points;
    }

    public Integer startSaleTransaction() {
        SaleTransactionDAOInterface saleDAO = new SaleTransactionDAO();

        //return -1 if there is a problem with db
        if (saleDAO.getMaxSaleTransactionId() == null) {
            System.out.print("Error with the loading of the ID");
            return -1;
        }

        this.setTicketNumber(saleDAO.getMaxSaleTransactionId() + 1);

        return this.getTicketNumber();
    }

    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        //throw exception
        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }



        //return false if there is problem with DB
        try {
            SaleTransactionDAO saleDAO = new SaleTransactionDAO();
            SaleTransaction sale= saleDAO.getSaleTransaction(transactionId);


            //return false if the transaction does not exist or it is already been closed
            if (this.getTicketNumber() == null || !this.getTicketNumber().equals(transactionId)) {
                if (sale == null)
                    System.out.print("Error! The transaction does not exist");
                else
                    System.out.print("Error! The transaction is already been closed");
                return false;
            }
            this.setPrice(0);
            this.getEntries().forEach(element -> {
                this.setPrice(this.getPrice()+(element.getAmount()*(element.getPricePerUnit()*(1-element.getDiscountRate()))));
            });
            this.setPrice(this.getPrice()*(1-this.getDiscountRate()));
            saleDAO.addSaleTransaction(this);
        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error! Problem with the DB");
            return false;
        }

        //update the DB with the ticket
        TicketEntryDAOInterface ticketDao = new TicketEntryDAO();
        this.getEntries().forEach(element -> {
            try {
                ticketDao.addTicketEntry(element, transactionId);
            } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
                System.out.print("Error! The DB has an error");
            }
        });
        this.setEntries(new ArrayList<>());
        this.setDate(null);
        this.setPaymentType(0);
        this.setDiscountRate(0.0);
        this.setTime(null);
        this.setPrice(0.0);
        this.setTicketNumber(null);
        return true;
    }

    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        //throw exception
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        SaleTransactionDAO saleDAO = new SaleTransactionDAO();
        SaleTransaction sale;

        //return false if there is problem with DB
        try {
            sale = saleDAO.getSaleTransaction(transactionId);
            //return false if the transaction does not exist or it is already been payed
            if (this.getTicketNumber() == null || !this.getTicketNumber().equals(transactionId)) {
                if (sale == null) {
                    System.out.print("Error! The transaction does not exist");
                    return false;
                } else if (sale.getPaymentType() != 0) {
                    System.out.print("Error! The transaction is already been payed");
                    return false;
                }
            }
            //if the transaction is open
            if (this.getTicketNumber() != null) {
                this.setEntries(new ArrayList<>());
                this.setDate(null);
                this.setPaymentType(0);
                this.setDiscountRate(0.0);
                this.setTime(null);
                this.setPrice(0.0);
                this.setTicketNumber(null);
                return true;
            }



            //if the transaction is close but not payed update the DB with the quantity
            ProductTypeDAOInterface productDAO = new ProductTypeDAO();
            TicketEntryDAOInterface ticketDao = new TicketEntryDAO();
            sale.getEntries().forEach(element -> {
                try {
                    ProductType product = productDAO.getProductByBarcode(element.getBarCode());
                    int quantity= product.getQuantity()+element.getAmount();
                    ProductType prodUpdate = new ProductTypeModel(product.getId(),product.getBarCode(),null,null,quantity,null,null,null,null,null);
                    productDAO.updateProductType(prodUpdate);
                    ticketDao.removeTicketEntry(element, transactionId);
                } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
                    ex.printStackTrace();
                }
            });

            saleDAO.removeSaleTransaction(transactionId);
        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error with the DB");
            return false;
        }
        return true;
    }

    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException {
        //throw exception
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        SaleTransactionDAOInterface SaleDAO = new SaleTransactionDAO();
        SaleTransaction sale;
        try {
            sale = SaleDAO.getSaleTransaction(transactionId);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            System.out.print("Error with the DB");
            return null;
        }
        return sale;
    }

    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        //throw exceptions
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.equals("") || !ProductTypeModel.GTIN13Check(productCode))
            throw new InvalidProductCodeException();
        if (discountRate < 0.0 || discountRate >= 1.0)
            throw new InvalidDiscountRateException();

        //return false if the transaction is not open
        if (this.getTicketNumber()==null || !this.getTicketNumber().equals(transactionId)) {
            System.out.print("Error! The transaction is not open");
            return false;
        }

        ProductTypeDAO productDAO = new ProductTypeDAO();
        ProductType product;
        try {
            product = productDAO.getProductByBarcode(productCode);
        } catch (MissingDAOParameterException | InvalidDAOParameterException ex) {
            System.out.print("Error with the DB");
            return false;
        }
        //return false if the product code does not exit
        if (product == null) {
            System.out.print("Error! The product does not exist");
            return false;
        }



        int index = entries.stream().map(TicketEntry::getBarCode).collect(Collectors.toList()).indexOf(productCode);
        //return false if the product is not present in the transaction
        if (index == -1) {
            System.out.print("Error! The element is not present in the transaction");
            return false;
        }

        entries.get(index).setDiscountRate(discountRate);
        return true;
    }
}

