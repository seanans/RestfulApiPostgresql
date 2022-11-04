package com.seanans.restservicedb.apartments;

public class ApartmentCount {
    private String address;
    private long personId;

    public ApartmentCount(String address, long personId) {
        this.address = address;
        this.personId = personId;
    }

    public ApartmentCount() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
