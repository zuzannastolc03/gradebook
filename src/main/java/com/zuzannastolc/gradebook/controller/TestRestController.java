package com.zuzannastolc.gradebook.controller;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @GetMapping("/logged_username")
    public String getLoggedUsername(Authentication authentication){
        String username;
        try {
            username = authentication.getName();
        }catch (Exception ex){
            username = "Nobody has logged in!";
        }
        return username;
    }
    @GetMapping("/logged_authorities")
    public String getLoggedAuthorities(Authentication authentication){
        String authorities;
        try {
            authorities = authentication.getAuthorities().toString();
        }catch (Exception ex){
            authorities = "Noboby has logged in!";
        }
        return authorities;
    }
}
