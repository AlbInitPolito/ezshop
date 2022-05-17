package it.polito.ezshop;

import it.polito.ezshop.DBConnection.mysqlDAO;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {

    public static void main(String[] args) {
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        if (args.length == 0) {
            mysqlDAO.url = "jdbc:mysql://localhost/";
            mysqlDAO.username = "root";
            mysqlDAO.password = "EZShop2021";
            mysqlDAO.DBname = "ezshopdb";
        } else {
            mysqlDAO.url = args[0];
            mysqlDAO.username = args[1];
            mysqlDAO.password = args[2];
            mysqlDAO.DBname = args[3];
        }

        EZShopGUI gui = new EZShopGUI(ezShop);
    }

}
