package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Role;
import com.zuzannastolc.gradebook.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;

    public UserDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }
    @Override
    public Role findRoleByName(String theRoleName) {
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where roleName=:roleName", Role.class);
        theQuery.setParameter("roleName", theRoleName);

        Role theRole = null;
        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }
        return theRole;
    }

    @Override
    public User findByUserName(String theUserName) {
        TypedQuery<User> theQuery = entityManager.createQuery("from User where username=:uName", User.class);
        theQuery.setParameter("uName", theUserName);

        User theUser = null;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }
        return theUser;
    }

    @Override
    @Transactional
    public void save(User theUser) {
        entityManager.merge(theUser);
    }
}
