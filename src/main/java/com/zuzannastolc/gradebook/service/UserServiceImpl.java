package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.dao.UserDAO;
import com.zuzannastolc.gradebook.entity.Role;
import com.zuzannastolc.gradebook.entity.User;
import com.zuzannastolc.gradebook.entity.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
@Service

public class UserServiceImpl implements UserService{
    private UserDAO userDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findByUserName(String userName) {
        // check the database if the user already exists
        return userDao.findByUserName(userName);
    }

    @Override
    public Role findRoleByName(String roleName) {
        return userDao.findRoleByName(roleName);
    }

    @Override
    @Transactional
    public void save(WebUser webUser) {
        User user = new User();
        Collection<Role> roles = new ArrayList<>();
        user.setUsername(webUser.getUserName());
        user.setPassword(webUser.getPassword());
        for(String role: webUser.getRoleName()){
            Role tempRole = userDao.findRoleByName(role);
            roles.add(tempRole);
        }
        user.setRoles(roles);
        user.setEnabled(true);
        userDao.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }
}
