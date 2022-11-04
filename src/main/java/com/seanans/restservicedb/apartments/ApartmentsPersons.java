package com.seanans.restservicedb.apartments;

import java.util.List;

public class ApartmentsPersons {
    private long id;
    private long area;
    private String address;
    private List<Long> personList;

    public ApartmentsPersons(long id, long area, String address, List<Long> personList) {
        this.id = id;
        this.area = area;
        this.address = address;
        this.personList = personList;
    }

    public ApartmentsPersons() {
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

    public List<Long> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Long> personList) {
        this.personList = personList;
    }
}

