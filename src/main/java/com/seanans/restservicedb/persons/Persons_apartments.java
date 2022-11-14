package com.seanans.restservicedb.persons;

import java.util.UUID;

public class Persons_apartments {
    private UUID personId;
    private UUID apartmentId;

    public Persons_apartments(UUID personId, UUID apartmentId) {
        this.personId = personId;
        this.apartmentId = apartmentId;
    }

    public Persons_apartments() {
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public UUID getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(UUID apartmentId) {
        this.apartmentId = apartmentId;
    }
}
