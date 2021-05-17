package com.superheroes.services;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.persistence.HeroRedisRepository;
import com.superheroes.persistence.HeroRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HeroServiceTest {
    @Autowired
    @InjectMocks
    HeroService heroService;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private HeroRedisRepository heroRedisRepository;

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
    public void getHeroByIdFromCache() throws NotFoundException {
        /*#### Given ####*/
        Hero heroToReturn = new Hero("Spiderman");
        when(this.heroRedisRepository.getById(anyLong())).thenReturn(java.util.Optional.of(heroToReturn));

        /*#### When ####*/
        Hero hero = heroService.getById(1L);

        /*#### Then ####*/
        assertTrue(heroToReturn == hero);
    }

    @Test
    public void getHeroByIdFromBD() throws NotFoundException {
        /*#### Given ####*/
        Hero heroToReturn = new Hero("Spiderman");
        when(this.heroRedisRepository.getById(anyLong())).thenReturn(java.util.Optional.ofNullable(null));
        when(this.heroRepository.findById(anyLong())).thenReturn(heroToReturn);

        /*#### When ####*/
        Hero hero = heroService.getById(1L);

        /*#### Then ####*/
        assertTrue(heroToReturn == hero);
    }

    @Test
    public void findHeroByStringFromCache() {
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
        }};
        when(this.heroRedisRepository.getByString(anyString())).thenReturn(Optional.ofNullable(superherosToReturn));

        /*#### When ####*/
        List<Hero> heros = heroService.getHerosByString("man");

        /*#### Then ####*/
        assertEquals(2 , heros.size());
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Batman"));
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Superman"));
    }

    @Test
    public void findHeroByStringFromDB() {
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
        }};
        when(this.heroRedisRepository.getByString(anyString())).thenReturn(Optional.ofNullable(null));
        when(this.heroRepository.findByString(anyString())).thenReturn(superherosToReturn);

        /*#### When ####*/
        List<Hero> heros = heroService.getHerosByString("man");

        /*#### Then ####*/
        assertEquals(2 , heros.size());
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Batman"));
        assertTrue(heros.stream().anyMatch(hero -> hero.getName() == "Superman"));
    }

    @Test
    public void editHeroById() throws NotFoundException {
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");

        /*#### When ####*/
        hero.setName("Hulk");
        heroService.editHero(hero);

        /*#### Then ####*/
        verify(heroRepository).update(hero);
        verify(heroRedisRepository).save(hero);
    }

    @Test
    public void saveHero() throws NotFoundException {
        /*#### Given ####*/
        Hero hero = new Hero("Hulk");

        /*#### When ####*/
        heroService.save(hero);

        /*#### Then ####*/
        verify(heroRepository).save(hero);
        verify(heroRedisRepository).save(hero);
    }

    @Test
    public void deleteHero() throws NotFoundException {
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(this.heroRepository).deleteById(1L);

        /*#### When ####*/
        heroService.delete(1L);

        /*#### Then ####*/
        verify(heroRepository).deleteById(1L);
        verify(heroRedisRepository).removeQueries(1L);
    }
}
