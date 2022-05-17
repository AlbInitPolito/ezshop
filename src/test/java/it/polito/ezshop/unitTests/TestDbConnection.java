package it.polito.ezshop.unitTests;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class TestDbConnection {

    @Test
    public void testGetInstance() {
        DbConnection.getInstance().reset();
        DbConnection instance = DbConnection.getInstance();
        assert (instance != null);
        assert (instance == DbConnection.getInstance());
        //line 14 is sufficient for WB
    }

    @Test
    public void testDBconnect() {
        DbConnection.getInstance().reset();
        assertThrows(MissingDAOParameterException.class, () -> DbConnection.getInstance().DBconnect(null, null, null, null));
        assertThrows(MissingDAOParameterException.class, () -> DbConnection.getInstance().DBconnect("", null, null, null));
        assertThrows(MissingDAOParameterException.class, () -> DbConnection.getInstance().DBconnect("", "", null, null));
        assertThrows(MissingDAOParameterException.class, () -> DbConnection.getInstance().DBconnect("", "", "", null));
        try {
            assert (!DbConnection.getInstance().DBconnect("", "", "", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert (!DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "", "", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert (!DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert (!DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert (!DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "testdb"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assert (DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "ezshopdb"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //lines 18, 22, 30 are sufficient for WB
    }

    @Test
    public void testExecuteQuery() {
        DbConnection.getInstance().reset();
        assert (DbConnection.getInstance().executeQuery(null) == null);
        assert (DbConnection.getInstance().executeQuery("SELECT * FROM user;") == null);
        try {
            DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "ezshopdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert (DbConnection.getInstance().executeQuery("SELECT * FROM user;") != null);
        assert (DbConnection.getInstance().executeQuery("SELECT * FROM testtable;") == null);
        //lines 37, 38, 41 are sufficient for WB
    }

    @Test
    public void testExecuteUpdate() {
        DbConnection.getInstance().reset();
        assert (!DbConnection.getInstance().executeUpdate(null));
        try {
            DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "ezshopdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert (DbConnection.getInstance().executeUpdate("UPDATE user SET username='testuser' WHERE id=1;"));
        assert (!DbConnection.getInstance().executeUpdate("UPDATE user SET testfield='testuser' WHERE id=1;"));
        //lines 48, 51  are sufficient for WB
    }

    @Test
    public void testDBdisconnect() { //sufficient for WB
        DbConnection.getInstance().reset();
        DbConnection.getInstance().DBdisconnect();
        assert (DbConnection.getInstance().getConnection() == null);
        try {
            DbConnection.getInstance().DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "ezshopdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbConnection.getInstance().DBdisconnect();
        assert (DbConnection.getInstance().getConnection() == null);
    }

    @Test
    public void testReset() {
        DbConnection instance = DbConnection.getInstance();
        try {
            instance.DBconnect("jdbc:mysql://localhost/", "root", "EZShop2021", "ezshopdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connection = DbConnection.getInstance().getConnection();
        instance.reset();
        DbConnection newInstance = DbConnection.getInstance();
        assert (instance != newInstance);
        Connection newConnection = DbConnection.getInstance().getConnection();
        assert (connection != newConnection);
    }
}
