package com.example.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MiddlewareApplication {

    public static void main(String[] args) {
         SpringApplication.run(MiddlewareApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
