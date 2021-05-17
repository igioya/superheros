package com.superheroes.persistence;

import com.superheroes.model.Hero;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HeroRedisRepositoryTest {

    @Autowired
    @InjectMocks
    HeroRedisRepository heroRedisRepository;

    @Mock
    HashMap<Long, Hero> cache;
    @Mock
    HashMap<String, List<Hero>> queryCache;

    @Test
    public void save(){
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");
        hero.setId(5L);

        /*#### When ####*/
        heroRedisRepository.save(hero);

        /*#### Then ####*/
        verify(cache).put(5L, hero);
    }

    @Test
    public void getById(){
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");
        hero.setId(5L);
        when(cache.get(5L)).thenReturn(hero);

        /*#### When ####*/
        Optional<Hero> heroById = heroRedisRepository.getById(5L);

        /*#### Then ####*/
        assertEquals(Optional.ofNullable(hero), heroById);
    }

    @Test
    public void getByIdNotFound(){
        /*#### Given ####*/
        Hero hero = new Hero("Spiderman");
        hero.setId(5L);
        when(cache.get(5L)).thenReturn(null);

        /*#### When ####*/
        Optional<Hero> heroById = heroRedisRepository.getById(5L);

        /*#### Then ####*/
        assertEquals(Optional.ofNullable(null), heroById);
    }

    @Test
    public void delete(){
        /*#### When ####*/
        heroRedisRepository.delete(5L);

        /*#### Then ####*/
        verify(cache).remove(5L);
    }

    @Test
    public void getByString(){
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
        }};
        when(queryCache.get("man")).thenReturn(superherosToReturn);

        /*#### When ####*/
        Optional<List<Hero>> heroById = heroRedisRepository.getByString("man");

        /*#### Then ####*/
        assertEquals(Optional.ofNullable(superherosToReturn), heroById);
    }

    @Test
    public void getByStringNotFound(){
        /*#### Given ####*/
        when(queryCache.get("man")).thenReturn(null);

        /*#### When ####*/
        Optional<List<Hero>> heroById = heroRedisRepository.getByString("man");

        /*#### Then ####*/
        assertEquals(Optional.ofNullable(null), heroById);
    }

    @Test
    public void removeQueriesWithoutOccurrences(){
        /*#### Given ####*/
        List<Hero> queryCached = new ArrayList<>() {{
            add(new Hero(2L, "Batman"));
        }};

        Map queryCacheCopy = new HashMap<>();
        queryCacheCopy.put("man", queryCached);

        when(queryCache.clone()).thenReturn(queryCacheCopy);

        /*#### When ####*/
        heroRedisRepository.removeQueries(1L);

        /*#### Then ####*/
        verify(queryCache, times(0)).remove(anyString());
    }

    @Test
    public void removeQueriesWithOneOccurrences(){
        /*#### Given ####*/
        List<Hero> queryCached = new ArrayList<>() {{
            add(new Hero(2L, "Batman"));
        }};
        List<Hero> queryCached2 = new ArrayList<>();
        queryCached2.add(new Hero(1L, "Superman"));

        Map queryCacheCopy = new HashMap<>();
        queryCacheCopy.put("man", queryCached);
        queryCacheCopy.put("superm", queryCached2);

        when(queryCache.clone()).thenReturn(queryCacheCopy);

        /*#### When ####*/
        heroRedisRepository.removeQueries(1L);

        /*#### Then ####*/
        verify(queryCache, times(1)).remove(anyString());
    }

    @Test
    public void removeQueriesWithTwoOccurrences(){
        /*#### Given ####*/
        List<Hero> queryCached = new ArrayList<>() {{
            add(new Hero(1L, "Superman"));
            add(new Hero(2L, "Batman"));
        }};
        List<Hero> queryCached2 = new ArrayList<>();
        queryCached2.add(new Hero(1L, "Superman"));

        Map queryCacheCopy = new HashMap<>();
        queryCacheCopy.put("man", queryCached);
        queryCacheCopy.put("superm", queryCached2);

        when(queryCache.clone()).thenReturn(queryCacheCopy);

        /*#### When ####*/
        heroRedisRepository.removeQueries(1L);

        /*#### Then ####*/
        verify(queryCache, times(2)).remove(anyString());
    }
}
