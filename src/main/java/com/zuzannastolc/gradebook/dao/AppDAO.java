package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;

public interface AppDAO {
    void addNewUserWithAuthorities(User user);
    User findUserByUsername(String username);
    void addNewStudent(Student student);
    void addNewTeacher(Teacher teacher);
}
