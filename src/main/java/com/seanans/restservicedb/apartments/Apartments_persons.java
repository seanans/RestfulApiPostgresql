package com.seanans.restservicedb.apartments;

public class Apartments_persons {
    private long apartmentId;
    private long personId;

    public Apartments_persons(long apartmentId, long personId) {
        this.apartmentId = apartmentId;
        this.personId = personId;
    }

    public Apartments_persons() {
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
