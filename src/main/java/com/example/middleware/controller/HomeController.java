/**
 * Created By shoh@n
 * Date: 3/29/2023
 */

package com.example.middleware.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Hidden
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
