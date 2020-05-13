package com.maqikun.blog.service;

import com.maqikun.blog.pojo.User;

public interface UserService {
    User checkUser(String username,String password);
}
