package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;

public interface AppService {
    void addNewUserWithAuthorities(WebUser webUser);
    User findByUsername(String username);
}
