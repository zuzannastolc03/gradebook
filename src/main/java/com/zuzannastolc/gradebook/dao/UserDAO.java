package com.zuzannastolc.gradebook.dao;

import com.zuzannastolc.gradebook.entity.Role;
import com.zuzannastolc.gradebook.entity.User;

public interface UserDAO {
    Role findRoleByName(String theRoleName);
    User findByUserName(String userName);

    void save(User user);
}
