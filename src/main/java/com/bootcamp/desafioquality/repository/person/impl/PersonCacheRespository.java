package com.bootcamp.desafioquality.repository.person.impl;

import com.bootcamp.desafioquality.entity.person.Person;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.repository.CacheDBTable;
import com.bootcamp.desafioquality.repository.CacheRepository;
import com.bootcamp.desafioquality.repository.person.PersonRepository;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class PersonCacheRespository implements PersonRepository, CacheRepository<Integer, Person> {
    final CacheDBTable<Integer, Person> database;

    public PersonCacheRespository() throws Exception {
        database = new CacheDBTable<>() {
            @Override
            protected @NotNull Integer generateNextId() {
                throw new BadRequestException("No se puede guardar una persona sin dni!");
            }
        };
        JsonNode jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/persons.json");
        for (JsonNode jsonNode : jsonNodes) {
            database.persist(Person.fromJson(jsonNode));
        }
    }

    @Override
    public CacheDBTable<Integer, Person> getDatabase() {
        return this.database;
    }
}
