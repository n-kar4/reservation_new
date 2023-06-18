package com.nk.reservation.services;

import java.util.List;
import java.util.Optional;

import com.nk.reservation.entity.Reservation;

public interface IReservationServices {
    
    public List<Reservation> getAllReservations();
    public void addNewReservation(Reservation reservation);
    public List<Reservation> getReservationsByTravelRequestId(int travelRequestId);
    public byte[] downloadReservationsDoc();
    public Optional<Reservation> getReservationById(int id);

}
