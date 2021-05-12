package com.superheroes.persistence;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HeroRepositoryTest {
    @Autowired
    private HeroRepository heroRepository;

    @Before
    public void setUp(){

    }

    @Test
    public void findAll() {
        /*#### Given ####*/
        this.heroRepository.save(new Hero("Batman"));
        this.heroRepository.save(new Hero("Superman"));

        /*#### When ####*/
        List<Hero> all = heroRepository.findAll();

        /*#### Then ####*/
        assertEquals(2, all.size());
    }

    @Test
    public void findAllEmpty() {
        /*#### When ####*/
        List<Hero> all = heroRepository.findAll();

        /*#### Then ####*/
        assertEquals(0, all.size());
    }

    @Test
    public void findById() throws NotFoundException {
        /*#### Given ####*/
        Long heroId = (Long) this.heroRepository.save(new Hero("Batman"));

        /*#### When ####*/
        Hero heroRetrieved = heroRepository.findById(heroId);

        /*#### Then ####*/
        assertEquals(heroId, heroRetrieved.getId());
        assertEquals("Batman", heroRetrieved.getName());
    }

    @Test(expected = NotFoundException.class)
    public void findByNotFound() throws NotFoundException {
        /*#### When ####*/
        heroRepository.findById(1L);
    }

    @Test
    public void update() throws NotFoundException {
        /*#### Given ####*/
        Hero batman = new Hero("Batman");
        Long heroId = (Long) this.heroRepository.save(batman);

        /*#### When ####*/
        batman.setName("Batman2");
        this.heroRepository.update(batman);

        /*#### Then ####*/
        Hero heroRetrieved = heroRepository.findById(heroId);
        assertEquals("Batman2", heroRetrieved.getName());
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws NotFoundException {
        /*#### Given ####*/
        Hero batman = new Hero("Batman");
        Long heroId = (Long) this.heroRepository.save(batman);

        /*#### When ####*/
        this.heroRepository.delete(batman);

        /*#### Then ####*/
        heroRepository.findById(heroId);
    }
}
