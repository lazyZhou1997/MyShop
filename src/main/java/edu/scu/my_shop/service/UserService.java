package edu.scu.my_shop.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;

@Service
public class UserService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Result login(){

        SqlSession sqlSession = sqlSessionFactory.openSession();

        return null;
    }
}
