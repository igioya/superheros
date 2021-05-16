package com.superheroes.persistence;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserRepository extends AbstractRepository<User>{
    public UserRepository(){
        this.setClazz(User.class);
    }
    public User getByUsername(String username) throws NotFoundException {
        Query query = this.getCurrentSession().createQuery("select u from User u where u.username=:username");
        query.setParameter("username", username);
        try{
            User obj = (User) query.getSingleResult();
            return obj;
        } catch(NoResultException e){
            throw new NotFoundException();
        }
    }
}
