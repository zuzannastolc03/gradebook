package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Student;
import com.zuzannastolc.gradebook.entity.Teacher;
import com.zuzannastolc.gradebook.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;


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
        TypedQuery<User> theQuery = entityManager.createQuery("from User where username=:theData", User.class);
        theQuery.setParameter("theData", username);
        User user = null;
        try {
            user = theQuery.getSingleResult();
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

}
