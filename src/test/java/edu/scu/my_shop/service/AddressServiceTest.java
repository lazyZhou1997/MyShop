package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Address;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.AddressException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static edu.scu.my_shop.exception.AddressException.ADDRESS_EMPTY;
import static edu.scu.my_shop.exception.AddressException.USER_EMPTY;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AddressServiceTest {

    @Autowired
    AddressService addressService;

    private Address addressToInsert;
    private Address nullAddress;
    private Address nullUserIDAddress;
    private Address nullIDAddress;
    private Address nullInfoAddress;
    private Address nullPhoneAddress;
    private Address nullDefaultAddress;
    private Address addressExist;
    private Address addressNotExist;
    private Address addressToDelete;
    private Address addressToUpdate;

    @Before
    public void init() {
        addressToInsert = new Address(); addressToInsert.setIsDefaultAddress(false);
        addressToInsert.setAddressId("address-to-insert"); addressToInsert.setUserId("for-address-test");
        addressToInsert.setPhoneNumber("address-to-insert"); addressToInsert.setAddressInfo("address-to-insert");

        nullAddress = null;

        nullUserIDAddress = new Address(); nullUserIDAddress.setIsDefaultAddress(false);
        nullUserIDAddress.setAddressId("null-user-address"); nullUserIDAddress.setUserId(null);
        nullUserIDAddress.setPhoneNumber("null-user-address"); nullUserIDAddress.setAddressInfo("null-user-address");

        nullIDAddress = new Address(); nullIDAddress.setIsDefaultAddress(false);
        nullIDAddress.setAddressId(null); nullIDAddress.setUserId("for-address-test");
        nullIDAddress.setPhoneNumber("null-id-address"); nullIDAddress.setAddressInfo("null-id-address");

        addressExist = new Address(); addressExist.setIsDefaultAddress(false);
        addressExist.setAddressInfo("address-exist-after-insert"); addressExist.setUserId("for-address-test");
        addressExist.setPhoneNumber("address-exist-after-insert"); addressExist.setAddressInfo("DO NOT DELETE MANUALLY");

        nullInfoAddress = new Address(); nullInfoAddress.setIsDefaultAddress(false);
        nullInfoAddress.setAddressId("nullInfoAddress"); nullInfoAddress.setUserId("for-address-test");
        nullInfoAddress.setPhoneNumber("nullInfoAddress"); nullInfoAddress.setAddressInfo(null);

        nullPhoneAddress = new Address(); nullPhoneAddress.setIsDefaultAddress(false);
        nullPhoneAddress.setAddressId("nullPhoneAddress"); nullPhoneAddress.setUserId("for-address-test");
        nullPhoneAddress.setPhoneNumber(null); nullPhoneAddress.setAddressInfo("nullPhoneAddress");

        nullDefaultAddress = new Address();
        nullDefaultAddress.setAddressId("nullDefaultAddress"); nullDefaultAddress.setUserId("for-address-test");
        nullDefaultAddress.setPhoneNumber("nullDefaultAddress"); nullDefaultAddress.setAddressInfo("nullDefaultAddress");

        addressNotExist = new Address(); addressNotExist.setIsDefaultAddress(false);
        addressNotExist.setAddressId("addressNotExist"); addressNotExist.setUserId("for-address-test");
        addressNotExist.setPhoneNumber("addressNotExist"); addressNotExist.setAddressInfo("addressNotExist");

        addressToDelete = new Address();
        addressToDelete.setAddressId("address-to-delete");

        addressToUpdate = new Address(); addressToUpdate.setIsDefaultAddress(true);
        addressToUpdate.setAddressId("address-to-update"); addressToUpdate.setUserId("for-address-test");
    }

    @Test
    public void exceptionInsertAddress() {
        try {
            addressService.insertAddress(nullAddress);
        } catch (AddressException e) {
            assertEquals(ADDRESS_EMPTY, e.getCode().longValue());
        }
        try {
            addressService.insertAddress(nullUserIDAddress);
        } catch (AddressException e) {
            assertEquals(USER_EMPTY, e.getCode().longValue());
        }
        try {
            addressService.insertAddress(nullInfoAddress);
        } catch (AddressException e) {
            assertEquals(ADDRESS_EMPTY, e.getCode().longValue());
        }
        try {
            addressService.insertAddress(nullPhoneAddress);
        } catch (AddressException e) {
            assertEquals(ADDRESS_EMPTY, e.getCode().longValue());
        }
    }

    @Test
    public void insertAddress() {
        addressService.insertAddress(nullIDAddress);
        addressService.insertAddress(addressExist);
        addressService.insertAddress(addressToInsert);
        addressService.insertAddress(nullDefaultAddress);
    }

    @Test
    public void exceptionUpdateAddress() {

    }

    @Test
    public void updateAddress() {
        addressService.updateAddress(nullIDAddress);
        addressService.updateAddress(nullDefaultAddress);

        addressService.updateAddress(addressToUpdate);
    }

    @Test
    public void exceptionDeleteAddress() {
        try {
            addressService.deleteAddress(nullAddress);
        } catch (AddressException e) {
            assertEquals(ADDRESS_EMPTY, e.getCode().longValue());
        }
    }

    @Test
    public void deleteAddress() {
        addressService.deleteAddress(nullIDAddress);
        addressService.deleteAddress(addressToDelete);
    }

    @Test
    public void getAllAddresses() {
        List<Address> addressList = addressService.getAllAddresses("for-address-test");
        System.out.println(addressList.size());
        addressList = addressService.getAllAddresses("worng ");
        assertEquals(0, addressList.size());
    }
}