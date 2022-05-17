package it.polito.ezshop.exceptions;

public class InvalidDAOParameterException extends Exception {
    public InvalidDAOParameterException() { super(); }
    public InvalidDAOParameterException(String msg) { super(msg); }
}
