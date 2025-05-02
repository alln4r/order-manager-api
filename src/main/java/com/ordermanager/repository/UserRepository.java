package com.ordermanager.repository;

import com.ordermanager.model.User;
import com.ordermanager.utils.BaseRepository;
import com.ordermanager.utils.JPAUtil;



public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super(User.class);
    }
}