package com.superheroes.services;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.persistence.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeroService {
    @Autowired
    private HeroRepository heroRepository;;

    public List<Hero> getAll() {
        return this.heroRepository.findAll();
    }

    public Hero getById(long id) throws NotFoundException {
        return this.heroRepository.findById(id);
    }

    public void editHero(Hero hero) {
        this.heroRepository.update(hero);
    }

    public void delete(Hero hero) {
        this.heroRepository.delete(hero);
    }

    public List<Hero> getHerosByString(String string) {
        List<Hero> allHeros = this.heroRepository.findAll();
        List herosByString = new ArrayList();
        allHeros.stream().filter(hero -> hero.getName().contains(string)).forEach(hero -> herosByString.add(hero));
        return herosByString;
    }
}
