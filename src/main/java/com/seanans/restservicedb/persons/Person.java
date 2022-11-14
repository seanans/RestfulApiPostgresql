package com.seanans.restservicedb.persons;

import java.util.List;
import java.util.UUID;

public class Person {

    private UUID id;
    private String name;
    private String surname;

    public Person(UUID id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
    private List<UUID> apartmentsIds;

    public Person(UUID id, String name, String surname, List<UUID> apartmentsIds) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.apartmentsIds = apartmentsIds;
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Person() {
    }

    public List<UUID> getApartmentsIds() {
        return apartmentsIds;
    }

    public void setApartmentsIds(List<UUID> apartmentsIds) {
        this.apartmentsIds = apartmentsIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
