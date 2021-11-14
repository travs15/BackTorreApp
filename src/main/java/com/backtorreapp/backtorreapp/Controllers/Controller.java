package com.backtorreapp.backtorreapp.Controllers;
import com.backtorreapp.backtorreapp.Model.Person;
import com.backtorreapp.backtorreapp.Model.Skill;
import com.backtorreapp.backtorreapp.Util.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/person/{id}")
    public ResponseEntity<?> getPerson(@PathVariable("id") String personId){
        Map<String, Object> response = new HashMap<String,Object>();
        Person p = new Person();
        try {
            String obj = restTemplate.getForObject(Constants.TORRE_PERSON +personId, String.class);
            JsonObject jsonObject = new JsonParser().parse(obj).getAsJsonObject();

            p.setName(jsonObject.get("person").getAsJsonObject().get("name").getAsString());
            p.setPicture(jsonObject.get("person").getAsJsonObject().get("picture").getAsString());
            p.setProfessionalHeadline(jsonObject.get("person").getAsJsonObject().get("professionalHeadline").getAsString());
            p.setLocationName(jsonObject.get("person").getAsJsonObject().get("location").getAsJsonObject().get("name").getAsString());
            p.setWeight(Float.parseFloat(jsonObject.get("person").getAsJsonObject().get("weight").getAsString()));
            p.setStats(jsonObject.get("stats").getAsJsonObject().toString());
            p.setPersonalityAnalysis(jsonObject.get("personalityTraitsResults").getAsJsonObject().get("analyses").getAsJsonArray().toString());
            p.setInterests(jsonObject.get("interests").getAsJsonArray().toString());
            p.setStrengths(jsonObject.get("strengths").getAsJsonArray().toString());
            p.setLinks(jsonObject.get("person").getAsJsonObject().get("links").getAsJsonArray().toString());

            response.put(Constants.RESPONSE, p);
            response.put(Constants.STATUS, HttpStatus.OK);

            return new ResponseEntity<Map<String, Object>> (response, (HttpStatus) response.get("status"));
        }
        catch(Exception  e) {
            response.put(Constants.RESPONSE, Constants.NOT_FOUND);
            response.put(Constants.STATUS, HttpStatus.OK);
            return new ResponseEntity<Map<String, Object>> (response, (HttpStatus) response.get("status"));
        }
    }

    @GetMapping("/person/{publicId}/{skillId}")
    public ResponseEntity<?> getSkillDetail(@PathVariable("publicId") String publicId, @PathVariable("skillId") String skillId){
        Map<String, Object> response = new HashMap<String,Object>();
        Skill s = new Skill();
        try {
            String obj = restTemplate.getForObject(Constants.TORRE_SKILLS+publicId+"/strengths-skills/"+skillId+"/detail", String.class);
            JsonObject jsonObject = new JsonParser().parse(obj).getAsJsonObject();

            s.setSkillName(jsonObject.get("name").getAsString());
            s.setRelatedExperience(jsonObject.get("relatedExperiences").getAsJsonArray().toString());

            response.put(Constants.RESPONSE, s);
            response.put(Constants.STATUS, HttpStatus.OK);

            return new ResponseEntity<Map<String, Object>> (response, (HttpStatus) response.get("status"));
        }
        catch(Exception  e) {
            response.put(Constants.RESPONSE, Constants.NOT_FOUND);
            response.put(Constants.STATUS, HttpStatus.OK);
            return new ResponseEntity<Map<String, Object>> (response, (HttpStatus) response.get("status"));
        }
    }
}
