package com.superheroes.persistence;

import com.superheroes.model.Hero;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class HeroRepository extends AbstractRepository<Hero> {
    public HeroRepository(){
        setClazz(Hero.class);
    }

    public List<Hero> findHerosByString(String string) {
        Query query = getCurrentSession().createQuery("select h from Hero h where h.name like concat('%',:string,'%')");
        query.setParameter("string", string);
        return query.getResultList();
    }
}
