package com.seanans.restservicedb.persons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonsData {
    private List<UUID> listIds = new ArrayList<>();

    public PersonsData(List<UUID> listIds) {
        this.listIds = listIds;
    }

    public PersonsData() {
    }

    public List<UUID> getListIds() {
        return listIds;
    }

    public void setListIds(List<UUID> listIds) {
        this.listIds = listIds;
    }
}
