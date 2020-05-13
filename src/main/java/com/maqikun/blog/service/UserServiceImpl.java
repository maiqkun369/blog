package com.maqikun.blog.service;

import com.maqikun.blog.Dao.UserRepository;
import com.maqikun.blog.pojo.User;
import com.maqikun.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    public User checkUser(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username,MD5Utils.toMD5(password));
        return user;
    }
}
