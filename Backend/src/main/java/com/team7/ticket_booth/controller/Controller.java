package com.team7.ticket_booth.controller;
import com.team7.ticket_booth.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
    private Movie movie;

    @Autowired
    public Controller(Movie movie, JsonComponentModule jsonComponentModule) {
        this.movie = movie;
    }

    @RequestMapping("/")
    public String index() {
        return "Hello World!";
    }

    @RequestMapping("/movieInfo")
    public Movie movieInfo() {
       return movie;
    }
}
