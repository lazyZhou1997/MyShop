package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.CartMapper;
import edu.scu.my_shop.dao.ProductMapper;
import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.Cart;
import edu.scu.my_shop.entity.CartExample;
import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.CartException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static edu.scu.my_shop.entity.CartExample.*;
import static edu.scu.my_shop.exception.CartException.*;

/**
 * Created by Vicent_Chen on 2018/3/19.
 */
@Service
public class CartService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ProductService productService;

    /**
     * This function will check all parameters.
     * All parameters must not be null.(Empty will works)
     * User must be to find in database.
     * @param user
     */
    private void checkParams(User user) {
        // validate data
        if (user == null) {
            throw new CartException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // check whether user is legal
        String userID = user.getUserId();
        if (userID == null || userID.equals("")) {
            sqlSession.close();
            throw new CartException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }
        User userInDB = userMapper.selectByPrimaryKey(userID);
        if (userInDB == null) {
            sqlSession.close();
            throw new CartException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }

        sqlSession.close();
    }

    /**
     * This function will check all parameters.
     * All parameters must not be null.(Empty will works)
     * User must be to find in database.
     * @param user
     * @param productIDList
     */
    private void checkParams(User user, List<String> productIDList) {
        checkParams(user);

        if (productIDList == null) {
            throw new CartException(PRODUCT_EMPTY_MESSAGE, PRODUCT_EMPTY);
        }
    }

    /**
     * This function will check all parameters.
     * All parameters must not be null.(Empty will works)
     * User must be to find in database.
     * @param user
     * @param productIDList
     * @param numberList
     */
    private void checkParams(User user, List<String> productIDList, List<Integer>numberList) {
        checkParams(user, productIDList);

        if (numberList == null) {
            throw new CartException(NUMBMER_EMPTY_MESSAGE, NUMBER_EMPTY);
        }
        if (productIDList.size() != numberList.size()) {
            throw new CartException(PRODUCT_NUMBER_NOT_MATCH_MESSAGE, PRODUCT_NUMBER_NOT_MATCH);
        }
    }

    /**
     * Insert a list of products.
     * All parameters cannot be null otherwise it will throw exception.
     * Contents in list can be null, or list can be empty, this function
     * will automatically ignore.
     * <b>Product that cannot be found in database will be ignored</b>
     * @param user
     * @param productIDList
     * @param numberList
     */
    @Transactional
    public void insertProducts(User user, List<String> productIDList, List<Integer>numberList) {

        checkParams(user, productIDList, numberList);

        String userID = user.getUserId();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);

        // update database
        for (int i = 0; i < productIDList.size(); i++) {
            String productID = productIDList.get(i);
            Integer number = numberList.get(i);

            // ignore empty product
            if (productID == null || number == null || productID.equals("")) {
                continue;
            }
            Product product = productMapper.selectByPrimaryKey(productID);
            if (product == null) continue;
            if (number == 0) continue;

            // check product left
            if (product.getProductLeftTotals() < number) {
                sqlSession.close();
                // TODO: throw exception OR return error message?
                throw new CartException(NO_MORE_PRODUCT_MESSAGE, NO_MORE_PRODUCT);
            }

            // check if product exists
            CartExample cartExample = new CartExample();
            Criteria criteria = cartExample.createCriteria();
            criteria.andUserIdEqualTo(userID).andProductIdEqualTo(productID);
            List<Cart> productInCart = cartMapper.selectByExample(cartExample);

            // product exists
            if (productInCart != null && !productInCart.isEmpty()) {
                // more than one kind of same product means error
                if (productInCart.size() != 1) {
                    sqlSession.close();
                    throw new CartException(DUPLICATE_SAME_PRODUCT_MESSAGE, DUPLICATE_SAME_PRODUCT);
                }
                // update product
                Cart cartToUpdate = productInCart.get(0);
                Integer originNumber = cartToUpdate.getTotals();
                cartToUpdate.setTotals(originNumber + number);
                cartMapper.updateByExample(cartToUpdate, cartExample);
            }
            // product not exist
            else {
                // insert product
                Cart cartToInsert = new Cart();
                cartToInsert.setTotals(number); cartToInsert.setUserId(userID); cartToInsert.setProductId(productID);
                cartMapper.insertSelective(cartToInsert);
            }
        }

        sqlSession.close();
    }

    /**
     * Update a list of products.
     * All parameters cannot be null otherwise it will throw exception.
     * Contents in list can be null, or list can be empty, this function
     * will automatically ignore.
     *
     * <b>Product that cannot be found in database will be ignored</b>
     *
     * delete product when number is 0
     * @param user
     * @param productIDList
     * @param numberList
     */
    @Transactional
    public void updateProducts(User user, List<String> productIDList, List<Integer> numberList) {

        checkParams(user, productIDList, numberList);

        String userID = user.getUserId();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);

        // update database
        for (int i = 0; i < productIDList.size(); i++) {
            String productID = productIDList.get(i);
            Integer number = numberList.get(i);

            // ignore empty product
            if (productID == null || number == null || productID.equals("")) {
                continue;
            }
            Product product = productMapper.selectByPrimaryKey(productID);
            if (product == null) continue;

            // check product left
            if (product.getProductLeftTotals() < number) {
                sqlSession.close();
                // TODO: throw exception OR return error message?
                throw new CartException(NO_MORE_PRODUCT_MESSAGE, NO_MORE_PRODUCT);
            }

            // check product number
            if (number < 0) {
                sqlSession.close();
                throw new CartException(WRONG_PRODUCT_NUMBER_MESSAGE, WRONG_PRODUCT_NUMBER);
            }

            // data to update
            Cart cart = new Cart();
            cart.setProductId(productID); cart.setUserId(user.getUserId()); cart.setTotals(number);
            CartExample cartExample = new CartExample();
            Criteria criteria = cartExample.createCriteria();
            criteria.andProductIdEqualTo(productID).andUserIdEqualTo(userID);

            // delete empty product
            if (number == 0) {
                cartMapper.deleteByExample(cartExample);
            }
            // update product
            else {
                cartMapper.updateByExample(cart, cartExample);
            }
        }

        sqlSession.close();
    }

    /**
     * Delete a list of products.
     * All parameters cannot be null otherwise it will throw exception.
     * Contents in list can be null, or list can be empty, this function
     * will automatically ignore.
     * <b>Product that cannot be found in database will be ignored</b>
     * @param user
     * @param productIDList
     */
    @Transactional
    public void deleteProducts(User user, List<String> productIDList) {
        // add an list for parameter checking
        checkParams(user, productIDList);

        String userID = user.getUserId();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);

        for(String productID : productIDList) {
            CartExample cartExample = new CartExample();
            Criteria criteria = cartExample.createCriteria();
            criteria.andUserIdEqualTo(userID).andProductIdEqualTo(productID);
            cartMapper.deleteByExample(cartExample);
        }

        sqlSession.close();
    }

    /**
     * Delete a list of products.
     * All parameters cannot be null otherwise it will throw exception.
     * Contents in list can be null, or list can be empty, this function
     * will automatically ignore.
     *
     * This function will not return null, but an empty list.
     *
     * This function will automatically update product number in cart
     * when there are not enough product.
     *
     * @param user
     * @return the left_total in product object in the number of products in cart
     */
    @Transactional
    public List<Product> getAllProducts(User user) {
        checkParams(user);

        // search all product ID
        String userID = user.getUserId();
        CartExample cartExample = new CartExample();
        Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userID);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);
        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        List<Cart> cartList = cartMapper.selectByExample(cartExample);

        List<Product> productList = new ArrayList<>();
        // extract product
        for (Cart cart : cartList) {
            String productID = cart.getProductId();
            Product product = productMapper.selectByPrimaryKey(productID);

            // update product number when not enough product left
            Integer productLeft = product.getProductLeftTotals();
            Integer productInCart = cart.getTotals();
            if (productLeft < productInCart) {
                cart.setTotals(productLeft);
                CartExample updateExample = new CartExample();
                Criteria updateCriteria = updateExample.createCriteria();
                updateCriteria.andUserIdEqualTo(userID).andProductIdEqualTo(productID);

                cartMapper.updateByExample(cart, updateExample);
            }

            // modify product and add to
            // NOTE: NEVER UPDATE product TABLE since here
            product.setProductLeftTotals(cart.getTotals());
            productList.add(product);
        }

        sqlSession.close();
        return productList;
    }

}
