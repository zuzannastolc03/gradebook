package com.zuzannastolc.gradebook.controller;

import com.zuzannastolc.gradebook.entity.*;
import com.zuzannastolc.gradebook.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public ResponseEntity<?> addNewUser(@RequestBody WebUser webUser) {
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
    public ResponseEntity<?> addNewStudent(@RequestBody Student student, @RequestParam String className) {
        SchoolClass schoolClass = appService.findClassByClassName(className);
        if (schoolClass == null) {
            String errorMsg = "Class: " + className + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewStudent(student, schoolClass);
        String msg = "Added a new student: " + student.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/add_new_teacher")
    public ResponseEntity<?> addNewTeacher(@RequestBody Teacher teacher) {
        appService.addNewTeacher(teacher);
        String msg = "Added a new teacher: " + teacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/disable_user")
    public ResponseEntity<?> disableUser(@RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
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
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestParam String newPassword) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        appService.changePassword(user, newPassword);
        String msg = "Password changed correctly.";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/update_student")
    public ResponseEntity<?> updateStudent(@RequestBody Student student, @RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            String errorMsg = "User: " + username + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Student tempStudent = user.getStudent();
        if (tempStudent == null) {
            String errorMsg = "Something went wrong. Probably indicated user is not a student.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        appService.updateStudentWithUser(student, tempStudent);
        String msg = "Updated from student: " + username + " to student: " + tempStudent.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/update_teacher")
    public ResponseEntity<?> updateTeacher(@RequestBody Teacher teacher, @RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            String errorMsg = "User: " + username + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Teacher tempTeacher = user.getTeacher();
        if (tempTeacher == null) {
            String errorMsg = "Something went wrong. Probably indicated user is not a teacher.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        appService.updateTeacherWithUser(teacher, tempTeacher);
        String msg = "Updated from teacher: " + username + " to teacher: " + tempTeacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/add_new_class")
    public ResponseEntity<?> addNewClass(@RequestBody SchoolClass schoolClass) {
        SchoolClass tempSchoolClass = appService.findClassByClassName(schoolClass.getClassName());
        if (tempSchoolClass != null) {
            String errorMsg = "Class name: " + schoolClass.getClassName() + " already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewClass(schoolClass);
        String msg = "Added a new class: " + schoolClass.getClassName();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }


    @GetMapping("/classes_list")
    public List<?> findAllClasses() {
        return appService.findAllClasses();
    }

    @GetMapping("/students_in_class")
    public List<?> getStudentsInClass(@RequestParam String className) {
        return appService.getStudentsInClass(className);
    }

    @PostMapping("/add_new_subject")
    public ResponseEntity<?> addNewSubject(@RequestBody Subject subject) {
        Subject tempSubject = appService.findSubjectBySubjectName(subject.getSubjectName());
        if (tempSubject != null) {
            String errorMsg = "Subject name: " + subject.getSubjectName() + " already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewSubject(subject);
        String msg = "Added a new subject: " + subject.getSubjectName();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping("/assign_teacher_to_subject")
    public ResponseEntity<?> assignTeacherToSubject(@RequestParam String username, @RequestParam String subjectName) {
        Teacher teacher = appService.findTeacherByUsername(username);
        if (teacher == null) {
            String errorMsg = "Teacher: " + username + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            String errorMsg = "Subject: " + subjectName + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        for (Subject s : teacher.getSubjects()) {
            if (s == subject) {
                String errorMsg = "Teacher: " + username + " is already assigned to subject: " + subjectName + ".";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
            }
        }
        appService.assignTeacherToSubject(teacher, subject);
        String msg = "Successfully assigned teacher: " + username + " to subject: " + subjectName + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @GetMapping("/list_of_teachers_subjects")
    public List<Subject> getListOfTeachersSubjects(Authentication authentication) {
        String username = appService.getLoggedUsername(authentication);
        Teacher teacher = appService.findTeacherByUsername(username);
        if (!teacher.getUser().isEnabled()) {
            throw new RuntimeException("Teacher: " + username + "doesn't teach anymore.");
        }
        return appService.getListOfTeachersSubjects(teacher);
    }

    @GetMapping("/list_of_subjects_teachers")
    public List<Teacher> getListOfSubjectsTeachers(@RequestParam String subjectName) {
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        return appService.getListOfSubjectsTeachers(subject);
    }

    @PostMapping("/assign_class_to_subject")
    public ResponseEntity<?> assignClassToSubject(@RequestParam String className, @RequestParam String subjectName) {
        SchoolClass schoolClass = appService.findClassByClassName(className);
        if (schoolClass == null) {
            String errorMsg = "Class: " + className + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            String errorMsg = "Subject: " + subjectName + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        for (Subject s : schoolClass.getSubjects()) {
            if (s == subject) {
                String errorMsg = "Class: " + className + " is already assigned to subject: " + subjectName + ".";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
            }
        }
        appService.assignClassToSubject(schoolClass, subject);
        String msg = "Successfully assigned class: " + className + " to subject: " + subjectName + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @GetMapping("/list_of_classes_subjects")
    public List<Subject> getListOfClassesSubjects(Authentication authentication) {
        String username = appService.getLoggedUsername(authentication);
        Student student = appService.findStudentByUsername(username);
        SchoolClass schoolClass = appService.findClassByClassName(student.getSchoolClass().getClassName());
        return appService.getListOfClassesSubjects(schoolClass);
    }

    @GetMapping("/list_of_subjects_classes")
    public List<SchoolClass> getListOfSubjectsClasses(@RequestParam String subjectName) {
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        return appService.getListOfSubjectsClasses(subject);
    }

    @GetMapping("/list_of_my_grades_from_subject")
    public List<?> getStudentsGradesFromSubject(Authentication authentication, @RequestParam String subjectName) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        Student student = user.getStudent();
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        SchoolClass schoolClass = student.getSchoolClass();
        List<Subject> subjects = schoolClass.getSubjects();
        if (!subjects.contains(subject)) {
            throw new RuntimeException("Class: " + schoolClass.getClassName() + " doesn't learn " + subject.getSubjectName() + ".");
        }
        return appService.getStudentsGradesFromSubject(student, subject);
    }

    @GetMapping("/list_of_students_grades_from_subject")
    public List<?> getStudentsGradesFromSubject(@RequestParam String username, @RequestParam String subjectName) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User: " + username + " doesn't exist.");
        }
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("User: " + username + " is not a student.");
        }
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        SchoolClass schoolClass = student.getSchoolClass();
        List<Subject> subjects = schoolClass.getSubjects();
        if (!subjects.contains(subject)) {
            throw new RuntimeException("Class: " + schoolClass.getClassName() + " doesn't learn " + subject.getSubjectName() + ".");
        }
        return appService.getStudentsGradesFromSubject(student, subject);
    }

    @PostMapping("/add_new_grade")
    public ResponseEntity<?> addNewGrade(@RequestBody Grade grade, @RequestParam String subjectName, @RequestParam String studentsUsername, Authentication authentication) {
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            String errorMsg = "Subject: " + subjectName + " doesn't exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        String username = appService.getLoggedUsername(authentication);
        Teacher teacher = appService.findTeacherByUsername(username);
        List<?> teachersSubjects = appService.getListOfTeachersSubjects(teacher);
        if (!teachersSubjects.contains(subject)) {
            String errorMsg = "You don't teach: " + subjectName + ".";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        Student student = appService.findStudentByUsername(studentsUsername);
        if (student == null) {
            String errorMsg = "Student: " + studentsUsername + " doesn't exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addGrade(grade, teacher, subject, student);
        String msg = "Added a new grade: " + grade.getGrade() + ", " + grade.getDescription();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PutMapping("/update_grade")
    public ResponseEntity<?> updateGrade(@RequestBody Grade grade, @RequestParam int gradeId, Authentication authentication) {
        Grade tempGrade = appService.findGradeById(gradeId);
        if (tempGrade == null) {
            String errorMsg = "Grade id: " + gradeId + " doesn't exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        String username = appService.getLoggedUsername(authentication);
        Teacher teacher = appService.findTeacherByUsername(username);
        if (teacher.getTeacherId() != tempGrade.getTeacher().getTeacherId()) {
            String errorMsg = "You didn't post this grade: " + tempGrade.getGrade() + ", " + tempGrade.getDescription() + ".";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        tempGrade.setGrade(grade.getGrade());
        tempGrade.setDescription(grade.getDescription());
        appService.updateGrade(tempGrade);
        String msg = "Successfully updated to grade: " + tempGrade.getGrade() + ", " + tempGrade.getDescription();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @DeleteMapping("/delete_grade")
    public ResponseEntity<?> deleteGrade(Authentication authentication, @RequestParam int gradeId) {
        Grade grade = appService.findGradeById(gradeId);
        if (grade == null) {
            String errorMsg = "Grade id: " + gradeId + " doesn't exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        String username = appService.getLoggedUsername(authentication);
        Teacher teacher = appService.findTeacherByUsername(username);
        if (teacher.getTeacherId() != grade.getTeacher().getTeacherId()) {
            String errorMsg = "You didn't post this grade: " + grade.getGrade() + ", " + grade.getDescription() + ".";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.deleteGrade(gradeId);
        String msg = "Successfully deleted grade id: " + gradeId + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @GetMapping("/my_mean_from_subject")
    public Float getStudentsMeanFromSubject(Authentication authentication, @RequestParam String subjectName) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        Student student = user.getStudent();
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        SchoolClass schoolClass = student.getSchoolClass();
        List<Subject> subjects = schoolClass.getSubjects();
        if (!subjects.contains(subject)) {
            throw new RuntimeException("Class: " + schoolClass.getClassName() + " doesn't learn " + subject.getSubjectName() + ".");
        }
        return appService.calculateMean(student, subject);
    }

    @GetMapping("/mean_of_students_grades_from_subject")
    public Float getStudentsMeanFromSubject(@RequestParam String username, @RequestParam String subjectName) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User: " + username + " doesn't exist.");
        }
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("User: " + username + " is not a student.");
        }
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        SchoolClass schoolClass = student.getSchoolClass();
        List<Subject> subjects = schoolClass.getSubjects();
        if (!subjects.contains(subject)) {
            throw new RuntimeException("Class: " + schoolClass.getClassName() + " doesn't learn " + subject.getSubjectName() + ".");
        }
        return appService.calculateMean(student, subject);
    }

}
