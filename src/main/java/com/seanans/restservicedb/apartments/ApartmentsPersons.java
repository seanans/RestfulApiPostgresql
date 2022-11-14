package com.seanans.restservicedb.apartments;

import java.util.List;
import java.util.UUID;

public class ApartmentsPersons {
    private UUID id;
    private long area;
    private String address;
    private List<UUID> personList;

    public ApartmentsPersons(UUID id, long area, String address, List<UUID> personList) {
        this.id = id;
        this.area = area;
        this.address = address;
        this.personList = personList;
    }

    public ApartmentsPersons() {
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

    public List<UUID> getPersonList() {
        return personList;
    }

    public void setPersonList(List<UUID> personList) {
        this.personList = personList;
    }
}

