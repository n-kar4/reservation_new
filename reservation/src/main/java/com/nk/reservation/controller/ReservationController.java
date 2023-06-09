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
import com.nk.reservation.services.IReservationServices;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    
    @Autowired
    private IReservationServices reservationServices;
    
    @PostMapping("/")
    public void addNewReservation(@RequestBody Reservation reservation) {
        reservationServices.addNewReservation(reservation);
    }

    @GetMapping("/")
    public List<Reservation> getAllReservations() {
        return reservationServices.getAllReservations();
    }

    // @GetMapping("/{id}")
    // public Reservation getReservationById(@PathVariable int id) {
    //     return reservationServices.getReservationById(id).orElse(null);
    // }

    @GetMapping("/travelRequest/{travelRequestId}")
    public Reservation getReservationByTravelRequestId(@PathVariable int travelRequestId) {
        return reservationServices.getReservationByTravelRequestId(travelRequestId).orElse(null);
    }

    @GetMapping("/download")
    public byte[] downloadReservationsDoc() {
        return reservationServices.downloadReservationsDoc();
    }
}
