package edu.scu.my_shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.scu.my_shop.dao")
public class MyShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopApplication.class, args);


    }
}
