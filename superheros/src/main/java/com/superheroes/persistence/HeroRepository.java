package com.superheroes.persistence;

import com.superheroes.model.Hero;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HeroRepository extends AbstractRepository<Hero> {
    public HeroRepository(){
        setClazz(Hero.class);
    }

    public List<Hero> findByString(String string) {
        Query query = this.getCurrentSession().createQuery("select h from Hero h where h.name like concat('%',:string,'%')");
        query.setParameter("string", string);
        return query.getResultList();
    }
}
