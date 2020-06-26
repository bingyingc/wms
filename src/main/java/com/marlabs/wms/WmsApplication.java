package com.marlabs.wms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(value = "com.marlabs.Mapper")
//@ComponentScan(basePackages = {"com.marlabs.*"})
public class WmsApplication {

    public static void main(String[] args) {
        System.out.println("here is main");
        SpringApplication.run(WmsApplication.class, args);
    }

}
