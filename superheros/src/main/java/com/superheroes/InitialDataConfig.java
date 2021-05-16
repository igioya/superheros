package com.superheroes;

import com.superheroes.model.User;
import com.superheroes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource({"classpath:application.properties"})
public class InitialDataConfig {

    @Autowired
    UserService userService;

    @Autowired
    private Environment env;

    @PostConstruct
    public void initialize() {
//            User user = new User();
//            user.setUsername("igioya");
//            user.setPassword("asd");
//            this.userService.save(user);
    }
}