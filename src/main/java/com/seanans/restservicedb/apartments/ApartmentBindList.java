package com.seanans.restservicedb.apartments;

import java.util.List;

public class ApartmentBindList {
    private long apartmentId;
    private List<Long> personsId;

    public ApartmentBindList(long apartmentId, List<Long> personsId) {
        this.apartmentId = apartmentId;
        this.personsId = personsId;
    }

    public ApartmentBindList() {
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public List<Long> getPersonsId() {
        return personsId;
    }

    public void setPersonsId(List<Long> personsId) {
        this.personsId = personsId;
    }
}
