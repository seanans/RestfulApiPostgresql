package com.seanans.restservicedb.persons;

import java.util.List;

public class Person {

    private long id;
    private String name;
    private String surname;

    public Person(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
    private List<Long> apartmentsIds;

    public Person(long id, String name, String surname, List<Long> apartmentsIds) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.apartmentsIds = apartmentsIds;
    }

    public Person() {
    }

    public List<Long> getApartmentsIds() {
        return apartmentsIds;
    }

    public void setApartmentsIds(List<Long> apartmentsIds) {
        this.apartmentsIds = apartmentsIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
