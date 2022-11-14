package com.seanans.restservicedb.apartments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApartmentsData {
    private List<UUID> listIds = new ArrayList<>();

    public ApartmentsData(List<UUID> listIds) {
        this.listIds = listIds;
    }

    public ApartmentsData() {
    }

    public List<UUID> getListIds() {
        return listIds;
    }

    public void setListIds(List<UUID> listIds) {
        this.listIds = listIds;
    }
}
