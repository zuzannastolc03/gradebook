package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.dao.AppDAO;
import com.zuzannastolc.gradebook.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppServiceImpl implements AppService {
    private AppDAO appDAO;

    @Autowired
    public AppServiceImpl(AppDAO appDAO) {
        this.appDAO = appDAO;
    }

    @Override
    @Transactional
    public void addNewUserWithAuthorities(WebUser webUser) {
        User user = new User(webUser.getUsername(), "{noop}" + webUser.getPassword(), true);
        for (String authority : webUser.getAuthorities()) {
            Authority tempAuthority = new Authority(authority);
            user.addAuthority(tempAuthority);
        }
        appDAO.addNewUserWithAuthorities(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return appDAO.findUserByUsername(username);
    }


    @Override
    @Transactional
    public void addNewStudent(Student student) {
        String username = generateUsername(student.getFirstName(), student.getLastName(), true, 0);
        String password = "{noop}" + student.getFirstName().toLowerCase();
        User user = new User(username, password, true);
        Authority authority = new Authority("ROLE_STUDENT");
        user.addAuthority(authority);
        student.setUser(user);
        appDAO.addNewStudent(student);
    }

    @Override
    @Transactional
    public void addNewTeacher(Teacher teacher) {
        String username = generateUsername(teacher.getFirstName(), teacher.getLastName(), false,0);
        String password = "{noop}" + teacher.getFirstName().toLowerCase();
        User user = new User(username, password, true);
        Authority authority = new Authority("ROLE_TEACHER");
        user.addAuthority(authority);
        teacher.setUser(user);
        appDAO.addNewTeacher(teacher);
    }

    public String generateUsername(String firstName, String lastName, boolean isAStudent, int i) {
        String username = firstName.toLowerCase() + "." + lastName.toLowerCase();
        if (i != 0) {
            username = username + String.valueOf(i);
        }
        if (isAStudent) {
            username = username + "@student.school.com";
        }
        else{
            username = username + "@school.com";
        }
        User user = findUserByUsername(username);
        if (user != null) {
            return generateUsername(firstName, lastName, isAStudent, i+1);
        } else {
            return username;
        }
    }
}
