package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.dao.AppDAO;
import com.zuzannastolc.gradebook.entity.Authority;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppServiceImpl implements AppService {
    private AppDAO appDAO;

    @Autowired
    public AppServiceImpl(AppDAO appDAO){
        this.appDAO = appDAO;
    }

    @Override
    @Transactional
    public void addNewUserWithAuthorities(WebUser webUser) {
        User user = new User(webUser.getUsername(), "{noop}" + webUser.getPassword(), true);
        for(String authority: webUser.getAuthorities()){
            Authority tempAuthority = new Authority(authority);
            user.addAuthority(tempAuthority);
        }
        appDAO.addNewUserWithAuthorities(user);
    }

    @Override
    public User findByUsername(String username) {
        return appDAO.findByUsername(username);
    }

}
