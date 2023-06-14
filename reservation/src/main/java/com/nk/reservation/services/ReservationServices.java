package com.nk.reservation.services;

import java.util.List;
import java.util.Optional;
// import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.repository.ReservationRepository;

@Service
public class ReservationServices implements IReservationServices{

    @Autowired
    private ReservationRepository reservationRepository;

    ReservationServices(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void addNewReservation(Reservation reservation){
        if(checkValidReservation(reservation)){
            reservationRepository.save(reservation);
        }
    }

    @Override
    public Reservation getReservationByTravelRequestId(int travelRequestId) {
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

    private boolean checkValidReservation(Reservation reservation){
        if(reservation.getAmount()<0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        // //sample date, have to fetch from travel planner table
        // LocalDate formdate = LocalDate.now(); 

        // //checking if reservationDate is 1 day before formDate in the travel Request table
        // //for flight/bus reservations
        // if(reservation.getReservationDate().minusDays(1)!=formdate){
        //     throw new IllegalArgumentException("Reservation date cannot be in the past");
        // }

        // //ReservationDate for hotel must be same as the from date in travel plan
        // if(reservation.getReservationTypeId()==2  && reservation.getReservationDate()!=formdate){
        //     throw new IllegalArgumentException("Reservation date must be same as travel plan date");
        // }

        //There must be exactly 3 reservations per travel, one for flight/bus/train,
        // one for hotel and one for cab travel to hotel from flight/bus/train

        //The amount of all 3 reservations must not exceed the 70% of the allocated budget
        
        return true;
    }
    
}
