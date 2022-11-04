package com.seanans.restservicedb.apartments;

import com.seanans.restservicedb.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentDataService apartmentDataService;

    @GetMapping("")
    public List<Apartment> getAll() {
        log.info("getAllApartments");
        return apartmentDataService.getAll();
    }

    @GetMapping("/{id}")
    public ApartmentsPersons selectApartmentsPersonsById(@PathVariable("id") Long id) {
        log.info("get persons of Apartment:{}", id);
        return apartmentDataService.selectApartmentsPersonsById(id);
    }

    @GetMapping("/apartments")
    public List<Apartment> selectApartmentsData(@NotNull @RequestBody() List<Long> listIds) throws NotFoundException {
        log.info("select list id`s:{} ", listIds);
        return apartmentDataService.selectApartmentsData(listIds);
    }

    @GetMapping("/count")
    public List<ApartmentCount> getPersonsCount() throws NotFoundException {
        log.info("get count");
        return apartmentDataService.getApartmentsCount();
    }

    @PostMapping("")
    public HttpStatus insertApartment(@RequestBody Apartment apartment) {
        log.info("insert new apartment with: id:{}, area:{}, address:{}", apartment.getId(), apartment.getArea(), apartment.getAddress());
        return apartmentDataService.insertApartment(apartment);
    }

    @PostMapping("/listBind")
    public HttpStatus insertListOfBinds(@RequestBody ApartmentBindList bindList) {
        log.info("List bind: {} to apartmentId: {}", bindList.getPersonsId(), bindList.getApartmentId());
        return apartmentDataService.insertListOfBinds(bindList);
    }

    @PutMapping("/{id}")
    public HttpStatus updateApartment(@NotNull @PathVariable("id") Long id, @RequestBody() Apartment apartment) {
        log.info("update apartment:{} to: area:{}, address:{}", id, apartment.getArea(), apartment.getAddress());
        return apartmentDataService.updateApartment(apartment, id);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteApartmentById(@PathVariable("id") Long id) {
        log.info("delete apartment with id:{}", id);
        return apartmentDataService.deleteApartmentById(id);
    }
}

