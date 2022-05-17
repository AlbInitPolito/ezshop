package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.mysqlDAO;
import org.junit.Test;

public class testMysqlDAO {

    @Test
    public void testConnect(){
        mysqlDAO mdao = new mysqlDAO();
        assert(mdao.connect());
        mysqlDAO.url = null;
        mysqlDAO.DBname = null;
        mysqlDAO.password = null;
        mysqlDAO.username = null;
        assert(!mdao.connect());
        mysqlDAO.url = "jdbc:mysql://localhost/";
        mysqlDAO.username = "root";
        mysqlDAO.password = "EZShop2021";
        mysqlDAO.DBname = "ezshopdb";

        DbConnection db = DbConnection.getInstance();

        mdao.disconnect();
        assert(db.getConnection()==null);
    }
}
