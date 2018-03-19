package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.User;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * ChangeUserInfoService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 19, 2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChangeUserInfoServiceTest {

    @Autowired
    ChangeUserInfoService changeUserInfoService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: changeUserInfo(User user)
     * 测试通过
     */
    @Test
    public void testChangeUserInfo() throws Exception {

        User user = new User();
        user.setBirthday(new Date());
        //user.setUserName("啦啦");
        user.setHeadImg("./d");
        user.setUserId("12@qq.com");
        user.setUserPassword("12");

        changeUserInfoService.changeUserInfo(user);
    }

    /**
     * Method: getUserInfo()
     */
    @Test
    public void testGetUserInfo() throws Exception {

    }


} 
