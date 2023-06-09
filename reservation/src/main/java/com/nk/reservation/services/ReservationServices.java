package com.nk.reservation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.repository.ReservationRepository;

@Service
public class ReservationServices implements IReservationServices{

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getAllReservations() {

        return reservationRepository.findAll();
    }

    @Override
    public void addNewReservation(Reservation reservation){

        reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservationByTravelRequestId(int travelRequestId) {

        return reservationRepository.findById(travelRequestId);
    }
    
    @Override
    public Optional<Reservation> getReservationById(int id) {

        return reservationRepository.findById(id);
    }

    @Override
    public byte[] downloadReservationsDoc() {

        return null;
    }
    
}
