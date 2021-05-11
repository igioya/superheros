package com.superheroes.persistence;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractRepository<T extends Serializable>{

    public T findById(final long id) {
        return null;
    }

    public List<T> findAll(){
        return null;
    }

    public T update(final T entity){
        return null;
    }

    public void delete(final T entity) {

    }
}


