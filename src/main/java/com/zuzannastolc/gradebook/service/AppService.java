package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;

public interface AppService {
    void addNewUserWithAuthorities(WebUser webUser);
    User findUserByUsername(String username);
    void addNewStudent(Student student);
    void addNewTeacher(Teacher teacher);

}
