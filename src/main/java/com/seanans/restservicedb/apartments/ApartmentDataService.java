package com.seanans.restservicedb.apartments;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
                SELECT apartments.id as apartmentId, apartments.area as apartmentArea, apartments.address as apartmentAddress,
                persons.id as personId
                FROM public."apartments"
                left join persons_apartments on apartments.id = apartment_id
                left join persons on person_id = persons.id
                ORDER BY apartments.id ASC;
                """;
        List<Apartment> localApartmentList = jdbcTemplate.query(sql, (resultSet, i) -> new Apartment(
                resultSet.getLong("apartmentId"),
                resultSet.getLong("apartmentArea"),
                resultSet.getString("apartmentAddress")));
        for (int i = 0; i < localApartmentList.size() - 1; i++) {
            for (int j = localApartmentList.size() - 1; j > i; j--) {
                if (localApartmentList.get(j).getId() == localApartmentList.get(i).getId()) {
                    localApartmentList.remove(j);
                }
            }
        }
        List<Apartment> apartmentList = new ArrayList<>(localApartmentList);

        List<Apartments_persons> localApartmentsPersons = jdbcTemplate.query(sql, (resultSet, i) ->
                new Apartments_persons(
                        resultSet.getLong("apartmentId"),
                        resultSet.getLong("personId")));
        List<Long> tempPersonsId = new ArrayList<>(localApartmentList.size());
        for (int i = 0; i < localApartmentList.size() - 1; i++) {

            if (!tempPersonsId.isEmpty()) {
                tempPersonsId.clear();
            }
            for (int j = localApartmentList.size() - 1; j > i; j--) {
                if (localApartmentList.get(i).getId() == localApartmentsPersons.get(j).getApartmentId()) {
                    tempPersonsId.add(localApartmentsPersons.get(j).getPersonId());
                    localApartmentsPersons.remove(localApartmentsPersons.get(j));
                }
            }

            tempPersonsId.add(localApartmentsPersons.get(i).getPersonId());
            apartmentList.get(i).setPersonsIds(tempPersonsId.stream().toList());
        }

        if (!tempPersonsId.isEmpty()){
            tempPersonsId.clear();
        }
        tempPersonsId.add(localApartmentsPersons.get(localApartmentsPersons.size() - 1).getPersonId());
        apartmentList.get(localApartmentList.size() - 1).setPersonsIds(tempPersonsId.stream().toList());
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
    public ApartmentsPersons selectApartmentsPersonsById(long id) {
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
        List<Long> apartmentsPersons = jdbcTemplate.query(sql, (resultSet, i) -> {

            targetApartment.setArea(resultSet.getLong("apartmentArea"));
            targetApartment.setAddress(resultSet.getString("apartmentAddress"));

            return resultSet.getLong("personId");
        }, id);
        targetApartment.setPersonList(apartmentsPersons);
        return targetApartment;
    }

    // return data of every apartment from List
    public List<Apartment> selectApartmentsData(List<Long> apartmentsIds) {
        var sql =
                String.format("SELECT * FROM public.apartments where id in (%s)",
                        apartmentsIds.stream()
                                .map(v -> "?")
                                .collect(Collectors.joining(", ")));

        return jdbcTemplate.query(
                sql, (resultSet, i) -> new Apartment(
                        resultSet.getLong("id"),
                        resultSet.getLong("area"),
                        resultSet.getString("address")
                ), apartmentsIds.toArray());
    }

    //insert new Apartment
    public HttpStatus insertApartment(Apartment apartment) {
        var sql = """
                INSERT INTO public."apartments"(id, area, address)
                VALUES(?, ?, ?);
                """;
        jdbcTemplate.update(sql, apartment.getId(), apartment.getArea(), apartment.getAddress());
        List<Long> personsIds = apartment.getPersonsIds();
        ApartmentBindList apartmentBindList = new ApartmentBindList(apartment.getId(), apartment.getPersonsIds());
        return HttpStatus.OK;
    }

    public HttpStatus insertListOfBinds(ApartmentBindList apartmentBindList) {
        List<Long> localListIds = new ArrayList<>();
        for (int i = 0; i < apartmentBindList.getPersonsId().size(); i++) {
            localListIds.add(apartmentBindList.getPersonsId().get(i));
            localListIds.add(apartmentBindList.getApartmentId());
        }
        var sql = String.format("INSERT INTO public.persons_apartments(person_id, apartment_id)" + "VALUES %s",
                apartmentBindList.getPersonsId().stream()
                        .map(v -> "(?, ?)")
                        .collect(Collectors.joining(", ")));
        jdbcTemplate.update(sql, localListIds.toArray());
        return HttpStatus.OK;
    }
    //update Apartment
    public HttpStatus updateApartment(Apartment apartment, long id) {
        var sql = """
                UPDATE public."apartments"
                SET area = ?, address = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, apartment.getArea(), apartment.getAddress(), id);
        return HttpStatus.OK;
    }

    //delete Apartment
    public HttpStatus deleteApartmentById(long id) {
        var sql = """
                DELETE FROM public."apartments"
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
        return HttpStatus.OK;
    }

}
