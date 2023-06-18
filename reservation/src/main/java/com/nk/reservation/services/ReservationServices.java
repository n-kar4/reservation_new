package com.nk.reservation.services;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;

@Service
public class ReservationServices implements IReservationServices{

    @Autowired
    private ReservationRepository reservationRepository;

    ReservationServices(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    
    @Override
    public void addNewReservation(Reservation reservation){
        if(reservation.getAmount()<0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        reservationRepository.save(reservation);
    }
    
    @Override
    public List<Reservation> getReservationsByTravelRequestId(int travelRequestId) {
        return reservationRepository.findByTravelRequestId(travelRequestId);
    }
    
    @Override
    public Optional<Reservation> getReservationById(int id) {
        return reservationRepository.findById(id);
    }
    
    @Override
    public byte[] downloadReservationsDoc() {
        return null;
    }
    
    //Not Required
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
