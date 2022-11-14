package com.seanans.restservicedb.apartments;

import java.util.UUID;

public class Apartments_persons {
    private UUID apartmentId;
    private UUID personId;

    public Apartments_persons(UUID apartmentId, UUID personId) {
        this.apartmentId = apartmentId;
        this.personId = personId;
    }

    public Apartments_persons() {
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
