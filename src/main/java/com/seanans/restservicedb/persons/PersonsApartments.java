package com.seanans.restservicedb.persons;

import java.util.List;

public class PersonsApartments {

    private String name;
    private String surname;
    private List<Long> apartmentIds;

    public PersonsApartments(String name, String surname, List<Long> personsApartment) {
        this.name = name;
        this.surname = surname;
        this.apartmentIds = personsApartment;
    }

    public PersonsApartments() {
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

    public List<Long> getPersonsApartment() {
        return apartmentIds;
    }

    public void setPersonsApartment(List<Long> personsApartment) {
        this.apartmentIds = personsApartment;
    }
}
