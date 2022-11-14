package com.seanans.restservicedb.persons;

public class PersonCount {
    private String name;
    private long countOfApartments;

    public PersonCount(String name, long countOfApartments) {
        this.name = name;
        this.countOfApartments = countOfApartments;
    }

    public PersonCount() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountOfApartments() {
        return countOfApartments;
    }

    public void setCountOfApartments(long countOfApartments) {
        this.countOfApartments = countOfApartments;
    }
}
