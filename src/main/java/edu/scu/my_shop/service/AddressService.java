package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.AddressMapper;
import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.Address;
import edu.scu.my_shop.entity.AddressExample;
import edu.scu.my_shop.entity.AddressExample.Criteria;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.AddressException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static edu.scu.my_shop.exception.AddressException.*;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
@Service
public class AddressService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * Address cannot be null.
     * user ID cannot be null.
     * user ID must be found in user table.
     * @param address
     */
    private void checkAddress(Address address) {
        if (address == null) {
            throw new AddressException(ADDRESS_EMPTY_MESSAGE, ADDRESS_EMPTY);
        }

        String userID = address.getUserId();
        if (userID == null) {
            throw new AddressException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(userID);
        if (user == null) {
            throw new AddressException(USER_EMPTY_MESSAGE, USER_EMPTY);
        }

        sqlSession.close();
    }

    /**
     * Insert an address into database.
     * If addressId already exists it will be automatically ignored.
     * @param address
     */
    @Transactional
    public String insertAddress(Address address) {

        checkAddress(address);

        if (address.getAddressInfo() == null || address.getPhoneNumber() == null) {
            throw new AddressException(ADDRESS_EMPTY_MESSAGE, ADDRESS_EMPTY);
        }

        // auto generate address ID
        String addressID = UUID.randomUUID().toString();
        address.setAddressId(addressID);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        // is_default_address can be null
        addressMapper.insertSelective(address);

        // update to database
        sqlSession.close();

        return addressID;
    }

    /**
     * Update address in database.
     * Automatically ignore address that not exist.
     *
     * @param address
     */
    @Transactional
    public void updateAddress(Address address) {
        checkAddress(address);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);

        if (address.getIsDefaultAddress()) {
            Address setNotDefault = new Address();
            setNotDefault.setIsDefaultAddress(false);

            AddressExample addressExample = new AddressExample();
            addressExample.createCriteria().andUserIdEqualTo(address.getUserId());
            addressMapper.updateByExampleSelective(setNotDefault, addressExample);
        }

        addressMapper.updateByPrimaryKeySelective(address);
        sqlSession.close();
    }

    /**
     * Delete address from database.
     * Address not exists will be ignored.
     * @param address
     */
    @Transactional
    public void deleteAddress(Address address) {
        checkAddress(address);

        String addressID = address.getAddressId();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        addressMapper.deleteByPrimaryKey(addressID);
        sqlSession.close();
    }

    /**
     * Get all address of user.
     * It will not return null but an empty list
     * @param userID
     */
    @Transactional
    public List<Address> getAllAddresses(String userID) {
        // create example
        AddressExample addressExample = new AddressExample();
        Criteria criteria = addressExample.createCriteria();
        criteria.andUserIdEqualTo(userID);

        // search database
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        List<Address> addressList = addressMapper.selectByExample(addressExample);
        sqlSession.close();

        return addressList;
    }

    /**
     * Check if address exists.
     * @param addressID
     * @return
     */
    public boolean addressExists(String addressID) {
        if (addressID == null || addressID.equals("")) {
            return false;
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        Address address = addressMapper.selectByPrimaryKey(addressID);

        sqlSession.close();
        return address != null;
    }
}
