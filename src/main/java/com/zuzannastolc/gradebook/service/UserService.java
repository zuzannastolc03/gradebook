package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.Role;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Role findRoleByName(String theRoleName);
    User findByUserName(String userName);

    void save(WebUser webUser);
}
