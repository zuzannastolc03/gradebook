package com.zuzannastolc.gradebook.controller;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;
import com.zuzannastolc.gradebook.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppRestController {
    private AppService appService;

    @Autowired
    public AppRestController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String homePage() {
        return "This is a home page of a gradebook.";
    }

    @GetMapping("/teachers")
    public String teachersSection() {
        return "Welcome to teachers' section!";
    }

    @GetMapping("/students")
    public String studentsSection() {
        return "Welcome to students' section!";
    }

    @GetMapping("/logged_username")
    public String getLoggedUsername(Authentication authentication) {
        String username;
        try {
            username = authentication.getName();
        } catch (Exception ex) {
            username = "Nobody has logged in!";
        }
        return username;
    }

    @GetMapping("/logged_authorities")
    public String getLoggedAuthorities(Authentication authentication) {
        String authorities;
        try {
            authorities = authentication.getAuthorities().toString();
        } catch (Exception ex) {
            authorities = "Nobody has logged in!";
        }
        return authorities;
    }

    @PostMapping("/add_new_user")
    public ResponseEntity addNewUser(@RequestBody WebUser webUser) {
        String username = webUser.getUsername();
        User user = appService.findUserByUsername(username);
        if (user != null){
            String errorMsg = "Username already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewUserWithAuthorities(webUser);
        String msg = "Added new user: " + webUser.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
    @PostMapping("/add_new_student")
    public ResponseEntity addNewStudent(@RequestBody Student student) {
        appService.addNewStudent(student);
        String msg = "Added new student: " + student.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/add_new_teacher")
    public ResponseEntity addNewTeacher(@RequestBody Teacher teacher) {
        appService.addNewTeacher(teacher);
        String msg = "Added new teacher: " + teacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

}
