package com.springsecurity.demo.controller;

import com.google.gson.Gson;
import com.springsecurity.demo.model.Person;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/persons")
public class PersonController {

    public static List<Person> PERSON_LIST = new ArrayList<>();

    public PersonController() {

        PERSON_LIST.add(new Person(1));
        PERSON_LIST.add(new Person(2));
        PERSON_LIST.add(new Person(3));
        PERSON_LIST.add(new Person(4));

    }

    /**
     * returns the Person with given id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Person getById(@PathVariable("id") Integer id) {

        return PERSON_LIST.stream().filter(person -> person.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Geçersiz Id");
                });

    }

    /**
     * ROLE: admin
     * returns error page since /api/persons is need to authenticated with rol 'admin'
     * see {@link com.springsecurity.demo.config.SecurityConfiguration} line 22
     * @return
     */
    @GetMapping
    public String getAll() {
        return new Gson().toJson(PERSON_LIST);
    }

    /**
     * ROLE: add_role
     * @param person
     * @return
     */
    @PostMapping("/add")
    public String  addPerson(@RequestBody Person person){
        PERSON_LIST.add(person);

        return new Gson().toJson(PERSON_LIST);
    }

    /**
     * ROLE: update_role
     * @param id
     * @param person
     * @return
     */
    @PutMapping("/update")
    public String updatePerson(@PathVariable("id") Integer id, @RequestBody Person person){

        return new Gson().toJson(PERSON_LIST);
    }

}