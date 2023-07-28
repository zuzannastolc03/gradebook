package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;
import org.springframework.security.core.Authentication;

public interface AppService {
    String getLoggedUsername(Authentication authentication);
    String getLoggedAuthorities(Authentication authentication);
    void addNewUserWithAuthorities(WebUser webUser);
    User findUserByUsername(String username);
    void addNewStudent(Student student);
    void addNewTeacher(Teacher teacher);
    void disableUser(User user);
    void changePassword(User user, String newPassword);
    void updateStudentWithUser(Student newStudent, Student oldStudent);
    void updateTeacherWithUser(Teacher newTeacher, Teacher oldTeacher);
}
