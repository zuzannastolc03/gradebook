package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.User;

public interface AppDAO {
    void addNewUserWithAuthorities(User user);
    User findByUsername(String username);

}
