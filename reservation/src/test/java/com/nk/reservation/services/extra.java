// package com.nk.reservation.services;

// import java.time.LocalDate;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import static org.junit.jupiter.api.Assertions.assertEquals;

// import com.nk.reservation.entity.Reservation;
// import com.nk.reservation.entity.ReservationTypes;
// import com.nk.reservation.repository.ReservationRepository;

// public class extra {

//     @Mock
//     private ReservationRepository reservationRepository;

//     @InjectMocks
//     private ReservationServices reservationServices;

//     AutoCloseable autoCloseable;

//     @BeforeEach
//     void setUp(){
//         autoCloseable = MockitoAnnotations.openMocks(this);

//         reservationServices = new ReservationServices(reservationRepository);
        
//         Reservation reservation = new Reservation(1,1000,
//                 1,LocalDate.now(),
//                 "Airline",LocalDate.now(),
//                 1000,"534","remarks",
//                 new ReservationTypes(1,"Flight"), null);

        
//         reservationRepository.save(reservation);
//     }

//     @AfterEach
//     void tearDown() throws Exception{
//         autoCloseable.close();
//     }

//     @Test
//     void getReservationByIdTest(){
//         Reservation reservation = new Reservation(1,1000,
//                 1,LocalDate.now(),
//                 "Airline",LocalDate.now(),
//                 1000,"534","remarks",
//                 new ReservationTypes(1,"Flight"), null);

//         Reservation reservation_expected = reservationRepository.findById(reservation.getId()).orElse(null);
//         assertEquals(reservation_expected, reservationServices.getReservationById(1).orElse(null));
//     }

//     @Test
//     void getReservationByTravelRequestIdTest(){
//                 Reservation reservation = new Reservation(1,1000,
//                 1,LocalDate.now(),
//                 "Airline",LocalDate.now(),
//                 1000,"534","remarks",
//                 new ReservationTypes(1,"Flight"), null);

//         Reservation reservation_expected = reservation;
//         assertEquals(reservation_expected, reservationServices.getReservationsByTravelRequestId(reservation.getTravelRequestId()));
//     }
// }
