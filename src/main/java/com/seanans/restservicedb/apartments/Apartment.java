package com.seanans.restservicedb.apartments;

import java.util.List;
import java.util.UUID;

public class Apartment {

    private UUID id;
    private long area;
    private String address;


    public Apartment(long area, String address) {
        this.area = area;
        this.address = address;
    }

    public Apartment(UUID id, long area, String address) {
        this.id = id;
        this.area = area;
        this.address = address;
    }
    private List<UUID> personsIds;

    public Apartment(UUID id, long area, String address, List<UUID> personsIds) {
        this.id = id;
        this.area = area;
        this.address = address;
        this.personsIds = personsIds;
    }

    public Apartment() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<UUID> getPersonsIds() {
        return personsIds;
    }

    public void setPersonsIds(List<UUID> personsIds) {
        this.personsIds = personsIds;
    }
}
