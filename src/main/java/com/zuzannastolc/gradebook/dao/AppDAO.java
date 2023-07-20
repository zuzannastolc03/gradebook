package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.User;

public interface AppDAO {

//    Student addNewStudent(Student student);
//    Teacher addNewTeacher(Teacher teacher);
    void addNewUserWithAuthorities(User user);
    User findByUsername(String username);
}
