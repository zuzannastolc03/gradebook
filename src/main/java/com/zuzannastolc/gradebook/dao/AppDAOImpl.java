package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AppDAOImpl implements AppDAO{
    private EntityManager entityManager;
    @Autowired
    public AppDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Student addNewStudent(Student student) {
        Student tempStudent = entityManager.merge(student);
        return tempStudent;
    }

    @Override
    public Teacher addNewTeacher(Teacher teacher) {
        Teacher tempTeacher = entityManager.merge(teacher);
        return tempTeacher;
    }
}
