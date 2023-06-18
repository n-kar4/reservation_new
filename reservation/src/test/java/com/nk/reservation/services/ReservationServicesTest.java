package com.nk.reservation.services;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServicesTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServices reservationServices;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reservation = new Reservation(1,1000,
                1,LocalDate.now(),
                "Airline",LocalDate.now(),
                1000,"534","remarks",
                new ReservationTypes(1,"Flight"), null);        
    }

    @Test
    void testAddNewReservation_ValidReservation() {
        // Mock the repository's behavior
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Call the service method
        reservationServices.addNewReservation(reservation);

        // Verify that the repository save method was called
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testAddNewReservation_InvalidReservation_amount() {
        // Create an invalid reservation
        Reservation reservation = new Reservation();
        reservation.setAmount(-10);

        // Call the service method
        assertThrows(IllegalArgumentException.class, () -> reservationServices.addNewReservation(reservation));
        // Verify that the repository save method was not called
        verify(reservationRepository, never()).save(reservation);
    }

    @Test
    void testGetReservationByTravelRequestId() {
        // Mock the repository's behavior
        int travelRequestId = 30;
        Reservation reservation = new Reservation();
        reservation.setTravelRequestId(travelRequestId);
        List<Reservation> reservationList = Collections.singletonList(reservation);
        when(reservationRepository.findByTravelRequestId(travelRequestId)).thenReturn(reservationList);

        // Call the service method for success
        List<Reservation> result1 = reservationServices.getReservationsByTravelRequestId(travelRequestId);
        assertEquals(reservationList, result1);

        // Call the service method for failure
        Random random = new Random(); //Math.random() returns double, but int needed
        List<Reservation> result2 = reservationServices.getReservationsByTravelRequestId(travelRequestId+(random.nextInt(100)+1));
        assertNotEquals(reservationList, result2);
    }

    @Test
    void testGetReservationById() {
        // Mock the repository's behavior
        int id = 1;
        Optional<Reservation> optionalReservation = Optional.of(new Reservation());
        when(reservationRepository.findById(id)).thenReturn(optionalReservation);

        // Call the service method for success
        Optional<Reservation> result_valid = reservationServices.getReservationById(id);
        assertEquals(optionalReservation, result_valid);

        // Call the service method for failure
        Random random = new Random(); //Math.random() returns double, but int needed
        Optional<Reservation> result_inv = reservationServices.getReservationById(id+(random.nextInt()+1));
        assertNotEquals(optionalReservation, result_inv);
    }

    @Test
    void testDownloadReservationsDoc() {
        // Call the service method
        byte[] result = reservationServices.downloadReservationsDoc();

        // Verify the result is null
        assertNull(result);
    }
}
