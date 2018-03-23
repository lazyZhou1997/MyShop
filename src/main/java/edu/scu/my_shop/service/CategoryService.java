package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.FirstCategoryMapper;
import edu.scu.my_shop.dao.SecondCategoryMapper;
import edu.scu.my_shop.entity.FirstCategory;
import edu.scu.my_shop.entity.FirstCategoryExample;
import edu.scu.my_shop.entity.SecondCategory;
import edu.scu.my_shop.entity.SecondCategoryExample;
import edu.scu.my_shop.exception.CategoryServiceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    public static String DEFAULT_CATEGORY = "默认";
    public static int MAX_COUNT_OF_CATEGORY = 6; //最大分类数

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 添加一级分类
     *
     * @param firstCategoryName
     */
    public void addFirstCategory(String firstCategoryName) {

        //检查输入
        if (null == firstCategoryName) {
            throw new CategoryServiceException(CategoryServiceException.INVALID_INPUT_MESSAGE, CategoryServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        FirstCategoryMapper firstCategoryMapper = sqlSession.getMapper(FirstCategoryMapper.class);

        //检查是否已经存在一级分类
        FirstCategoryExample firstCategoryExample = new FirstCategoryExample();
        firstCategoryExample.createCriteria().andFirstCategoryNameEqualTo(firstCategoryName);
        List<FirstCategory> firstCategories = firstCategoryMapper.selectByExample(firstCategoryExample);
        if (!firstCategories.isEmpty()) {
            throw new CategoryServiceException(CategoryServiceException.FIRST_CATEGORYNAME_EXIST_MESSAGE, CategoryServiceException.FIRST_CATEGORYNAME_EXIST);
        }

        //添加分类到数据库
        FirstCategory firstCategory = new FirstCategory();
        firstCategory.setFirstCategoryId(UUID.randomUUID().toString());
        firstCategory.setFirstCategoryName(firstCategoryName);
        firstCategoryMapper.insert(firstCategory);

        sqlSession.close();

    }


    /**
     * 传入一个一级分类对象，修改一级分类名称
     * @param firstCategory
     */
    public void changeFirstCategory(FirstCategory firstCategory){

        //检查输入
        if (null == firstCategory) {
            throw new CategoryServiceException(CategoryServiceException.INVALID_INPUT_MESSAGE, CategoryServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        FirstCategoryMapper firstCategoryMapper = sqlSession.getMapper(FirstCategoryMapper.class);

        //检查是否已经存在一级分类
        FirstCategoryExample firstCategoryExample = new FirstCategoryExample();
        firstCategoryExample.createCriteria().andFirstCategoryNameEqualTo(firstCategory.getFirstCategoryName());
        List<FirstCategory> firstCategories = firstCategoryMapper.selectByExample(firstCategoryExample);
        if (!firstCategories.isEmpty()) {
            throw new CategoryServiceException(CategoryServiceException.FIRST_CATEGORYNAME_EXIST_MESSAGE, CategoryServiceException.FIRST_CATEGORYNAME_EXIST);
        }

        //更新一级分类
        firstCategoryMapper.updateByPrimaryKeySelective(firstCategory);

        sqlSession.close();

    }

    /**
     * 获取所有的一级分类
     */
    public List<FirstCategory> getAllFirstCategory(){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        FirstCategoryMapper firstCategoryMapper = sqlSession.getMapper(FirstCategoryMapper.class);

        //获取所有一级分类
        List<FirstCategory> firstCategories = firstCategoryMapper.selectByExample(new FirstCategoryExample());

        //判断或有一级分类是否为为空，为空则抛出异常
        if (firstCategories.isEmpty()){
            throw new CategoryServiceException(CategoryServiceException.NO_FIRST_CATEGORYNAME_MESSAGE,CategoryServiceException.NO_FIRST_CATEGORYNAME);
        }

        sqlSession.close();
        return firstCategories;
    }

    /**
     * 根据传入的一级分类id，获取一级分类下的二级分类
     * @param firstCategoryId
     * @return
     */
    public List<SecondCategory> getAllSecondCategory(String firstCategoryId){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        SecondCategoryMapper secondCategoryMapper = sqlSession.getMapper(SecondCategoryMapper.class);

        //获取二级分类
        SecondCategoryExample secondCategoryExample = new SecondCategoryExample();
        secondCategoryExample.createCriteria().andFirstCategoryIdEqualTo(firstCategoryId);
        List<SecondCategory> secondCategories = secondCategoryMapper.selectByExample(secondCategoryExample);

        //判断或有二级分类是否为为空，为空则抛出异常
        if (secondCategories.isEmpty()){
            throw new CategoryServiceException(CategoryServiceException.NO_SECOND_CATEGORYNAME_MESSAGE,CategoryServiceException.NO_SECOND_CATEGORYNAME);
        }

        sqlSession.close();
        return secondCategories;

    }

}
