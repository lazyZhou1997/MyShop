package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.entity.UserExample;
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
        if (null == user || null == user.getUserId()||null==user.getUserPassword()||null==user.getUserName()){

            throw new RegisterException(RegisterException.INVALID_INPUT_MESSAGE,RegisterException.INVALID_INPUT);

        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //看看用户email是否已经存在,如果已经存在
        if (null != userMapper.selectByPrimaryKey(user.getUserId())) {

            throw new RegisterException(RegisterException.EMAIL_HAS_EXIST_MESSAGE,RegisterException.EMAIL_HAS_EXIST);
        }

        //看看userName是否已经存在
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(user.getUserName());
        if (!userMapper.selectByExample(userExample).isEmpty()){
            throw new RegisterException(RegisterException.USERNAME_HAS_EXIST_MESSAGE,RegisterException.USERNAME_HAS_EXIST);
        }

        //设置角色为普通用户
        user.setRole(false);
        //设置头像路径为
        user.setHeadImg("./portrait.png");
        //将用户密码进行加密后，保存到数据库中
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userMapper.insert(user);


        sqlSession.close();

        return;
    }

}
