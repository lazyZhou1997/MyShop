package edu.scu.my_shop.service;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ManageUserService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 19, 2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManageUserServiceTest {

    @Autowired
    private ManageUserService manageUserService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 测试设置用户为管理员
     */
    @Test
    public void testAppointSuperUser() throws Exception {

        manageUserService.appointSuperUser("zzz");

    }


} 
