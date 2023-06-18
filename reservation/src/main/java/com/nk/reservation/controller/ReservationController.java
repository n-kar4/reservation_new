package com.nk.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.exceptions.ResourceNotFoundException;
import com.nk.reservation.services.IReservationServices;
import com.nk.reservation.services.IReservationTypesServices;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    
    @Autowired
    private IReservationServices reservationServices;
    @Autowired
    private IReservationTypesServices reservationTypesServices;

    @GetMapping("/types")
    public List<ReservationTypes> getReservationTypes() {

        return reservationTypesServices.getReservationTypes();
    }
    
    @PostMapping("/add")
    public void addNewReservation(@RequestBody Reservation reservation) {
        reservationServices.addNewReservation(reservation);
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable int id) throws ResourceNotFoundException{
        return reservationServices.getReservationById(id).orElse(null);
    }

    @GetMapping("/track/{travelRequestId}")
    public List<Reservation> getReservationByTravelRequestId(@PathVariable int travelRequestId) throws ResourceNotFoundException{
        return reservationServices.getReservationsByTravelRequestId(travelRequestId);
    }

    @GetMapping("/{reservationid}/download")
    public byte[] downloadReservationsDoc() {
        return reservationServices.downloadReservationsDoc();
    }


    //NOT REQUIRED
    @GetMapping("/")
    public List<Reservation> getAllReservations() {
        return reservationServices.getAllReservations();
    }
}
