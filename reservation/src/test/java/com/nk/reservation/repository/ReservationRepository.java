package com.nk.reservation.repository;

import com.nk.reservation.entity.Reservation;

public class ReservationRepository {

    @BeforeEach
    void setUp(){
        Reservation reservation = new Reservation();

    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void ReservationRepositoryTest(){
    }
}
