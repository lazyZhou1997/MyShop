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
     * @param user
     */
    @Transactional
    public void changeUserInfo(User user){

        //获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //确保用户名不重复
        if (null!=user.getUserName()){
            UserExample userExample = new UserExample();
            userExample.createCriteria().andUserNameEqualTo(user.getUserName());

            if (null!=userMapper.selectByExample(userExample)){
                //如果用户名重复，抛出异常
                throw new ChangeUserInfoException("用户名已经存在");
            }
        }



        //加密用户密码
        if (null!=user.getUserPassword()){
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }

        //更新用户信息
        userMapper.updateByPrimaryKeySelective(user);

        sqlSession.close();

        return;
    }

}
