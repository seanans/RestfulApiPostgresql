package com.seanans.restservicedb.persons;

public class PersonCount {
    private String name;
    private long count;

    public PersonCount(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public PersonCount() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
