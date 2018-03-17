package edu.scu.my_shop.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ControllerService {

    /**
     * 密码加密工具类
     */
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);


}
