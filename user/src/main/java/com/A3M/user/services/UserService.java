package com.A3M.user.services;

import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;


}
