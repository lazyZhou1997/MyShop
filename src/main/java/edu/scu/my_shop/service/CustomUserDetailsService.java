package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;

        //获得SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //根据用户名查找用户
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        user = userMapper.selectByPrimaryKey(username);

        //没找到用户
        if (null == user) {

            throw new UsernameNotFoundException("username"+username+"not found");
        }
        // SecurityUser实现UserDetails并将user_id映射为username
        SecurityUser securityUser = new SecurityUser(user);

        //关闭
        sqlSession.close();
        return securityUser;
    }
}
