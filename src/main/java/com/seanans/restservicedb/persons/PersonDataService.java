package com.seanans.restservicedb.persons;

import com.seanans.restservicedb.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
                resultSet.getLong("personId"),
                resultSet.getString("personName"),
                resultSet.getString("personSurname")));
        for (int i = 0; i < localPersonList.size() - 1; i++) {
            for (int j = localPersonList.size() - 1; j > i; j--) {
                if (localPersonList.get(j).getId() == localPersonList.get(i).getId()) {
                    localPersonList.remove(j);
                }
            }
        }
        List<Person> personList = new ArrayList<>(localPersonList);
        // разделить var sql

        var sql1 = """
                SELECT * FROM public.persons_apartments
                ORDER BY person_id ASC
                """;
        List<Persons_apartments> localPersonsApartments = jdbcTemplate.query(sql1, (resultSet, i) ->
                new Persons_apartments(
                        resultSet.getLong("person_id"),
                        resultSet.getLong("apartment_id")));
        List<Long> tempApartmentsId = new ArrayList<>(localPersonsApartments.size());

        int l = 0;
        int m = 0;
        int k = 0;
        for (int i = 0; i < localPersonsApartments.size() - 1; i++) {
            if (!tempApartmentsId.isEmpty()) {
                tempApartmentsId.clear();
            }

            for (int j = localPersonsApartments.size() - 1; j > i; j--) {

                if (localPersonList.get(i).getId() == localPersonsApartments.get(j).getPersonId()) {
                    tempApartmentsId.add(localPersonsApartments.get(j).getApartmentId());
                    localPersonsApartments.remove(localPersonsApartments.get(j));
                }
                k = j - 1;
            }
            if (localPersonList.get(i).getId() == localPersonsApartments.get(k).getPersonId()) {
                tempApartmentsId.add(localPersonsApartments.get(i).getApartmentId());
                personList.get(i).setApartmentsIds(tempApartmentsId.stream().toList());
            }

            if (k != 0) {
                m = k - 1;
            }

            if (i != 0) {
                l = i - 1;
            }
            if (i != 0) {
                if (localPersonList.get(i).getId() == localPersonsApartments.get(m).getPersonId()) {
                    tempApartmentsId.add(localPersonsApartments.get(l).getApartmentId());
                    personList.get(i).setApartmentsIds(tempApartmentsId.stream().toList());
                }
            }

        }
        if (!tempApartmentsId.isEmpty()) {
            tempApartmentsId.clear();
        }
        if (localPersonList.get(l + 2).getId() == localPersonsApartments.get(localPersonsApartments.size() - 1).getPersonId()){
            tempApartmentsId.add(localPersonsApartments.get(localPersonsApartments.size() - 1).getApartmentId());
            personList.get(localPersonsApartments.size() - 1).setApartmentsIds(tempApartmentsId.stream().toList());
        }
        if (localPersonList.get(localPersonList.size() - 1).getId() == localPersonsApartments.get(localPersonsApartments.size() - 1).getPersonId()){
            tempApartmentsId.add(localPersonsApartments.get(localPersonsApartments.size() - 1).getApartmentId());
            personList.get(localPersonsApartments.size() - 1).setApartmentsIds(tempApartmentsId.stream().toList());
        }


        return personList;
    }

    //return person with belonged apartments by id
    public PersonsApartments selectPersonApartmentsById(long id) throws NotFoundException {
        PersonsApartments personsApartment = new PersonsApartments();
        var sql = """
                SELECT persons.name as personName, persons.surname as personSurname, apartments.id as apartmentsID
                FROM public.persons
                left join persons_apartments on persons.id = person_id
                left join apartments on apartment_id = apartments.id
                where persons.id = ?;
                """;
        List<Long> apartmentIds = jdbcTemplate.query(sql, (resultSet, i) -> {

            personsApartment.setName(resultSet.getString("personName"));
            personsApartment.setSurname(resultSet.getString("personSurname"));

            return resultSet.getLong("apartmentsID");

        }, id);
        personsApartment.setPersonsApartment(apartmentIds);
        return personsApartment;
    }

    //returns data of every person from List
    public List<Person> selectPersonsData(List<Long> personsIds) {

        var sql = String.format("SELECT * FROM public.persons where id in (%s)",
                personsIds.stream()
                        .map(v -> "?")
                        .collect(Collectors.joining(", ")));

        return jdbcTemplate.query(
                sql, (resultSet, i) -> new Person(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                ), personsIds.toArray());

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
    public HttpStatus deletePersonById(long id) {
        var sql = """
                DELETE FROM public."persons"
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
        return HttpStatus.OK;
    }

    //delete bind
    public HttpStatus deleteBind(Long personId, Long apartmentId) {
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
        var sql = """
                INSERT INTO public."persons"(id, name, surname)
                VALUES (?, ?, ?);
                """;
        jdbcTemplate.update(sql, person.getId(), person.getName(), person.getSurname());
        if (person.getApartmentsIds() != null) {
            PersonsBindList bindList = new PersonsBindList(person.getId(), person.getApartmentsIds());
            insertListOfBinds(bindList);
        }
        return HttpStatus.CREATED;
    }

    //add new bind
    public HttpStatus insertSingleBind(Long personId, Long apartmentId) {
        var sql = """
                INSERT INTO public.persons_apartments(person_id, apartment_id)
                VALUES (?, ?);
                """;
        jdbcTemplate.update(sql, personId, apartmentId);
        return HttpStatus.CREATED;
    }

    //add new ListBind
    public HttpStatus insertListOfBinds(PersonsBindList bindList) {
        List<Long> localListIds = new ArrayList<>();
        for (int i = 0; i < bindList.getApartmentsId().size(); i++) {
            localListIds.add(bindList.getPersonId());
            localListIds.add(bindList.getApartmentsId().get(i));
        }
        var sql = String.format("INSERT INTO public.persons_apartments(person_id, apartment_id)" + "VALUES %s",
                bindList.getApartmentsId().stream()
                        .map(v -> "(?, ?)")
                        .collect(Collectors.joining(", ")));

        jdbcTemplate.update(sql, localListIds.toArray());
        return HttpStatus.CREATED;
    }

    //update person
    public Person updatePerson(Person person, long id) {
        var sql = """
                UPDATE public."persons"
                SET name = ?, surname = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, person.getName(), person.getSurname(), id);
        return person;
    }

}
