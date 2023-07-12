package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;


public interface AppDAO {

    Student addNewStudent(Student student);
    Teacher addNewTeacher(Teacher teacher);
}
