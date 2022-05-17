package it.polito.ezshop.exceptions;

public class InvalidProductCodeException extends Exception {
    public InvalidProductCodeException() { super("Error! Invalid Product code"); }
    public InvalidProductCodeException(String msg) { super(msg); }
}
