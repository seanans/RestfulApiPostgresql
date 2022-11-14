package com.seanans.restservicedb.apartments;

public class ApartmentCount {
    private String address;
    private long countOfPersons;

    public ApartmentCount(String address, long countOfPersons) {
        this.address = address;
        this.countOfPersons = countOfPersons;
    }

    public ApartmentCount() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCountOfPersons() {
        return countOfPersons;
    }

    public void setCountOfPersons(long countOfPersons) {
        this.countOfPersons = countOfPersons;
    }
}
