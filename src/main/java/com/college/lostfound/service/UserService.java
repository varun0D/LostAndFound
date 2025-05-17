package com.college.lostfound.service;

import com.college.lostfound.model.User;
import com.college.lostfound.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        return repo.save(user);
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }
}
