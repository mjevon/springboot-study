package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LeonApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(LeonApplication.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}
