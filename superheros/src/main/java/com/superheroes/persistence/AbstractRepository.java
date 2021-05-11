package com.superheroes.persistence;

import com.superheroes.exceptions.persistence.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractRepository<T extends Serializable>{
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public void setClazz(final Class<T> clazzToSet){
        clazz = clazzToSet;
    }

    public T findById(final long id) throws NotFoundException {
        Query query = sessionFactory.getCurrentSession().createQuery("from "+this.clazz.getSimpleName()+" where id=:id");
        query.setParameter("id", id);
        try {
            T obj = (T) query.getSingleResult();
            return obj;
        } catch(NoResultException e){
            throw new NotFoundException();
        }
    }

    public List<T> findAll(){
        return (List<T>) sessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public T update(final T entity){
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public Serializable save(final T entity) throws HibernateException {
        Serializable saveResponse;
        try {
            saveResponse = sessionFactory.getCurrentSession().save(entity);
        } catch(HibernateException e){
            throw e;
        }
        return saveResponse;
    }

    protected final Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }
}


