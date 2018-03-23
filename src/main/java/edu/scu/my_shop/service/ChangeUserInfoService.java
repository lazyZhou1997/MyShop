package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.entity.UserExample;
import edu.scu.my_shop.exception.ChangeUserInfoException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeUserInfoService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 加密工具类
     */
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    /**
     * 修改用户信息
     *
     * @param user
     */
    @Transactional
    public void changeUserInfo(User user) {

        //获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //确保用户名不重复
        if (null != user.getUserName()) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andUserNameEqualTo(user.getUserName());

            if (!userMapper.selectByExample(userExample).isEmpty()) {
                //如果用户名重复，抛出异常
                throw new ChangeUserInfoException(ChangeUserInfoException.USERNAME_HAS_EXIST_MESSAGE, ChangeUserInfoException.USERNAME_HAS_EXIST);
            }
        }


        //加密用户密码
        if (null != user.getUserPassword()) {
            System.out.println(user.getUserPassword());
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }

        //更新用户信息
        if (null == user.getUserName() && null == user.getUserPassword() &&
                null == user.getHeadImg() && null == user.getBirthday()) {
            throw new ChangeUserInfoException(ChangeUserInfoException.NO_VALUE_MESSAGE, ChangeUserInfoException.NO_VALUE);
        }

        userMapper.updateByPrimaryKeySelective(user);

        sqlSession.close();

        return;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public User getUserInfoByUserId(String userId) {

        //检查输入
        if (null == userId) {
            throw new ChangeUserInfoException(ChangeUserInfoException.INVALID_INPUT_MESSAGE, ChangeUserInfoException.INVALID_INPUT);
        }

        //获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //查询用户信息
        User user = userMapper.selectByPrimaryKey(userId);
        //判断是否为null
        if (null == user) {
            throw new ChangeUserInfoException(ChangeUserInfoException.NO_USER_MESSAGE,ChangeUserInfoException.NO_USER);
        }

        sqlSession.close();
        return user;
    }

    /**
     * Search for database to check if user exists.
     *
     * @param userID
     * @return
     */
    public boolean userExists(String userID) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(userID);

        sqlSession.close();
        return user != null;
    }

}
