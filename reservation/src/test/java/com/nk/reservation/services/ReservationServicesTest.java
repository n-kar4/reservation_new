package com.nk.reservation.services;

import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;

public class ReservationServicesTest {

    @Mock
    private ReservationRepository reservationRepository;
    private ReservationServices reservationServices;
    AutoCloseable autoCloseable;
    Reservation reservation;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);

        reservationServices = new ReservationServices(reservationRepository);
        
        reservation = new Reservation(1,1000,
                1,LocalDate.now(),
                "Airline",LocalDate.now(),
                1000,"534","remarks",
                new ReservationTypes(1,"Flight"), null);

        
        reservationRepository.save(reservation);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void getReservationByIdTest(){
        mock(Reservation.class);
        mock(ReservationRepository.class);
        Reservation reservation_expected = reservationRepository.findById(reservation.getId()).orElse(null);
        assertEquals(reservation_expected, reservationServices.getReservationById(1).orElse(null));
    }

    @Test
    void getReservationByTravelRequestIdTest(){
        mock(Reservation.class);
        mock(ReservationRepository.class);
        Reservation reservation_expected = reservation;
        assertEquals(reservation_expected, reservationServices.getReservationByTravelRequestId(reservation.getTravelRequestId()));
    }
}
