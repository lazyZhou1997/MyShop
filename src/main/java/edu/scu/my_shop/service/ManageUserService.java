package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.entity.UserExample;
import edu.scu.my_shop.exception.ManageUserException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.scu.my_shop.exception.ManageUserException.*;

@Service
public class ManageUserService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    /**
     * 任命指定userId的用户为管理员
     * @param userId
     */
    @Transactional
    public void appointSuperUser(String userId){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //判断输入是否有效
        if (null==userId||null==userMapper.selectByPrimaryKey(userId)){

            //输入无效，抛出"非法输入"异常
            throw new ManageUserException(ManageUserException.INVALID_INPUT_MESSAGE,ManageUserException.INVALID_INPUT);
        }

        //设置指定userId的用户为管理员
        User user = new User();
        user.setUserId(userId);
        user.setRole(true);//管理员
        userMapper.updateByPrimaryKeySelective(user);

        sqlSession.close();
    }

    /**
     * Get all user in database.
     * @return
     */
    public List<User> getAllUser() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.selectByExample(new UserExample());
        sqlSession.close();
        return userList;
    }


    /**
     * 管理员删除用户
     * @param userId
     */
    public void deleteUser(String userId){

        //检查输入
        if (null==userId){
            throw new ManageUserException(INVALID_INPUT_MESSAGE,INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        userMapper.deleteByPrimaryKey(userId);

        sqlSession.close();

    }
}
