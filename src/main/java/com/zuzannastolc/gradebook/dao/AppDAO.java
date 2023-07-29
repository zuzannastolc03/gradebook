package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.SchoolClass;
import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;
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
    List<?> getStudentsInClass(String className);
}
