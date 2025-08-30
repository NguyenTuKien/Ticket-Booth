package com.team7.ticket_booth.Controllers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
    @RequestMapping("/")
    public String index() {
        return "Hello World!";
    }

    @RequestMapping("/login")
    public String login() {
        return "Login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "Logout";
    }

    @RequestMapping("/register")
    public String register() {
        return "Register";
    }
}
