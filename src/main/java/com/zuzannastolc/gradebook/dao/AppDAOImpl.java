package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class AppDAOImpl implements AppDAO {
    private EntityManager entityManager;

    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public String getLoggedUsername(Authentication authentication) {
        String username;
        try {
            username = authentication.getName();
        } catch (Exception ex) {
            username = "Nobody has logged in!";
        }
        return username;
    }

    @Override
    public String getLoggedAuthorities(Authentication authentication) {
        String authorities;
        try {
            authorities = authentication.getAuthorities().toString();
        } catch (Exception ex) {
            authorities = "Nobody has logged in!";
        }
        return authorities;
    }

    @Override
    public void addNewUserWithAuthorities(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUserByUsername(String username) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM gradebook.users where username = :username", User.class);
        theQuery.setParameter("username", username);
        User user = null;
        try {
            user = (User)theQuery.getSingleResult();
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    @Override
    public void addNewStudent(Student student) {
        entityManager.persist(student);
    }

    @Override
    public void addNewTeacher(Teacher teacher) {
        entityManager.persist(teacher);
    }

    @Override
    public void updateUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateStudent(Student student) {
        entityManager.persist(student);
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        entityManager.persist(teacher);
    }

    @Override
    public List<String> findAllClasses() {
        Query theQuery = entityManager.createNativeQuery("SELECT class_name FROM gradebook.classes");
        List<?> tempClasses = theQuery.getResultList();
        List<String> classes = new ArrayList<>();
        for (Object o : tempClasses) {
            String s = o.toString();
            classes.add(s);
        }
        return classes;
    }

    @Override
    public SchoolClass findClassByClassName(String className) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM gradebook.classes where classes.class_name = :className", SchoolClass.class);
        theQuery.setParameter("className", className);
        SchoolClass schoolClass = null;
        try {
            schoolClass = (SchoolClass) theQuery.getSingleResult();
        } catch (Exception ex) {
            schoolClass = null;
        }
        return schoolClass;
    }

    @Override
    public void addNewClass(SchoolClass schoolClass) {
        entityManager.persist(schoolClass);
    }

    @Override
    public List<?> getEnabledStudentsInClass(String className) {
        SchoolClass schoolClass = findClassByClassName(className);
        int class_id = schoolClass.getClassId();
        Query theQuery = entityManager.createNativeQuery("SELECT usr.user_id, std.student_id, std.first_name, std.last_name FROM gradebook.users usr, gradebook.students std where usr.enabled = 1 and std.class_id = :classId and std.user_id = usr.user_id");
        theQuery.setParameter("classId", class_id);
        return (List<?>) theQuery.getResultList();
    }

    @Override
    public Subject findSubjectBySubjectName(String subjectName) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM gradebook.subjects where subject_name = :subjectName", Subject.class);
        theQuery.setParameter("subjectName", subjectName);
        Subject subject = null;
        try {
            subject = (Subject) theQuery.getSingleResult();
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }

    @Override
    public void addNewSubject(Subject subject) {
        entityManager.persist(subject);
    }

    @Override
    public void updateSubject(Subject subject) {
        entityManager.merge(subject);
    }

    @Override
    public List<?> getStudentsGradesFromSubject(Student student, Subject subject) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM gradebook.grades where subject_id = :subjectId AND student_id = :studentId", Grade.class);
        theQuery.setParameter("subjectId", subject.getSubjectId());
        theQuery.setParameter("studentId", student.getStudentId());
        return theQuery.getResultList();
    }

    @Override
    public void addGrade(Grade grade) {
        entityManager.persist(grade);
    }

    @Override
    public void updateGrade(Grade grade) {
        entityManager.merge(grade);
    }

    @Override
    public Grade findGradeById(int id) {
        return entityManager.find(Grade.class, id);
    }

    @Override
    public void deleteGrade(int id) {
        Grade grade = findGradeById(id);
        entityManager.remove(grade);
    }

}
