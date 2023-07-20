package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;

public interface AppService {
//    Student addNewStudent(Student student);
//    Teacher addNewTeacher(Teacher teacher);
    void addNewUserWithAuthorities(WebUser webUser);
    User findByUsername(String username);
}
