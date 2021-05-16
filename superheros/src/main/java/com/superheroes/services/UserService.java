package com.superheroes.services;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.User;
import com.superheroes.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User getUser(String username) throws NotFoundException {
        return this.repository.getByUsername(username);
    }

    public void save(User user) {
        this.repository.save(user);
    }

    public List<User> getAll() {
        return this.repository.findAll();
    }
}
