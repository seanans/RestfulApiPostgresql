package com.seanans.restservicedb.persons;

import java.util.List;
import java.util.UUID;

public class PersonsBindList {
    private UUID personId;
    private List<UUID> apartmentsIds;

    public PersonsBindList(UUID personId, List<UUID> apartmentsIds) {
        this.personId = personId;
        this.apartmentsIds = apartmentsIds;
    }

    public PersonsBindList() {
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public List<UUID> getApartmentsIds() {
        return apartmentsIds;
    }

    public void setApartmentsIds(List<UUID> apartmentsIds) {
        this.apartmentsIds = apartmentsIds;
    }
}
