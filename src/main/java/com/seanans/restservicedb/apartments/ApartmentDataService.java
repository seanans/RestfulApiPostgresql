package com.seanans.restservicedb.apartments;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApartmentDataService {
    private final JdbcTemplate jdbcTemplate;

    public ApartmentDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //returns all apartments
    public List<Apartment> getAll() {

        var sql = """
                SELECT apartments.id as apartmentId, apartments.area as apartmentArea,
                apartments.address as apartmentAddress
                FROM public."apartments"
                ORDER BY apartments.id ASC;
                """;
        List<Apartment> localApartmentList = jdbcTemplate.query(sql, (resultSet, i) -> new Apartment(
                resultSet.getObject("apartmentId", java.util.UUID.class),
                resultSet.getLong("apartmentArea"),
                resultSet.getString("apartmentAddress")));
        if (localApartmentList.isEmpty() || localApartmentList.size() == 1) {
            return localApartmentList;
        }

        List<Apartment> apartmentList = new ArrayList<>(localApartmentList);
        var sql1 = """
                SELECT * FROM public.persons_apartments
                ORDER BY person_id ASC
                """;
        List<Apartments_persons> localApartmentsPersons = jdbcTemplate.query(sql1, (resultSet, i) ->
                new Apartments_persons(
                        resultSet.getObject("apartment_id", java.util.UUID.class),
                        resultSet.getObject("person_id", java.util.UUID.class)));
        if (localApartmentsPersons.isEmpty()) {
            return localApartmentList;
        }
        List<UUID> tempPersonsId = new ArrayList<>(localApartmentList.size());

        for (int i = 0; i < localApartmentList.size(); i++) {
            if (!tempPersonsId.isEmpty()) {
                tempPersonsId.clear();
            }
            for (int j = 0; j < localApartmentsPersons.size(); j++) {
                if (localApartmentList.get(i).getId().equals(localApartmentsPersons.get(j).getApartmentId())) {
                    tempPersonsId.add(localApartmentsPersons.get(j).getPersonId());
                }
            }
            if (!tempPersonsId.isEmpty()) {
                List<UUID> localSave = new ArrayList<>(tempPersonsId);
                apartmentList.get(i).setPersonsIds(localSave);
            }
        }
        return apartmentList;

    }


    public List<ApartmentCount> getApartmentsCount() {
        var sql = """
                SELECT apartments.address as apartmentAddress, COUNT(*) as countOfPersons
                FROM public.apartments
                join persons_apartments on apartment_id = apartments.id
                join persons on persons.id = person_id
                GROUP BY apartmentAddress;
                """;
        return jdbcTemplate.query(sql, (resultSet, i) -> new ApartmentCount(
                resultSet.getString("apartmentAddress"),
                resultSet.getLong("countOfPersons")
        ));
    }

    //select Apartment with personsIds which belong to this Apartment
    public ApartmentsPersons selectApartmentById(UUID id) {
        ApartmentsPersons targetApartment = new ApartmentsPersons();
        targetApartment.setId(id);
        var sql = """
                SELECT apartments.id as apartmentID, apartments.area as apartmentArea, apartments.address as apartmentAddress,
                persons.id as personID, persons.name as personName, persons.surname as personSurname
                FROM public.apartments
                left join persons_apartments on apartments.id = apartment_id
                left join persons on person_id = persons.id
                where apartments.id = ?;
                """;
        List<UUID> apartmentsPersons = jdbcTemplate.query(sql, (resultSet, i) -> {

            targetApartment.setArea(resultSet.getLong("apartmentArea"));
            targetApartment.setAddress(resultSet.getString("apartmentAddress"));

            return resultSet.getObject("personId", java.util.UUID.class);
        }, id);
        targetApartment.setPersonList(apartmentsPersons);
        return targetApartment;
    }

    // return data of every apartment from List
    public List<Apartment> selectApartmentsData(List<UUID> apartmentsIds) {
        var sql =
                String.format("SELECT * FROM public.apartments where id in (%s)",
                        apartmentsIds.stream()
                                .map(v -> "?")
                                .collect(Collectors.joining(", ")));

        List<Apartment> localApartmentList = jdbcTemplate.query(
                sql, (resultSet, i) -> new Apartment(
                        resultSet.getObject("id", java.util.UUID.class),
                        resultSet.getLong("area"),
                        resultSet.getString("address")
                ), apartmentsIds.toArray());
        if (localApartmentList.size() == 0) {
            return localApartmentList;
        }

        List<Apartment> apartmentList = new ArrayList<>(localApartmentList);

        var sql1 = String.format("SELECT apartment_id as apartmentId, person_id as personId FROM public.persons_apartments WHERE (apartment_id) IN (%s)",
                apartmentsIds.stream()
                        .map(v -> "?")
                        .collect(Collectors.joining(", ")));

        List<Apartments_persons> localApartmentsPersons = jdbcTemplate.query(
                sql1, (resultSet, i) -> new Apartments_persons(
                        resultSet.getObject("apartmentId", UUID.class),
                        resultSet.getObject("personId", UUID.class)),
                apartmentsIds.toArray());
        if (localApartmentsPersons.size() == 0) {
            return localApartmentList;
        }
        List<UUID> tempApartmentsId = new ArrayList<>(localApartmentsPersons.size());

        for (int i = 0; i < localApartmentList.size(); i++) {

            if (!tempApartmentsId.isEmpty()) {
                tempApartmentsId.clear();
            }

            for (int j = 0; j < localApartmentsPersons.size(); j++) {
                if (localApartmentList.get(i).getId().equals(localApartmentsPersons.get(j).getApartmentId())) {
                    tempApartmentsId.add(localApartmentsPersons.get(j).getPersonId());
                }
            }
            if (!tempApartmentsId.isEmpty()) {
                List<UUID> localSave = new ArrayList<>(tempApartmentsId);
                apartmentList.get(i).setPersonsIds(localSave);
            }
        }

        return apartmentList;
    }

    //insert new Apartment
    public HttpStatus insertApartment(Apartment apartment) {
        if (apartment.getArea() < 0 || apartment.getAddress() == null) {
            return HttpStatus.BAD_REQUEST;
        }
        var sql = """
                INSERT INTO public."apartments"(id, area, address)
                VALUES(uuid_generate_v4(), ?, ?)
                RETURNING id;
                """;
        var sql1 = """
                INSERT INTO public."apartments"(id, area, address)
                VALUES(uuid_generate_v4(), ?, ?)
                                
                """;
        if (apartment.getPersonsIds() != null) {
            List<ApartmentBindList> apartmentBindList = jdbcTemplate.query(sql, (resultSet, i) ->
                    new ApartmentBindList(resultSet.getObject("id", java.util.UUID.class),
                            apartment.getPersonsIds()), apartment.getArea(), apartment.getAddress());
            ApartmentBindList bindList = new ApartmentBindList(apartmentBindList.get(0).getApartmentId(), apartmentBindList.get(0).getPersonsIds());
            insertListOfBinds(bindList);
        } else {
            jdbcTemplate.update(sql1, apartment.getArea(), apartment.getAddress());
        }
        return HttpStatus.CREATED;
    }

    public HttpStatus insertListOfBinds(ApartmentBindList apartmentBindList) {
        List<UUID> localListIds = new ArrayList<>();
        for (int i = 0; i < apartmentBindList.getPersonsIds().size(); i++) {
            localListIds.add(apartmentBindList.getPersonsIds().get(i));
            localListIds.add(apartmentBindList.getApartmentId());
        }
        var sql = String.format("INSERT INTO public.persons_apartments(person_id, apartment_id)" + "VALUES %s",
                apartmentBindList.getPersonsIds().stream()
                        .map(v -> "(?, ?)")
                        .collect(Collectors.joining(", ")));
        jdbcTemplate.update(sql, localListIds.toArray());
        return HttpStatus.CREATED;
    }

    //update Apartment
    public HttpStatus updateApartment(Apartment apartment, UUID id) {
        var sql = """
                UPDATE public."apartments"
                SET area = ?, address = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, apartment.getArea(), apartment.getAddress(), id);
        return HttpStatus.OK;
    }

    //delete Apartment
    public HttpStatus deleteApartmentById(UUID id) {
        var sql = """
                DELETE FROM public."apartments"
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
        return HttpStatus.OK;
    }

}
