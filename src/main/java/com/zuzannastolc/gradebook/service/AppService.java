package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;

public interface AppService {
    Student addNewStudent(Student student);
    Teacher addNewTeacher(Teacher teacher);
}
