package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.dao.AppDAO;
import com.zuzannastolc.gradebook.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {
    private AppDAO appDAO;

    @Autowired
    public AppServiceImpl(AppDAO appDAO) {
        this.appDAO = appDAO;
    }

    @Override
    public String getLoggedUsername(Authentication authentication) {
        return appDAO.getLoggedUsername(authentication);
    }

    @Override
    public String getLoggedAuthorities(Authentication authentication) {
        return appDAO.getLoggedAuthorities(authentication);
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
        String username = generateUsername(teacher.getFirstName(), teacher.getLastName(), false, 0);
        String password = "{noop}" + teacher.getFirstName().toLowerCase();
        User user = new User(username, password, true);
        Authority authority = new Authority("ROLE_TEACHER");
        user.addAuthority(authority);
        teacher.setUser(user);
        appDAO.addNewTeacher(teacher);
    }

    @Override
    @Transactional
    public void disableUser(User user) {
        user.setEnabled(false);
        appDAO.updateUser(user);
    }

    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        user.setPassword("{noop}" + newPassword);
        appDAO.updateUser(user);
    }

    @Override
    @Transactional
    public void updateStudentWithUser(Student newStudent, Student oldStudent) {
        String username = generateUsername(newStudent.getFirstName(), newStudent.getLastName(), true, 0);
        String password = "{noop}" + newStudent.getFirstName().toLowerCase();
        User user = oldStudent.getUser();
        user.setUsername(username);
        user.setPassword(password);
        appDAO.updateUser(user);
        oldStudent.setFirstName(newStudent.getFirstName());
        oldStudent.setLastName(newStudent.getLastName());
        appDAO.updateStudent(oldStudent);
    }

    @Override
    @Transactional
    public void updateTeacherWithUser(Teacher newTeacher, Teacher oldTeacher) {
        String username = generateUsername(newTeacher.getFirstName(), newTeacher.getLastName(), false, 0);
        String password = "{noop}" + newTeacher.getFirstName().toLowerCase();
        User user = oldTeacher.getUser();
        user.setUsername(username);
        user.setPassword(password);
        appDAO.updateUser(user);
        oldTeacher.setFirstName(newTeacher.getFirstName());
        oldTeacher.setLastName(newTeacher.getLastName());
        appDAO.updateTeacher(oldTeacher);
    }

    @Override
    public List<String> findAllClasses() {
        return appDAO.findAllClasses();
    }

    @Override
    public SchoolClass findClassByClassName(String className) {
        return appDAO.findClassByClassName(className);
    }

    @Override
    @Transactional
    public void addNewClass(SchoolClass schoolClass) {
        appDAO.addNewClass(schoolClass);
    }


    @Override
    public List<?> getStudentsInClass(String className) {
        SchoolClass schoolClass = null;
        try {
            schoolClass = findClassByClassName(className);
        } catch (Exception ex) {
            schoolClass = null;
        }
        if(schoolClass == null){
            return new ArrayList<String>(Collections.singleton("Class: " + className + " doesn't exist."));
        }
        return appDAO.getStudentsInClass(className);
    }


    public String generateUsername(String firstName, String lastName, boolean isAStudent, int i) {
        String username = firstName.toLowerCase() + "." + lastName.toLowerCase();
        if (i != 0) {
            username = username + i;
        }
        if (isAStudent) {
            username = username + "@student.school.com";
        } else {
            username = username + "@school.com";
        }
        User user = findUserByUsername(username);
        if (user != null) {
            return generateUsername(firstName, lastName, isAStudent, i + 1);
        } else {
            return username;
        }
    }
}
