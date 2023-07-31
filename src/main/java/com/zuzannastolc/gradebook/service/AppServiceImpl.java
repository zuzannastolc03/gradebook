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
    public void addNewStudent(Student student, SchoolClass schoolClass) {
        String username = generateUsername(student.getFirstName(), student.getLastName(), true, 0);
        String password = "{noop}" + student.getFirstName().toLowerCase();
        User user = new User(username, password, true);
        Authority authority = new Authority("ROLE_STUDENT");
        user.addAuthority(authority);
        student.setUser(user);
        student.setSchoolClass(schoolClass);
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
        if (schoolClass == null) {
            return new ArrayList<String>(Collections.singleton("Class: " + className + " doesn't exist."));
        }
        return appDAO.getEnabledStudentsInClass(className);
    }

    @Override
    public Subject findSubjectBySubjectName(String subjectName) {
        return appDAO.findSubjectBySubjectName(subjectName);
    }

    @Override
    @Transactional
    public void addNewSubject(Subject subject) {
        appDAO.addNewSubject(subject);
    }

    @Override
    public Teacher findTeacherByUsername(String username) {
        User user = findUserByUsername(username);
        Teacher teacher = null;
        try {
            teacher = user.getTeacher();
        } catch (Exception ex) {
            teacher = null;
        }
        return teacher;
    }

    @Override
    public Student findStudentByUsername(String username) {
        User user = findUserByUsername(username);
        Student student = null;
        try {
            student = user.getStudent();
        } catch (Exception ex) {
            student = null;
        }
        return student;
    }

    @Override
    @Transactional
    public void assignTeacherToSubject(Teacher teacher, Subject subject) {
        subject.addTeacher(teacher);
        appDAO.updateSubject(subject);
    }

    @Override
    public List<Subject> getListOfTeachersSubjects(Teacher teacher) {
        return teacher.getSubjects();
    }

    @Override
    public List<Teacher> getListOfSubjectsTeachers(Subject subject) {
        return subject.getTeachers();
    }

    @Override
    @Transactional
    public void assignClassToSubject(SchoolClass schoolClass, Subject subject) {
        subject.addClass(schoolClass);
        appDAO.updateSubject(subject);
    }

    @Override
    public List<Subject> getListOfClassesSubjects(SchoolClass schoolClass) {
        return schoolClass.getSubjects();
    }

    @Override
    public List<SchoolClass> getListOfSubjectsClasses(Subject subject) {
        return subject.getSchoolClasses();
    }

    @Override
    public List<?> getStudentsGradesFromSubject(Student student, Subject subject) {
        return appDAO.getStudentsGradesFromSubject(student, subject);
    }

    @Override
    @Transactional
    public void addGrade(Grade grade, Teacher teacher, Subject subject, Student student) {
        grade.setStudent(student);
        grade.setTeacher(teacher);
        grade.setSubject(subject);
        appDAO.addGrade(grade);
    }

    @Override
    @Transactional
    public void updateGrade(Grade grade) {
        appDAO.updateGrade(grade);
    }

    @Override
    public Grade findGradeById(int id) {
        return appDAO.findGradeById(id);
    }

    @Override
    @Transactional
    public void deleteGrade(int id) {
        appDAO.deleteGrade(id);
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
