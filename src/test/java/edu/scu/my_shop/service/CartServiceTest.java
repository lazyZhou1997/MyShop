package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.CartException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.scu.my_shop.exception.CartException.*;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    CartService cartService;

    private User wrongUser;
    private User nullUser;
    private User nullIDUser;
    private User user;

    private List<String> nullProductID = null;
    private List<String> wrongProductID = new ArrayList<>();
    private List<String> productID = new ArrayList<>();
    private List<String> twoProductID = new ArrayList<>();
    private List<String> emptyProductID = new ArrayList<>();
    private List<String> duplicateProductID = new ArrayList<>();
    private List<String> twoNewProduct = new ArrayList<>();
    private List<String> productNotExist = new ArrayList<>();
    private List<Integer> nullNumber = null;
    private List<Integer> exceedNumber = new ArrayList<>();
    private List<Integer> zeroNumber = new ArrayList<>();
    private List<Integer> number = new ArrayList<>();
    private List<Integer> twoNumber = new ArrayList<>();
    private List<Integer> emptyNumber = new ArrayList<>();
    private List<Integer> twoNewNumber = new ArrayList<>();
    private List<Integer> numberNotExist = new ArrayList<>();

    @Before
    public void init() {
        wrongUser = new User();
        wrongUser.setUserId("wrongUser"); wrongUser.setHeadImg("wrongUser"); wrongUser.setUserName("wrongUser");
        wrongUser.setRole(false); wrongUser.setBirthday(new Date());

        nullUser = null;

        nullIDUser = new User();
        nullIDUser.setUserId(null);

        user = new User();
        user.setUserId("for-cart-test"); user.setUserPassword("DoNotDeleteManually"); user.setUserName("CartTest");
        user.setRole(false); user.setBirthday(new Date());

        wrongProductID.add("wrongProductID");
        productID.add("for-cart-test-1");
        twoProductID.add("for-cart-test-1"); twoProductID.add("for-cart-test-2");
        duplicateProductID.add("for-cart-test-duplicate");
        twoNewProduct.add("for-cart-test-3"); twoNewProduct.add("for-cart-test-4");
        productNotExist.add("for-cart-test-not-exist");

        exceedNumber.add(10000);
        zeroNumber.add(0);
        number.add(20);
        twoNumber.add(20); twoNumber.add(30);
        twoNewNumber.add(30); twoNewNumber.add(40);
        numberNotExist.add(88);
    }

    @Test
    public void exceptionInsertProducts() {
        try {
            cartService.insertProducts(nullUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(user, nullProductID, number);
        } catch (CartException e) {
            assertEquals(PRODUCT_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(user, productID, nullNumber);
        } catch (CartException e) {
            assertEquals(NUMBER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(user, productID, twoNumber);
        } catch (CartException e) {
            assertEquals(PRODUCT_NUMBER_NOT_MATCH, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(wrongUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(nullIDUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(user, productID, exceedNumber);
        } catch (CartException e) {
            assertEquals(NO_MORE_PRODUCT, e.getCode().longValue());
        }
        try {
            cartService.insertProducts(user, duplicateProductID, number);
        } catch (CartException e) {
            assertEquals(DUPLICATE_SAME_PRODUCT, e.getCode().longValue());
        }
    }

    @Test
    public void emptyInsertProduct() {
        cartService.insertProducts(user, emptyProductID, emptyNumber);
    }

    @Test
    public void insertProduct() {
        cartService.insertProducts(user, twoNewProduct, twoNewNumber);
    }

    @Test
    public void insertExistProduc() {
        cartService.insertProducts(user, twoProductID, twoNumber);
    }

    @Test
    public void exceptionUpdateProducts() {
        try {
            cartService.updateProducts(nullUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(user, nullProductID, number);
        } catch (CartException e) {
            assertEquals(PRODUCT_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(user, productID, nullNumber);
        } catch (CartException e) {
            assertEquals(NUMBER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(user, productID, twoNumber);
        } catch (CartException e) {
            assertEquals(PRODUCT_NUMBER_NOT_MATCH, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(wrongUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(nullIDUser, productID, number);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.updateProducts(user, productID, exceedNumber);
        } catch (CartException e) {
            assertEquals(NO_MORE_PRODUCT, e.getCode().longValue());
        }
    }

    @Test
    public void emptyUpdateProducts() {
        cartService.updateProducts(user, emptyProductID, emptyNumber);
    }

    @Test
    public void updateOneProduct() {
        cartService.updateProducts(user, productID, number);
    }

    @Test
    public void updateTwoProduct() {
        cartService.updateProducts(user, twoProductID, twoNumber);
    }

    @Test
    public void updateNotExist() {
        cartService.updateProducts(user, productNotExist, numberNotExist);
    }

    @Test
    public void updateZero() {
        cartService.updateProducts(user, productID, zeroNumber);
        cartService.updateProducts(user, twoProductID, twoNumber);
    }

    @Test
    public void exceptionDeleteProducts() {
        try {
            cartService.deleteProducts(nullUser, productID);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.deleteProducts(user, nullProductID);
        } catch (CartException e) {
            assertEquals(PRODUCT_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.deleteProducts(wrongUser, productID);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            cartService.deleteProducts(nullIDUser, productID);
        } catch (CartException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
    }

    @Test
    public void deleteProducts() {
        cartService.deleteProducts(user, productNotExist);
        cartService.deleteProducts(user, twoNewProduct);
    }

    @Test
    public void getAllProducts() {
        List<Product> productList = cartService.getAllProducts(user);
        for(Product product : productList) {
            System.out.println(product.getProductId() + " " + product.getProductLeftTotals());
        }
    }
}