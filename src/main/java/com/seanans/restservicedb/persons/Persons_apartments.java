package com.seanans.restservicedb.persons;

public class Persons_apartments {
    private long personId;
    private long apartmentId;

    public Persons_apartments(long personId, long apartmentId) {
        this.personId = personId;
        this.apartmentId = apartmentId;
    }

    public Persons_apartments() {
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(long apartmentId) {
        this.apartmentId = apartmentId;
    }
}
