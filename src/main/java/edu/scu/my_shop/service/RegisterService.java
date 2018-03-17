package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.RegisterException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {


    /**
     * 密码加密工具类
     */
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     *
     * @param user 要注册的新用户，不能传入为null
     */
    public void register(User user) {

        //如果传入user.user_id为空
        if (null == user || null == user.getUserId()) {
            throw new RegisterException("无效用户名");

        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //看看用户是否已经存在,如果已经存在
        if (null != userMapper.selectByPrimaryKey(user.getUserId())) {

            throw new RegisterException("用户名已经存在");
        }

        //将用户密码进行加密后，保存到数据库中
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userMapper.insert(user);


        sqlSession.close();

        return;
    }

}
