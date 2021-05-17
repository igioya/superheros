package com.superheroes.services;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.persistence.HeroRedisRepository;
import com.superheroes.persistence.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class HeroService {
    @Autowired
    private HeroRepository heroRepository;;
    @Autowired
    private HeroRedisRepository heroRedisRepository;

    public List<Hero> getAll() {
        return this.heroRepository.findAll();
    }

    public Hero getById(long id) throws NotFoundException {
        return this.heroRedisRepository.getById(id)
                        .orElse(heroRepository.findById(id));
    }

    public void editHero(Hero hero) throws NotFoundException {
        this.heroRepository.update(hero);
        this.heroRedisRepository.save(hero);
    }

    public void delete(Long heroId) throws NotFoundException {
        this.heroRepository.deleteById(heroId);
        this.heroRedisRepository.delete(heroId);
        this.heroRedisRepository.removeQueries(heroId);
    }

    public List<Hero> getHerosByString(String string) {
        Supplier<List<Hero>> listSupplier = () -> {
            List<Hero> herosByString = heroRepository.findByString(string);
            this.heroRedisRepository.saveByString(string, herosByString);
            return herosByString;
        };
        return this.heroRedisRepository.getByString(string)
                        .orElseGet(listSupplier);
    }

    public void save(Hero hero) {
        this.heroRepository.save(hero);
        this.heroRedisRepository.save(hero);
    }
}
