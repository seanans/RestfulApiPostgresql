package com.seanans.restservicedb.apartments;

import java.util.List;

public class Apartment {

    private long id;
    private long area;
    private String address;


    public Apartment(long id, long area, String address) {
        this.id = id;
        this.area = area;
        this.address = address;
    }
    private List<Long> personsIds;

    public Apartment(long id, long area, String address, List<Long> personsIds) {
        this.id = id;
        this.area = area;
        this.address = address;
        this.personsIds = personsIds;
    }

    public Apartment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Long> getPersonsIds() {
        return personsIds;
    }

    public void setPersonsIds(List<Long> personsIds) {
        this.personsIds = personsIds;
    }
}
