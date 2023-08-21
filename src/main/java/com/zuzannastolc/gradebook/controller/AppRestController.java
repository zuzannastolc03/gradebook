package com.zuzannastolc.gradebook.controller;

import com.zuzannastolc.gradebook.entity.*;
import com.zuzannastolc.gradebook.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AppRestController {
    private AppService appService;

    @Autowired
    public AppRestController(AppService appService) {
        this.appService = appService;
    }

    @Operation(summary = "Home page.")
    @GetMapping("/")
    public String homePage() {
        return "This is a home page of a gradebook.";
    }

    @Operation(summary = "Home page of teachers' section.")
    @GetMapping("/teachers")
    public String teachersSection() {
        return "Welcome to teachers' section!";
    }

    @Operation(summary = "Home page of teachers' section.")
    @GetMapping("/students")
    public String studentsSection() {
        return "Welcome to students' section!";
    }

    @Operation(summary = "Gets data about who is logged in.")
    @GetMapping("/logged_username")
    public String getLoggedUsername(Authentication authentication) {
        return appService.getLoggedUsername(authentication);
    }

    @Operation(summary = "Gets data about logged in person's authorities.")
    @GetMapping("/logged_authorities")
    public String getLoggedAuthorities(Authentication authentication) {
        return appService.getLoggedAuthorities(authentication);
    }

    @Operation(summary = "Adds a new user with no connection to teachers or students.")
    @PostMapping("/add_new_user")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody WebUser webUser) {
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

    @Operation(summary = "Adds a new student with adequate inserts to users and authorities tables.")
    @PostMapping("/add_new_student")
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody Student student, @RequestParam String className) {
        SchoolClass schoolClass = appService.findClassByClassName(className);
        if (schoolClass == null) {
            String errorMsg = "Class: " + className + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewStudent(student, schoolClass);
        String msg = "Added a new student: " + student.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Adds a new teacher with adequate inserts to users and authorities tables.")
    @PostMapping("/add_new_teacher")
    public ResponseEntity<?> addNewTeacher(@Valid @RequestBody Teacher teacher) {
        appService.addNewTeacher(teacher);
        String msg = "Added a new teacher: " + teacher.getUser().getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Disables a user.")
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

    @Operation(summary = "Changes password of a logged in user.")
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestParam String newPassword) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        appService.changePassword(user, newPassword);
        String msg = "Password changed correctly.";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Updates student's personal data with adequate changes to username and password.")
    @PutMapping("/update_student")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody Student student, @RequestParam String username) {
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

    @Operation(summary = "Updates teacher's personal data with adequate changes to username and password.")
    @PutMapping("/update_teacher")
    public ResponseEntity<?> updateTeacher(@Valid @RequestBody Teacher teacher, @RequestParam String username) {
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

    @Operation(summary = "Adds a new available class to the list.")
    @PostMapping("/add_new_class")
    public ResponseEntity<?> addNewClass(@Valid @RequestBody SchoolClass schoolClass) {
        SchoolClass tempSchoolClass = appService.findClassByClassName(schoolClass.getClassName());
        if (tempSchoolClass != null) {
            String errorMsg = "Class name: " + schoolClass.getClassName() + " already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewClass(schoolClass);
        String msg = "Added a new class: " + schoolClass.getClassName();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }


    @Operation(summary = "Gets a list of all classes in school.")
    @GetMapping("/classes_list")
    public List<?> findAllClasses() {
        return appService.findAllClasses();
    }

    @Operation(summary = "Gets a list of all students in an indicated class.")
    @GetMapping("/students_in_class")
    public List<?> getStudentsInClass(@RequestParam String className) {
        SchoolClass schoolClass = appService.findClassByClassName(className);
        if (schoolClass == null) {
            throw new RuntimeException("Class: " + className + " doesn't exist.");
        }
        return appService.getStudentsInClass(className);
    }

    @Operation(summary = "Adds a new subject to the list.")
    @PostMapping("/add_new_subject")
    public ResponseEntity<?> addNewSubject(@Valid @RequestBody Subject subject) {
        Subject tempSubject = appService.findSubjectBySubjectName(subject.getSubjectName());
        if (tempSubject != null) {
            String errorMsg = "Subject name: " + subject.getSubjectName() + " already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewSubject(subject);
        String msg = "Added a new subject: " + subject.getSubjectName();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Assigns a teacher to a subject.")
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

    @Operation(summary = "Gets a list of all subjects, that an indicated teacher is in charge of.")
    @GetMapping("/list_of_teachers_subjects")
    public List<Subject> getListOfTeachersSubjects(@RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            String errorMsg = "User: " + username + " doesn't exist.";
            throw new RuntimeException(errorMsg);
        }
        Teacher teacher = user.getTeacher();
        if (teacher == null) {
            String errorMsg = "Something went wrong. Probably indicated user is not a teacher.";
            throw new RuntimeException(errorMsg);
        }
        if (!teacher.getUser().isEnabled()) {
            throw new RuntimeException("Teacher: " + username + "doesn't teach anymore.");
        }
        return appService.getListOfTeachersSubjects(teacher);
    }

    @Operation(summary = "Gets a list of all teachers, that teach an indicated subject.")
    @GetMapping("/list_of_subjects_teachers")
    public List<Teacher> getListOfSubjectsTeachers(@RequestParam String subjectName) {
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        return appService.getListOfSubjectsTeachers(subject);
    }

    @Operation(summary = "Assigns an indicated class to a particular subject.")
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

    @Operation(summary = "Gets a list of all subjects, that students from a logged in student class learn.")
    @GetMapping("/list_of_classes_subjects")
    public List<Subject> getListOfClassesSubjects(Authentication authentication) {
        String username = appService.getLoggedUsername(authentication);
        Student student = appService.findStudentByUsername(username);
        SchoolClass schoolClass = appService.findClassByClassName(student.getSchoolClass().getClassName());
        return appService.getListOfClassesSubjects(schoolClass);
    }

    @Operation(summary = "Gets a list of all classes, from which students learn an indicated subject.")
    @GetMapping("/list_of_subjects_classes")
    public List<SchoolClass> getListOfSubjectsClasses(@RequestParam String subjectName) {
        Subject subject = appService.findSubjectBySubjectName(subjectName);
        if (subject == null) {
            throw new RuntimeException("Subject: " + subjectName + " doesn't exist.");
        }
        return appService.getListOfSubjectsClasses(subject);
    }

    @Operation(summary = "Gets a list of grades from an indicated subject of a logged in student.")
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

    @Operation(summary = "Gets a list of grades of an indicated student from a particular subject.")
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

    @Operation(summary = "Adds a new grade for a given student from particular subject by a logged in teacher.")
    @PostMapping("/add_new_grade")
    public ResponseEntity<?> addNewGrade(@Valid @RequestBody Grade grade, @RequestParam String subjectName, @RequestParam String studentsUsername, Authentication authentication) {
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

    @Operation(summary = "Changes a given grade.")
    @PutMapping("/update_grade")
    public ResponseEntity<?> updateGrade(@Valid @RequestBody Grade grade, @RequestParam int gradeId, Authentication authentication) {
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

    @Operation(summary = "Deletes a grade with a given ID.")
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

    @Operation(summary = "Gets grades mean of a logged in student from a particular subject.")
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

    @Operation(summary = "Gets grades mean of a particular student from an indicated subject.")
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
