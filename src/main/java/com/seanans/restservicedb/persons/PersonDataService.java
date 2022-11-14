package com.seanans.restservicedb.persons;

import com.seanans.restservicedb.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PersonDataService {

    private final JdbcTemplate jdbcTemplate;

    public PersonDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //returns all persons
    //returns every apartmentsId connected to person
    //uuid!

    public List<Person> getAll() {
        var sql = """
                SELECT persons.id as personId, persons."name" as personName,
                persons.surname as personSurname
                FROM public."persons"
                ORDER BY persons.id ASC;
                """;
        List<Person> localPersonList = jdbcTemplate.query(sql, (resultSet, i) -> new Person(
                resultSet.getObject("personId", java.util.UUID.class),
                resultSet.getString("personName"),
                resultSet.getString("personSurname")));
        if (localPersonList.isEmpty() || localPersonList.size() == 1) {
            return localPersonList;
        }
        List<Person> personList = new ArrayList<>(localPersonList);

        var sql1 = """
                SELECT * FROM public.persons_apartments
                ORDER BY person_id ASC
                """;
        List<Persons_apartments> localPersonsApartments = jdbcTemplate.query(sql1, (resultSet, i) ->
                new Persons_apartments(
                        resultSet.getObject("person_id", java.util.UUID.class),
                        resultSet.getObject("apartment_id", java.util.UUID.class)));
        if (localPersonsApartments.size() == 0) {
            return personList;
        }
        List<UUID> tempApartmentsId = new ArrayList<>(localPersonsApartments.size());

        for (int i = 0; i < localPersonList.size(); i++) {
            if (!tempApartmentsId.isEmpty()) {
                tempApartmentsId.clear();
            }
            for (int j = 0; j < localPersonsApartments.size(); j++) {
                if (localPersonList.get(i).getId().equals(localPersonsApartments.get(j).getPersonId())) {
                    tempApartmentsId.add(localPersonsApartments.get(j).getApartmentId());
                }
            }
            if (!tempApartmentsId.isEmpty()) {
                List<UUID> localSave = new ArrayList<>(tempApartmentsId);
                personList.get(i).setApartmentsIds(localSave);
            }
        }

        return personList;
    }

    //return person with belonged apartments by id
    public PersonsApartments selectPersonApartmentsById(UUID id) throws NotFoundException {
        PersonsApartments personsApartment = new PersonsApartments();
        var sql = """
                SELECT persons.name as personName, persons.surname as personSurname, apartments.id as apartmentsID
                FROM public.persons
                left join persons_apartments on persons.id = person_id
                left join apartments on apartment_id = apartments.id
                where persons.id = ?;
                """;
        List<UUID> apartmentIds = jdbcTemplate.query(sql, (resultSet, i) -> {

            personsApartment.setName(resultSet.getString("personName"));
            personsApartment.setSurname(resultSet.getString("personSurname"));

            return resultSet.getObject("apartmentsID", java.util.UUID.class);

        }, id);
        personsApartment.setPersonsApartment(apartmentIds);
        return personsApartment;
    }

    //returns data of every person from List
    public List<Person> selectPersonsData(List<UUID> personsIds) {

        var sql = String.format("SELECT * FROM public.persons where id in (%s)",
                personsIds.stream()
                        .map(v -> "?")
                        .collect(Collectors.joining(", ")));

        List<Person> localPersonList = jdbcTemplate.query(
                sql, (resultSet, i) -> new Person(
                        resultSet.getObject("id", java.util.UUID.class),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                ), personsIds.toArray());
        if (localPersonList.isEmpty() || localPersonList.size() == 1) {
            return localPersonList;
        }

        List<Person> personList = new ArrayList<>(localPersonList);

        var sql1 = String.format("SELECT person_id as personId, apartment_id as apartmentId FROM public.persons_apartments WHERE (person_id) IN (%s)",
                personsIds.stream()
                        .map(v -> "?")
                        .collect(Collectors.joining(", ")));

        List<Persons_apartments> localPersonsApartments = jdbcTemplate.query(
                sql1, (resultSet, i) -> new Persons_apartments(
                        resultSet.getObject("personId", java.util.UUID.class),
                        resultSet.getObject("apartmentId", java.util.UUID.class)),
                personsIds.toArray());
        if (localPersonsApartments.size() == 0) {
            return localPersonList;
        }

        List<UUID> tempApartmentsId = new ArrayList<>(localPersonsApartments.size());

        for (int i = 0; i < localPersonList.size(); i++) {
            if (!tempApartmentsId.isEmpty()) {
                tempApartmentsId.clear();
            }
            for (int j = 0; j < localPersonsApartments.size(); j++) {
                if (localPersonList.get(i).getId().equals(localPersonsApartments.get(j).getPersonId())) {
                    tempApartmentsId.add(localPersonsApartments.get(j).getApartmentId());
                }
            }
            if (!tempApartmentsId.isEmpty()) {
                List<UUID> localSave = new ArrayList<>(tempApartmentsId);
                personList.get(i).setApartmentsIds(localSave);
            }
        }
        return personList;
    }

    //return person with count of apartments
    public List<PersonCount> getPersonsCount() {
        var sql = """
                SELECT persons.name as personName, COUNT(*) as countOfApartments
                FROM public.persons
                join persons_apartments on person_id = persons.id
                join apartments on apartment_id = apartments.id
                GROUP BY personName;
                """;
        return jdbcTemplate.query(sql, (resultSet, i) -> new PersonCount(
                resultSet.getString("personName"),
                resultSet.getLong("countOfApartments")
        ));
    }

    //delete person
    //cascade delete binds
    public HttpStatus deletePersonById(UUID id) {
        var sql = """
                DELETE FROM public."persons"
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
        return HttpStatus.OK;
    }

    //delete bind
    public HttpStatus deleteBind(UUID personId, UUID apartmentId) {
        var sql = """
                DELETE FROM public.persons_apartments
                WHERE person_id = ? and apartment_id = ?
                """;
        jdbcTemplate.update(sql, personId, apartmentId);
        return HttpStatus.OK;
    }

    //add new person
    //add List of apartmentsIds!
    public HttpStatus insertPerson(Person person) {
        if (person.getName() == null || person.getSurname() == null) {
            return HttpStatus.BAD_REQUEST;
        }
        var sql = """
                INSERT INTO public."persons"(id, name, surname)
                VALUES (uuid_generate_v4(), ?, ?)
                RETURNING id;
                """;
        var sql1 = """
                INSERT INTO public."persons"(id, name, surname)
                VALUES (uuid_generate_v4(), ?, ?)
                """;
        if (person.getApartmentsIds() != null) {
            List<PersonsBindList> personsBindList = jdbcTemplate.query(sql, (resultSet, i) ->
                    new PersonsBindList(resultSet.getObject("id", java.util.UUID.class),
                            person.getApartmentsIds()), person.getName(), person.getSurname());
            PersonsBindList bindList = new PersonsBindList(personsBindList.get(0).getPersonId(), personsBindList.get(0).getApartmentsIds());
            insertListOfBinds(bindList);
        } else {
            jdbcTemplate.update(sql1, person.getName(), person.getSurname());
        }
        return HttpStatus.CREATED;
    }

    //add new bind
    public HttpStatus insertSingleBind(UUID personId, UUID apartmentId) {
        var sql = """
                INSERT INTO public.persons_apartments(person_id, apartment_id)
                VALUES (?, ?);
                """;
        jdbcTemplate.update(sql, personId, apartmentId);
        return HttpStatus.CREATED;
    }

    //add new ListBind
    public HttpStatus insertListOfBinds(PersonsBindList bindList) {
        List<UUID> localListIds = new ArrayList<>();
        for (int i = 0; i < bindList.getApartmentsIds().size(); i++) {
            localListIds.add(bindList.getPersonId());
            localListIds.add(bindList.getApartmentsIds().get(i));
        }
        var sql = String.format("INSERT INTO public.persons_apartments(person_id, apartment_id)" + "VALUES %s",
                bindList.getApartmentsIds().stream()
                        .map(v -> "(?, ?)")
                        .collect(Collectors.joining(", ")));

        jdbcTemplate.update(sql, localListIds.toArray());
        return HttpStatus.CREATED;
    }

    //update person
    public HttpStatus updatePerson(Person person, UUID id) {
        var sql = """
                UPDATE public."persons"
                SET name = ?, surname = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, person.getName(), person.getSurname(), id);
        return HttpStatus.CREATED;
    }

}
