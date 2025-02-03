package org.example.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/admin")
    public String test1(){
        return "This is for admin";
    }

    @PostMapping("/user")
    public String test2(){
        return "This is for user";
    }
}
