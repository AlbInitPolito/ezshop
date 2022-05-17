package it.polito.ezshop.exceptions;

public class MissingDAOParameterException extends Exception {
    public MissingDAOParameterException() { super(); }
    public MissingDAOParameterException(String msg) { super(msg); }
}
