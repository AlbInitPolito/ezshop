package it.polito.ezshop.data;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface SaleTransaction  {

    Integer getPaymentType();

    void setPaymentType(Integer paymentType);

    Date getDate();

    void setDate(Date date);

    Time getTime();

    void setTime(Time time);

    Integer getTicketNumber();

    void setTicketNumber(Integer ticketNumber);

    List<TicketEntry> getEntries();

    void setEntries(List<TicketEntry> entries);

    double getDiscountRate();

    void setDiscountRate(double discountRate);

    double getPrice();

    void setPrice(double price);
}
