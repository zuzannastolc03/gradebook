package com.zuzannastolc.gradebook.controller;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {
    private AppService appService;
    @Autowired
    public AppRestController(AppService appService){
        this.appService = appService;
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
            authorities = "Noboby has logged in!";
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
}
