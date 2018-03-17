package edu.scu.my_shop.service;

import edu.scu.my_shop.result.Result;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public Result login(){

        //获取

        return null;
    }
}
