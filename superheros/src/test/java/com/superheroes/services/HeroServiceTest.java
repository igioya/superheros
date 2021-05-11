package com.superheroes.services;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.persistence.HeroRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HeroServiceTest {
    @Autowired
    @InjectMocks
    HeroService heroService;

    @Mock
    private HeroRepository heroRepository;

    @Test
    public void getAllHeros() {
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
            add(new Hero("Spiderman"));
        }};
        when(this.heroRepository.findAll()).thenReturn(superherosToReturn);

        /*#### When ####*/
        List<Hero> superheroes = heroService.getAll();

        /*#### Then ####*/
        assertTrue(3 == superheroes.size());
        assertTrue(superheroes.stream().anyMatch(hero -> hero.getName() == "Batman"));
        assertTrue(superheroes.stream().anyMatch(hero -> hero.getName() == "Superman"));
        assertTrue(superheroes.stream().anyMatch(hero -> hero.getName() == "Spiderman"));

    }

    @Test
    public void getHeroById() throws NotFoundException {
        /*#### Given ####*/
        Hero heroToReturn = new Hero("Spiderman");
        when(this.heroRepository.findById(anyLong())).thenReturn(heroToReturn);

        /*#### When ####*/
        Hero hero = heroService.getById(1L);

        /*#### Then ####*/
        assertTrue(heroToReturn == hero);
    }

    @Test
    public void findHeroByString() {
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
        }};
        when(this.heroRepository.findHerosByString(anyString())).thenReturn(superherosToReturn);

        /*#### When ####*/
        List<Hero> heros = heroService.getHerosByString("man");

        /*#### Then ####*/
        assertTrue(2 == heros.size());
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Batman"));
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Superman"));
    }

    @Test
    public void editHeroById() {
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");

        /*#### When ####*/
        hero.setName("Hulk");
        heroService.editHero(hero);

        /*#### Then ####*/
        verify(heroRepository).update(hero);
    }

    @Test
    public void deleteHero() {
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");

        /*#### When ####*/
        heroService.delete(hero);

        /*#### Then ####*/
        verify(heroRepository).delete(hero);
    }
}
