package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppService {
    String getLoggedUsername(Authentication authentication);

    String getLoggedAuthorities(Authentication authentication);

    void addNewUserWithAuthorities(WebUser webUser);

    User findUserByUsername(String username);

    void addNewStudent(Student student, SchoolClass schoolClass);

    void addNewTeacher(Teacher teacher);

    void disableUser(User user);

    void changePassword(User user, String newPassword);

    void updateStudentWithUser(Student newStudent, Student oldStudent);

    void updateTeacherWithUser(Teacher newTeacher, Teacher oldTeacher);
    List<String> findAllClasses();
    SchoolClass findClassByClassName(String className);
    void addNewClass(SchoolClass schoolClass);
    List<?> getStudentsInClass(String className);
    Subject findSubjectBySubjectName(String subjectName);
    void addNewSubject(Subject subject);
}
