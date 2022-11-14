package com.seanans.restservicedb.persons;

import java.util.List;
import java.util.UUID;

public class PersonsApartments {

    private String name;
    private String surname;
    private List<UUID> apartmentIds;

    public PersonsApartments(String name, String surname, List<UUID> personsApartment) {
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

    public List<UUID> getPersonsApartment() {
        return apartmentIds;
    }

    public void setPersonsApartment(List<UUID> personsApartment) {
        this.apartmentIds = personsApartment;
    }
}
