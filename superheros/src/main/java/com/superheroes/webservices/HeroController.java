package com.superheroes.webservices;

import com.superheroes.config.annotations.logexcecutiontime.LogExecutionTime;
import com.superheroes.exceptions.persistence.NotFoundException;
import com.superheroes.model.Hero;
import com.superheroes.services.HeroService;
import com.superheroes.webservices.reponse.ErrorResponse;
import com.superheroes.webservices.reponse.Response;
import com.superheroes.webservices.reponse.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/superheros")
public class HeroController {

    @Autowired
    HeroService heroService;

    @LogExecutionTime
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<String> getAll() {
        List<Hero> heros = this.heroService.getAll();
        SuccessResponse succesResponse = new SuccessResponse();
        succesResponse.setData(heros);
        return new ResponseEntity<>(JsonHelper.fromObjectToJSON(succesResponse), HttpStatus.OK);
    }

    @LogExecutionTime
    @RequestMapping(value = "/get/{heroId}", method = RequestMethod.GET)
    public ResponseEntity<String> getHero(@PathVariable("heroId") Long heroId)  {
        Hero hero = null;
        Response response = new SuccessResponse();
        try {
            hero = this.heroService.getById(heroId);
            response.setData(hero);
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(response), HttpStatus.OK);
        } catch (NotFoundException e) {
            response = new ErrorResponse();
            response.setData("Hero with id: " + heroId + " not found");
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(response), HttpStatus.BAD_REQUEST);
        }
    }

    @LogExecutionTime
    @RequestMapping(value = "/edit/{heroId}", method = RequestMethod.POST)
    public ResponseEntity<String> editHero(@RequestBody Hero hero)  {
        Response response = new SuccessResponse();
        try {
            this.heroService.editHero(hero);
            response.setData(hero);
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(response), HttpStatus.OK);
        } catch (NotFoundException e) {
            response = new ErrorResponse();
            response.setData("Hero with id: " + hero.getId() + " not found");
            return new ResponseEntity<>(JsonHelper.fromObjectToJSON(response), HttpStatus.BAD_REQUEST);
        }
    }

    @LogExecutionTime
    @RequestMapping(value = "/delete/{heroId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteHero(@PathVariable("heroId") Long heroId)  {
        Response response = new SuccessResponse();
        try {
            this.heroService.delete(heroId);
            return new ResponseEntity<>("Delted hero with id: " + heroId, HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Hero with id: " + heroId + " not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @LogExecutionTime
    @RequestMapping(value = "/getByString/{string}", method = RequestMethod.GET)
    public ResponseEntity<String> getByString(@PathVariable("string") String string) {
        List<Hero> herosByString = this.heroService.getHerosByString(string);
        SuccessResponse succesResponse = new SuccessResponse();
        succesResponse.setData(herosByString);
        return new ResponseEntity<>(JsonHelper.fromObjectToJSON(succesResponse), HttpStatus.OK);
    }
}
