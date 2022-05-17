package it.polito.ezshop.integrationTests.step3;

import it.polito.ezshop.DBConnection.DbConnection;
import it.polito.ezshop.DBConnection.ProductTypeDAO;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidDAOParameterException;
import it.polito.ezshop.exceptions.MissingDAOParameterException;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testProductTypeDAO {

    @Test
    public void testAddProductType() {
        ProductTypeDAO pdao = new ProductTypeDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type WHERE barcode='1234567890'";
        db.executeUpdate(query);

        assertThrows(MissingDAOParameterException.class, () -> pdao.addProductType(null));
        ProductTypeModel p = new ProductTypeModel();
        assertThrows(MissingDAOParameterException.class, () -> pdao.addProductType(p));
        p.setPricePerUnit(null);
        assertThrows(MissingDAOParameterException.class, () -> pdao.addProductType(p));
        p.setPricePerUnit(10.0);
        p.setBarCode("12345678901234567890");
        assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductType(p));
        p.setBarCode("1234567890");
        try {
            assert (pdao.addProductType(p) != null);
            p.setLocation("1-a-3");
            query = "DELETE FROM product_type WHERE barcode='1234567890'";
            db.executeUpdate(query);
            assert (pdao.addProductType(p) != null);
            p.setLocation(null);
            p.setProductDescription("descriptiontest");
            query = "DELETE FROM product_type WHERE barcode='1234567890'";
            db.executeUpdate(query);
            assert (pdao.addProductType(p) != null);
            StringBuilder desc = new StringBuilder();
            for (int i = 0; i < 21; i++)
                desc.append("0123456789");
            p.setProductDescription(desc.toString());
            assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductType(p));
            p.setProductDescription(null);
            p.setQuantity(-10);
            assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductType(p));
            p.setQuantity(10);
            query = "DELETE FROM product_type WHERE barcode='1234567890'";
            db.executeUpdate(query);
            assert (pdao.addProductType(p) != null);
            p.setQuantity(null);
            p.setNote(desc.toString());
            assertThrows(InvalidDAOParameterException.class, () -> pdao.addProductType(p));
            p.setNote("notetest");
            query = "DELETE FROM product_type WHERE barcode='1234567890'";
            db.executeUpdate(query);
            assert (pdao.addProductType(p) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveProductType() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.removeProductType(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.removeProductType(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type WHERE barcode='testrembar'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testrembar', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (pdao.removeProductType(Integer.parseInt(opquery[0])));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM product_type WHERE id=" + opquery[0];
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

    @Test
    public void testUpdateProductType() {
        ProductTypeDAO pdao = new ProductTypeDAO();

        assertThrows(MissingDAOParameterException.class, () -> pdao.updateProductType(null));
        ProductTypeModel p = new ProductTypeModel(null, null, null, null, null, null, null, null, null, null);
        assertThrows(MissingDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setId(0);
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setId(1);
        assertThrows(MissingDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setLocation("5-1-2");
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setLocation(null);
        p.setBarCode("12345678901234567890");
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setBarCode("1234567890123");
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < 21; i++)
            desc.append("0123456789");
        p.setProductDescription(desc.toString());
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setProductDescription(null);
        p.setNote(desc.toString());
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setNote(null);
        p.setQuantity(-10);
        assertThrows(InvalidDAOParameterException.class, () -> pdao.updateProductType(p));
        p.setQuantity(null);

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type WHERE barcode='testrembar'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testrembar', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);
        p.setId(Integer.parseInt(opquery[0]));
        query = "DELETE FROM product_type WHERE barcode='testupdbar'";
        db.executeUpdate(query);

        try {
            p.setBarCode("testupdbar");
            assert (pdao.updateProductType(p));
            p.setBarCode(null);
            p.setPricePerUnit(30.0);
            assert (pdao.updateProductType(p));
            p.setPricePerUnit(null);
            p.setProductDescription("testupddesc");
            assert (pdao.updateProductType(p));
            p.setProductDescription(null);
            p.setQuantity(22);
            assert (pdao.updateProductType(p));
            p.setQuantity(null);
            p.setNote("testupdnote");
            assert (pdao.updateProductType(p));
            p.setLocation("5-f-2");
            assert (pdao.updateProductType(p));
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProducts() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        List<ProductType> plist = pdao.getProducts();
        assert (plist != null);

        DbConnection db = DbConnection.getInstance();

        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_type";
        db.executeUpdate(query);
        assert (pdao.getProducts().size() == 0);

        query = "DELETE FROM product_type WHERE barcode='testgetprod'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetprod', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);

        assert (pdao.getProducts().size() != 0);
    }

    @Test
    public void testGetProductById() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.getProductById(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.getProductById(0));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type";
        db.executeUpdate(query);
        try {
            assert (pdao.getProductById(1) == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_type WHERE barcode='testgetprod'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetprod', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);
        String[] opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (pdao.getProductById(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_type WHERE barcode='testgetprod'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetprod', null, 10, 17, null, 7, 's', 5);";
        db.executeUpdate(query);
        opquery = db.executeQuery("SELECT last_insert_id();").get(0);

        try {
            assert (pdao.getProductById(Integer.parseInt(opquery[0])) != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductByBarcode() {
        ProductTypeDAO pdao = new ProductTypeDAO();
        assertThrows(MissingDAOParameterException.class, () -> pdao.getProductByBarcode(null));
        assertThrows(InvalidDAOParameterException.class, () -> pdao.getProductByBarcode("1234567890123456"));

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_type";
        db.executeUpdate(query);
        try {
            assert (pdao.getProductByBarcode("12345678901234") == null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_type WHERE barcode='testgetprod'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetprod', null, 10, null, null, null, null, null);";
        db.executeUpdate(query);

        try {
            assert (pdao.getProductByBarcode("testgetprod") != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM product_type WHERE barcode='testgetprod'";
        db.executeUpdate(query);
        query = "INSERT INTO product_type VALUES(null, 'testgetprod', null, 10, 17, null, 7, 's', 5);";
        db.executeUpdate(query);

        try {
            assert (pdao.getProductByBarcode("testgetprod") != null);
        } catch (MissingDAOParameterException | InvalidDAOParameterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetProductTypes() {
        ProductTypeDAO pdao = new ProductTypeDAO();

        DbConnection db = DbConnection.getInstance();
        String query = "DELETE FROM product_order";
        db.executeUpdate(query);
        query = "DELETE FROM product_in_transaction";
        db.executeUpdate(query);
        query = "DELETE FROM return_product";
        db.executeUpdate(query);

        assert (pdao.resetProductTypes());

        query = "SELECT * FROM product_type";
        List<String[]> result = db.executeQuery(query);
        assert (result.size() == 0);
    }

}
