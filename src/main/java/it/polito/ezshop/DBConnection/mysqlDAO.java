package it.polito.ezshop.DBConnection;

import it.polito.ezshop.data.dbDAOInterface;
import it.polito.ezshop.exceptions.MissingDAOParameterException;

public class mysqlDAO implements dbDAOInterface {

    public static String url = "jdbc:mysql://localhost/";
    public static String username = "root";
    public static String password = "EZShop2021";
    public static String DBname = "ezshopdb";

    private boolean connected;

    DbConnection db;

    public mysqlDAO() {
        this.db = DbConnection.getInstance();
        this.connected = connect();

    }

    @Override
    public boolean connect() {
        try {
            return db.DBconnect(url, username, password, DBname);
        } catch (MissingDAOParameterException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void disconnect() {
        db.DBdisconnect();
    }

}
