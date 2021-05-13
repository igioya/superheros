package com.superheroes;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.persistence.HeroRepository;
import com.superheroes.webservices.HeroController;
import com.superheroes.webservices.JsonHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class IntegrationTest {
    @Autowired
    HeroRepository heroRepository;

    @Autowired
    HeroController heroController;

    MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = standaloneSetup(heroController).build();
        this.heroRepository.save(new Hero("Batman"));
        this.heroRepository.save(new Hero("Superman"));
        this.heroRepository.save(new Hero("Spiderman"));
        this.heroRepository.save(new Hero("Thor"));
    }

    @Test
    public void getAllHeros() throws Exception {
        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }

    @Test
    public void getHeroById() throws Exception {
        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/get/" + 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }

    @Test
    public void getHeroByIdNotFound() throws Exception {
        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/get/" + null)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()); /*#### Then ####*/
    }

    @Test
    public void editHero() throws Exception, NotFoundException {
        /*#### Given ####*/
        Hero hero = this.heroRepository.findById(1L);
        hero.setName("NewName");

        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/edit/" + 1L)
                        .content(JsonHelper.asJsonString(hero))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void editHeroNotFound() throws Exception, NotFoundException {
        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/edit/" + 1L)
                        .content(JsonHelper.asJsonString(new Hero("Superman")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteHero() throws Exception, NotFoundException {
        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/delete/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteHeroNotFound() throws Exception, NotFoundException {
        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/delete/" + null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void findHeroByString() throws Exception {
        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/getByString/man")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }
}
