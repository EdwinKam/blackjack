package com.edwinkam.blackjack.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @CrossOrigin
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World from springboot!";
    }
}