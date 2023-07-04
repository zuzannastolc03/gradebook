package com.zuzannastolc.gradebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
    @GetMapping("/")
    public String homePage(){
        return "This is a home page of a gradebook.";
    }
    @GetMapping("/teachers")
    public String teachersSection(){
        return "Welcome to teachers' section!";
    }
    @GetMapping("/students")
    public String studentsSection(){
        return "Welcome to students' section!";
    }
}
