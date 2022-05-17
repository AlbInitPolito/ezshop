package it.polito.ezshop.unitTests;

import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestProductTypeModel {

    @Test
    public void testGTIN13Check() {
        assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check(null));
        assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check("10xa"));
        assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check("abcx"));
        assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check("a111"));
        try {
            assert (!ProductTypeModel.GTIN13Check("6291041500210"));
            assert (!ProductTypeModel.GTIN13Check("6291041500211"));
            assert (!ProductTypeModel.GTIN13Check("6291041500212"));
            assert (ProductTypeModel.GTIN13Check("6291041500213"));
            assert (!ProductTypeModel.GTIN13Check("6291041500214"));
            assert (!ProductTypeModel.GTIN13Check("6291041500215"));
            assert (!ProductTypeModel.GTIN13Check("6291041500216"));
            assert (!ProductTypeModel.GTIN13Check("6291041500217"));
            assert (!ProductTypeModel.GTIN13Check("6291041500218"));
            assert (!ProductTypeModel.GTIN13Check("6291041500219"));
            assert (ProductTypeModel.GTIN13Check("1234567891200"));
            assert (ProductTypeModel.GTIN13Check("123456789128"));
            assert (ProductTypeModel.GTIN13Check("1234567891231"));
            assert (ProductTypeModel.GTIN13Check("12345678912343"));

            assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check("6291041500218151"));
            assertThrows(InvalidProductCodeException.class, () -> ProductTypeModel.GTIN13Check("629104150"));

            assert (ProductTypeModel.GTIN13Check("1234567891200"));
        } catch (InvalidProductCodeException ipc) {
            System.out.println("Invalid Product Code");
        }
    }
}