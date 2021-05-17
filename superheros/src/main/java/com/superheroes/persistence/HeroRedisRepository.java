package com.superheroes.persistence;

import com.superheroes.model.Hero;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class HeroRedisRepository {

    HashMap<Long, Hero> cache;
    HashMap<String, List<Hero>> queryCache;

    public HeroRedisRepository(){
        this.cache = new HashMap<>();
        this.queryCache = new HashMap<>();
    }

    public void save(Hero hero) {
        this.cache.put(hero.getId(), hero);
    }

    public Optional<Hero> getById(Long id) {
        Hero hero = this.cache.get(id);
        return Optional.ofNullable(hero);
    }

    public void delete(Long heroId) {
        this.cache.remove(heroId);
    }

    public Optional<List<Hero>> getByString(String string) {
        return Optional.ofNullable(this.queryCache.get(string));
    }

    public void removeQueries(Long heroId) {
        Map<String, List<Hero>> queryCacheCopy = (Map<String, List<Hero>>) queryCache.clone();
        queryCacheCopy.forEach((stringQuery, heroes) -> {
            boolean anyMatch = heroes.stream().anyMatch(hero -> hero.getId() == heroId);
            if(anyMatch) this.queryCache.remove(stringQuery);
        });
    }

    public void saveByString(String string, List<Hero> herosByString) {
        this.queryCache.put(string, herosByString);
    }
}
