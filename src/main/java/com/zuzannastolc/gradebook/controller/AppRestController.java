package com.zuzannastolc.gradebook.controller;

import com.zuzannastolc.gradebook.entity.*;
import com.zuzannastolc.gradebook.service.AppService;
import com.zuzannastolc.gradebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppRestController {
    private AppService appService;
    private UserService userService;
    @Autowired
    public AppRestController(AppService appService, UserService userService){
        this.appService = appService;
        this.userService = userService;
    }
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
            authorities = "Nobody has logged in!";
        }
        return authorities;
    }
    @PostMapping("/addStudent")
    public Student addStudent(@RequestBody Student student){
        Student dbStudent = appService.addNewStudent(student);
        return dbStudent;
    }

    @PostMapping("/addTeacher")
    public Teacher addTeacher(@RequestBody Teacher teacher){
        Teacher dbTeacher = appService.addNewTeacher(teacher);
        return dbTeacher;
    }

    @PostMapping("/register_user")
    public ResponseEntity registerUser(@RequestBody WebUser theWebUser) {
        String userName = theWebUser.getUserName();
        User user = userService.findByUserName(userName);
        if (user != null){
            String errorMsg = "Username already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }

        List<String> roleName = theWebUser.getRoleName();
        for(String role: roleName){
            Role tempRole = userService.findRoleByName(role);
            if (tempRole == null){
                String errorMsg = "Minimum one invalid role.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
            }
        }


        userService.save(theWebUser);
        return ResponseEntity.ok().build();
    }
}
