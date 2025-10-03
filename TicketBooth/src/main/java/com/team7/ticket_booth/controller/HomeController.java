package com.team7.ticket_booth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @GetMapping("")
    public String home() {
        return "Welcome to Ticket Booth!";
    }

    @GetMapping("/help")
    public String help() {
        return "This is the help page.";
    }
}
