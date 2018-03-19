package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.exception.ProductException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static edu.scu.my_shop.exception.ProductException.*;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    private Product goodProduct = new Product();
    private Product nameNull = new Product();
    private Product idNull = new Product();
    private Product priceNull = new Product();
    private Product totalNull = new Product();
    private Product categoryNull = new Product();
    private Product categoryError = new Product();
    private Product descriptionNull = new Product();
    private Product notExist = new Product();
    private Product toDelete = new Product();
    private Product toUpdateGood = new Product();
    private Product toUpdateSelectively = new Product();

    @Before
    public void init() {
        goodProduct.setProductId("goodProduct");
        goodProduct.setProductName("goodProduct");
        goodProduct.setProductDescription("goodProduct");
        goodProduct.setProductLeftTotals(100);
        goodProduct.setProductPrice(100.0);
        goodProduct.setSecondCategoryId("kajsnxaksajskx");

        nameNull.setProductId("nameNull");
        nameNull.setProductName(null);
        nameNull.setProductDescription("nameNull");
        nameNull.setProductLeftTotals(101);
        nameNull.setProductPrice(101.0);
        nameNull.setSecondCategoryId("kajsnxaksajskx");

        idNull.setProductId(null);
        idNull.setProductName("idNull");
        idNull.setProductDescription("idNull");
        idNull.setProductLeftTotals(102);
        idNull.setProductPrice(102.0);
        idNull.setSecondCategoryId("kajsnxaksajskx");

        priceNull.setProductId("priceNull");
        priceNull.setProductName("priceNull");
        priceNull.setProductDescription("priceNull");
        priceNull.setProductLeftTotals(103);
        priceNull.setProductPrice(null);
        priceNull.setSecondCategoryId("kajsnxaksajskx");

        totalNull.setProductId("totalNull");
        totalNull.setProductName("totalNull");
        totalNull.setProductDescription("totalNull");
        totalNull.setProductLeftTotals(null);
        totalNull.setProductPrice(104.0);
        totalNull.setSecondCategoryId("kajsnxaksajskx");

        categoryNull.setProductId("categoryNull");
        categoryNull.setProductName("categoryNull");
        categoryNull.setProductDescription("categoryNull");
        categoryNull.setProductLeftTotals(105);
        categoryNull.setProductPrice(105.0);
        categoryNull.setSecondCategoryId(null);

        categoryError.setProductId("categoryError");
        categoryError.setProductName("categoryError");
        categoryError.setProductDescription("categoryError");
        categoryError.setProductLeftTotals(106);
        categoryError.setProductPrice(106.0);
        categoryError.setSecondCategoryId("categoryError");

        descriptionNull.setProductId("descriptionNull");
        descriptionNull.setProductName("descriptionNull");
        descriptionNull.setProductDescription(null);
        descriptionNull.setProductLeftTotals(107);
        descriptionNull.setProductPrice(107.0);
        descriptionNull.setSecondCategoryId("kajsnxaksajskx");

        notExist.setProductId("notExist");

        toDelete.setProductId("fa5aa63e-e756-4d5d-8df1-1fdb179a555f");
        toDelete.setProductName("For Delete Test");

        toUpdateGood.setProductId("87c491c5-c6b8-4254-b142-44cd2343eadf");
        toUpdateGood.setProductName("for update test");
        toUpdateGood.setProductDescription("DO NOT DELETE");
        toUpdateGood.setProductLeftTotals(200);
        toUpdateGood.setProductPrice(200.0);
        toUpdateGood.setSecondCategoryId("kajsnxaksajskx");

        toUpdateSelectively.setProductId("245af984-6484-4dac-9549-beab145471fe");
        toUpdateSelectively.setProductName("for seletively update test");
        toUpdateSelectively.setProductDescription("DO NOT DELETE");
    }

    @Test
    public void basicInsertProductTest() {
        productService.insertProduct(goodProduct);
        productService.insertProduct(idNull);
        productService.insertProduct(descriptionNull);

        try {
            productService.insertProduct(null);
        } catch (ProductException e) {
            assertEquals(PRODUCT_IS_NULL, e.getCode().longValue());
        }

        try {
            productService.insertProduct(nameNull);
        } catch (ProductException e) {
            assertEquals(PRODUCT_NAME_EMPTY, e.getCode().longValue());
        }

        try {
            productService.insertProduct(priceNull);
        } catch (ProductException e) {
            assertEquals(PRODUCT_PRICE_EMPTY, e.getCode().longValue());
        }

        try {
            productService.insertProduct(totalNull);
        } catch (ProductException e) {
            assertEquals(PRODUCT_TOTAL_EMPTY, e.getCode().longValue());
        }

        try {
            productService.insertProduct(categoryNull);
        } catch (ProductException e) {
            assertEquals(PRODUCT_CATEGORY_EMPTY, e.getCode().longValue());
        }

        try {
            productService.insertProduct(categoryError);
        } catch (ProductException e) {
            assertEquals(PRODUCT_CATEGORY_ERROR, e.getCode().longValue());
        }
    }

    @Test
    public void duplicateInsertProductTest() {
        productService.insertProduct(goodProduct);
        productService.insertProduct(goodProduct);
    }

    @Test
    public void basicDeleteProductTest() {
        productService.deleteProduct(notExist);
        productService.deleteProduct(toDelete);
    }

    @Test
    public void basicUpdateProductTest() {
        try {
            productService.updateProduct(null);
        } catch (ProductException e) {
            assertEquals(PRODUCT_IS_NULL, e.getCode().longValue());
        }

        productService.updateProduct(goodProduct);
        productService.updateProduct(toUpdateGood);
        productService.updateProduct(toUpdateSelectively);
    }

    /**
     * 分页查询
     */
    @Test
    public void getAllProductsTest() {
        List<Product> productList = productService.getAllProducts(1,3);
        System.out.println(productList.size());
        for (Product product : productList) {
            System.out.println(product.getProductName());
        }

        productList = productService.getAllProducts(2,3);
        System.out.println(productList.size());
        for (Product product : productList) {
            System.out.println(product.getProductName());
        }
    }

    @Test
    public void searchProductByIDTest() {
        Product productNull = productService.searchProductByID(goodProduct.getProductId());
        assertEquals(null, productNull);

        Product productGood = productService.searchProductByID(toDelete.getProductId());
        assertEquals(productGood.getProductName(), toDelete.getProductName());
    }

    /**
     * 测试商品模糊查询通过
     */
    @Test
    public void SearchProductByName(){

        List<Product> products = productService.searchProductByName("id");

        for (Product product:
             products) {
            System.out.println(product.getProductName());
        }

    }
}