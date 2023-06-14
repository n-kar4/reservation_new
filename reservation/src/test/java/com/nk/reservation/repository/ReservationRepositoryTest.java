package com.nk.reservation.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;

public class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp(){
        //sample reservation object with parameters
        Reservation reservation = new Reservation();
        reservation.setAmount(1000);
        reservation.setConfirmationID("123456");
        reservation.setCreatedOn(LocalDate.now());
        reservation.setRemarks("remarks");
        reservation.setReservationDate(LocalDate.now());
        reservation.setReservationDoneByEmployeeId(1);
        reservation.setReservationDoneWithEntity("Airline");
        reservation.setReservationTypeId(
            new ReservationTypes(1,"Flight")
        );
        reservation.setTravelRequestId(1);
        
        reservationRepository.save(reservation);
    }

    @AfterEach
    void tearDown(){
        reservationRepository.deleteAll();
    }



}
