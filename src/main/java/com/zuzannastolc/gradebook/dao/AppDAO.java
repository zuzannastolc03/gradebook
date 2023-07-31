package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppDAO {
    String getLoggedUsername(Authentication authentication);
    String getLoggedAuthorities(Authentication authentication);
    void addNewUserWithAuthorities(User user);
    User findUserByUsername(String username);
    void addNewStudent(Student student);
    void addNewTeacher(Teacher teacher);
    void updateUser(User user);
    void updateStudent(Student student);
    void updateTeacher(Teacher teacher);
    List<String> findAllClasses();
    SchoolClass findClassByClassName(String className);
    void addNewClass(SchoolClass schoolClass);
    List<?> getEnabledStudentsInClass(String className);
    Subject findSubjectBySubjectName(String subjectName);
    void addNewSubject(Subject subject);
    void updateSubject(Subject subject);
    List<?> getStudentsGradesFromSubject(Student student, Subject subject);
    void addGrade(Grade grade);
    void updateGrade(Grade grade);
    Grade findGradeById(int id);
    void deleteGrade(int id);

}
