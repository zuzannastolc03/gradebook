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

import java.util.Objects;

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
        return appService.getLoggedUsername(authentication);
    }

    @GetMapping("/logged_authorities")
    public String getLoggedAuthorities(Authentication authentication) {
        return appService.getLoggedAuthorities(authentication);
    }

    @PostMapping("/add_new_user")
    public ResponseEntity addNewUser(@RequestBody WebUser webUser) {
        String username = webUser.getUsername();
        User user = appService.findUserByUsername(username);
        if (user != null) {
            String errorMsg = "Username already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewUserWithAuthorities(webUser);
        String msg = "Added a new user: " + webUser.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/add_new_student")
    public ResponseEntity addNewStudent(@RequestBody Student student) {
        appService.addNewStudent(student);
        String msg = "Added a new student: " + student.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/add_new_teacher")
    public ResponseEntity addNewTeacher(@RequestBody Teacher teacher) {
        appService.addNewTeacher(teacher);
        String msg = "Added a new teacher: " + teacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/disable_user")
    public ResponseEntity disableUser(@RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if(user == null){
            String errorMsg = "Something went wrong. Probably the username is incorrect.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        } else if (!user.isEnabled()) {
            String errorMsg = "User: " + user.getUsername() + " has already been disabled.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.disableUser(user);
        String msg = "Disabled user: " + user.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/change_password")
    public ResponseEntity changePassword(Authentication authentication, @RequestParam String oldPassword, @RequestParam String newPassword) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        if(!Objects.equals(user.getPassword(), "{noop}"+oldPassword)) {
            String errorMsg = "Your password is incorrect. Try again.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
        }
        appService.changePassword(user, newPassword);
        String msg = "Password changed correctly.";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/update_student")
    public ResponseEntity updateStudent(@RequestBody Student student, @RequestParam String username){
        User user = appService.findUserByUsername(username);
        if(user == null){
            String errorMsg = "User: " + username + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Student tempStudent = user.getStudent();
        if(tempStudent == null){
            String errorMsg = "Something went wrong. Probably indicated user is not a student.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        appService.updateStudentWithUser(student, tempStudent);
        String msg = "Updated from student: " + username + " to student: " + tempStudent.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/update_teacher")
    public ResponseEntity updateTeacher(@RequestBody Teacher teacher, @RequestParam String username){
        User user = appService.findUserByUsername(username);
        if(user == null){
            String errorMsg = "User: " + username + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Teacher tempTeacher = user.getTeacher();
        if(tempTeacher == null){
            String errorMsg = "Something went wrong. Probably indicated user is not a teacher.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        appService.updateTeacherWithUser(teacher, tempTeacher);
        String msg = "Updated from teacher: " + username + " to teacher: " + tempTeacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
}
