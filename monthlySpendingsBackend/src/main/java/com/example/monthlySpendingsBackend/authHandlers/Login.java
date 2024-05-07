package com.example.monthlySpendingsBackend.authHandlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {
    @RequestMapping(path="/login")
    public String loginHandler(){
        return "custom_login.html";
    }

    @RequestMapping(path="/home")
    public String homeHandler(){
        return "index.html";
    }

    @RequestMapping(path="/register")
    public String registrationHandler(){
        return "sign-up.html";
    }
}
