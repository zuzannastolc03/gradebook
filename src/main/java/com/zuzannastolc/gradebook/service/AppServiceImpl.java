package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.dao.AppDAO;
import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppServiceImpl implements AppService{
    private AppDAO appDAO;

    @Autowired
    public AppServiceImpl(AppDAO appDAO){
        this.appDAO = appDAO;
    }

    @Override
    @Transactional
    public Student addNewStudent(Student student) {
        return appDAO.addNewStudent(student);
    }

    @Override
    @Transactional
    public Teacher addNewTeacher(Teacher teacher) {
        return appDAO.addNewTeacher(teacher);
    }


}
