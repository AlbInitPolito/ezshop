package it.polito.ezshop.DBConnection;

import it.polito.ezshop.exceptions.MissingDAOParameterException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {

    private static DbConnection instance;

    //private static String url = "jdbc:mysql://localhost/";
    //private static String username = "root";
    //private static String password = "EZShop2021";
    //private static String DBname = "ezshopdb";
    private static Connection connection;

    public static DbConnection getInstance() {
        if (instance == null)
            instance = new DbConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean DBconnect(String url, String username, String password, String DBname) throws MissingDAOParameterException {
        if ( (url == null) || (username == null) || (password == null) )
            throw new MissingDAOParameterException("url, username, password are all required " +
                    "in DBconnect in DbConnection");
        if ( ( DBname == null ) || ( DBname.equals("") ) )
            throw new MissingDAOParameterException("DBname is required " +
                    "in DBconnect in DbConnection");
        try {
            if (connection == null)
                connection = DriverManager.getConnection(url + DBname, username, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> executeQuery(String query) {
        if (query == null)
            return null;
        List<String[]> queryResult = null;
        String[] tuple;
        int c;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            queryResult = new ArrayList<>();
            c = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                tuple = new String[c];
                for (int i = 0; i < c; i++)
                    tuple[i] = rs.getString(i + 1);
                queryResult.add(tuple.clone());
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return queryResult;
    }

    public boolean executeUpdate(String query) {
        boolean updateResult;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            updateResult = true;
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            updateResult = false;
        }
        return updateResult;
    }

    public void DBdisconnect() {
        try {
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        connection = null;
        instance = null;
        Runtime.getRuntime().gc();
    }
}
