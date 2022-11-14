package com.seanans.restservicedb.apartments;

import java.util.List;
import java.util.UUID;

public class ApartmentBindList {
    private UUID apartmentId;
    private List<UUID> personsIds;

    public ApartmentBindList(UUID apartmentId, List<UUID> personsIds) {
        this.apartmentId = apartmentId;
        this.personsIds = personsIds;
    }

    public ApartmentBindList() {
    }

    public UUID getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(UUID apartmentId) {
        this.apartmentId = apartmentId;
    }

    public List<UUID> getPersonsIds() {
        return personsIds;
    }

    public void setPersonsIds(List<UUID> personsIds) {
        this.personsIds = personsIds;
    }
}
