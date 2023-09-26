package com.nk.reservation.services;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.nk.reservation.entity.Reservation;

public interface IReservationServices {
    
    public List<Reservation> getAllReservations();
    public boolean addNewReservation(Reservation reservation) throws Exception;
    public List<Reservation> getReservationsByTravelRequestId(int travelRequestId) throws Exception;
    public ByteArrayInputStream downloadReservationsDoc(int id) throws Exception;
    public Reservation getReservationById(int id) throws Exception;

}
