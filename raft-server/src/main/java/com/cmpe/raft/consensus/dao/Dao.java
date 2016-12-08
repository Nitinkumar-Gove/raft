package com.cmpe.raft.consensus.dao;

import com.cmpe.raft.consensus.model.Person;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Sushant on 06-12-2016.
 */
public class Dao {

    private static Integer id = 0;
    private static Map<Integer, Person> person_map =  new HashMap<>();

    public static Person getPerson(int id) {
        return person_map.get(id);
    }

    public static List<Person> getAll() {
       return new ArrayList<>(person_map.values());
    }

    public static Person addPerson(Person person) {
        if (person.getId() > id && person.getId() > 0) {
            id = person.getId();
        } else {
            id++;
            person.setId(id);
        }
        person_map.put(id, person);
        return person;
    }

    public static Person updatePerson(Person person) {
        Person newPerson = person_map.get(person.getId());
        if (person.getName()!= null) {
            newPerson.setName(person.getName());
        }
        if (person.getAge()!= null) {
            newPerson.setAge(person.getAge());
        }
        return person_map.put(newPerson.getId(), newPerson);
    }

    public static Person deletePerson(Integer id) {
        return person_map.remove(id);
    }

    public static void updateDao(List<Person> persons) {
        person_map = new HashMap<>();
        for (Person person: persons) {
            person_map.put(person.getId(), person);
        }
    }
}
