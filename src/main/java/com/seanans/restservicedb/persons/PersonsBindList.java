package com.seanans.restservicedb.persons;

import java.util.List;

public class PersonsBindList {
    private long personId;
    private List<Long> apartmentsId;

    public PersonsBindList(long personId, List<Long> apartmentsId) {
        this.personId = personId;
        this.apartmentsId = apartmentsId;
    }

    public PersonsBindList() {
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public List<Long> getApartmentsId() {
        return apartmentsId;
    }

    public void setApartmentsId(List<Long> apartmentsId) {
        this.apartmentsId = apartmentsId;
    }
}
