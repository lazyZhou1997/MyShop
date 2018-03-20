package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.FirstCategory;
import edu.scu.my_shop.entity.SecondCategory;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * CategoryService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 20, 2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 测试增加一级分类，测试通过
     * Method: addFirstCategory(String firstCategoryName)
     */
    @Test
    public void testAddFirstCategory() throws Exception {

        categoryService.addFirstCategory("衣服");
    }


    /**
     * 测试修改一级分类，测试通过
     *
     * @throws Exception
     */
    @Test
    public void testChangeFirstCategory() throws Exception {

        FirstCategory firstCategory = new FirstCategory();
        firstCategory.setFirstCategoryId("aff361a7-5612-4df1-b9ae-57fa0c1928ff");
        firstCategory.setFirstCategoryName("裤子");

        categoryService.changeFirstCategory(firstCategory);
    }

    /**
     * 测试获取所有的一级分类，测试通过
     *
     * @throws Exception
     */
    @Test
    public void testGetAllFirstCategory() throws Exception {
        List<FirstCategory> firstCategories = categoryService.getAllFirstCategory();

        for (FirstCategory firstCategory :
                firstCategories) {
            System.out.println(firstCategory.getFirstCategoryName());
        }
    }

    /**
     * 测试获取所有的二级分类，测试通过
     * @throws Exception
     */
    @Test
    public void testGetAllSecondCategory() throws Exception {

        List<SecondCategory> secondCategories = categoryService.getAllSecondCategory("asjkxankjdhcajxknaksjx");
        for (SecondCategory secondCategory:
             secondCategories) {
            System.out.println(secondCategory.getSecondCategoryName());
        }
    }
}
