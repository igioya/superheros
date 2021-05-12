package com.superheroes.webservices;

import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.services.HeroService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class HeroControllerTest {

    @InjectMocks
    HeroController heroController;

    @Mock
    HeroService heroService;

    MockMvc mockMvc;

    @Before
    public void init() throws InterruptedException {
        mockMvc = standaloneSetup(heroController).build();
    }

    @Test
    public void getAllHeros() throws Exception {
        /*#### Given ####*/
        List<Hero> superherosToReturn = new ArrayList<>() {{
            add(new Hero("Superman"));
            add(new Hero("Batman"));
            add(new Hero("Spiderman"));
        }};
        when(this.heroService.getAll()).thenReturn(superherosToReturn);

        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }

    @Test
    public void getHeroById() throws Exception, NotFoundException {
        /*#### Given ####*/
        when(this.heroService.getById(anyLong())).thenReturn(new Hero("Spiderman"));

        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/get/" + anyLong())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }

    @Test
    public void getHeroByIdNotFound() throws Exception, NotFoundException {
        /*#### Given ####*/
        when(this.heroService.getById(anyLong())).thenThrow(NotFoundException.class);

        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/get/" + anyLong())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()); /*#### Then ####*/
    }

    @Test
    public void editHero() throws Exception, NotFoundException {
        /*#### Given ####*/
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(this.heroService).editHero(any());

        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/edit/" + any())
                        .content(JsonHelper.asJsonString(new Hero("Superman")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void editHeroNotFound() throws Exception, NotFoundException {
        /*#### Given ####*/
        doThrow(NotFoundException.class).when(this.heroService).editHero(any());

        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/edit/" + any())
                        .content(JsonHelper.asJsonString(new Hero("Superman")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteHero() throws Exception, NotFoundException {
        /*#### Given ####*/
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(this.heroService).delete(any());

        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/delete/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteHeroNotFound() throws Exception, NotFoundException {
        /*#### Given ####*/
        doThrow(NotFoundException.class).when(this.heroService).delete(any());

        /*#### When ####*/
        mockMvc.perform(
                MockMvcRequestBuilders.post("/superheros/delete/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void findHeroByString() throws Exception {
        /*#### Given ####*/
        when(this.heroService.getHerosByString(anyString())).thenReturn(anyList());

        /*#### When ####*/
        mockMvc.perform( MockMvcRequestBuilders
                .get("/superheros/getByString/sarasa")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); /*#### Then ####*/
    }
}
