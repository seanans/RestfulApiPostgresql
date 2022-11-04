package com.seanans.restservicedb.persons;


import com.seanans.restservicedb.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonDataService personDataService;

    @GetMapping("")
    public List<Person> getAll() throws NotFoundException{
        log.info("getAllPersons");
        return personDataService.getAll();
    }

    @GetMapping("/{id}")
    public Object selectPersonApartmentsById(@PathVariable("id") Long id) throws NotFoundException {
        log.info("get person`s:{} apartments", id);
        return personDataService.selectPersonApartmentsById(id);
    }

    @GetMapping("/persons")
    public List<Person> selectPersonsData(@NotNull @RequestBody() List<Long> listIds) {
        log.info("select personsList:{} ", listIds);
        return personDataService.selectPersonsData(listIds);
    }

    @GetMapping("/count")
    public List<PersonCount> getPersonsCount() throws NotFoundException{
        log.info("get count");
        return personDataService.getPersonsCount();
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteById(@PathVariable("id") Long id) throws NotFoundException{
        log.info("delete Person id:{}", id);
        return personDataService.deletePersonById(id);
    }
    @DeleteMapping("/bind/{personId}&{apartmentId}")
    public HttpStatus deleteBind(@PathVariable("personId") Long personId, @PathVariable("apartmentId") Long apartmentId) {
        log.info("delete bind personId:{} apartmentId:{}", personId, apartmentId);
        return personDataService.deleteBind(personId, apartmentId);
    }
    @PostMapping("")
    public HttpStatus insertPerson(@NotNull @RequestBody() Person person) throws NotFoundException{
        log.info("create Person with: id:{}, name:{}, surname:{}", person.getId(), person.getName(), person.getSurname());
        return personDataService.insertPerson(person);
    }

    @PostMapping("/bind/{personId}&{apartmentId}")
    public HttpStatus insertBind(@PathVariable("personId") Long personId, @PathVariable("apartmentId") Long apartmentId) {
        log.info("inserted bind personId:{} apartmentId:{}", personId, apartmentId);
        return personDataService.insertSingleBind(personId, apartmentId);
    }

    @PostMapping("/listBind")
    public HttpStatus insertListOfBinds(@RequestBody PersonsBindList bindList) {
        log.info("List bind: {} to person: {}", bindList.getApartmentsId(),  bindList.getPersonId());
        return personDataService.insertListOfBinds(bindList);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@NotNull @PathVariable("id") Long id, @RequestBody() Person person) throws NotFoundException{
        log.info("update Person:{} to name:{}, surname:{}", id, person.getName(), person.getSurname());
        return personDataService.updatePerson(person, id);
    }
}
