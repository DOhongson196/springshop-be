package com.example.springshopbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringShopBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShopBeApplication.class, args);
    }

}
