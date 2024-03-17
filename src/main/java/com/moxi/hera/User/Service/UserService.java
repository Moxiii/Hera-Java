package com.moxi.hera.User.Service;

import com.moxi.hera.User.Model.User;
import com.moxi.hera.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        User  user = userRepository.findUserByUsername(username);
        return user;
    }
}
