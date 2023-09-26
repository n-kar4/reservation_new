package com.nk.reservation.services.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.nk.reservation.dto.ReservationDTO;
import com.nk.reservation.entity.Reservation;

public class ReservationDTOMappper {

    // to convert single Reservation to ReservationDTO
    public ReservationDTO convertToReservationDTO(Reservation reservation) {
        return new ReservationDTO(
            reservation.getId(),
            reservation.getTravelRequestId(),
            reservation.getReservationDate(),
            reservation.getAmount(),
            reservation.getConfirmationID(),
            reservation.getRemarks(),
            reservation.getReservationTypeId().getTypeName()
        );
    }



    // to convert single ReservationDTO to Reservation
    public static Reservation convertToReservation(ReservationDTO reservationDTO) {

        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.id());
        reservation.setTravelRequestId(reservationDTO.travelRequestId());
        reservation.setReservationDate(reservationDTO.reservationDate());
        reservation.setAmount(reservationDTO.amount());
        reservation.setConfirmationID(reservationDTO.confirmationID());
        reservation.setRemarks(reservationDTO.remarks());
        reservation.getReservationTypeId().setTypeName(reservationDTO.reservationTypeId());

        return reservation;
    }

    // to convert List<Reservation> to List<ReservationDTO>
    public List<ReservationDTO> convertToReservationDTOList(List<Reservation> reservations) {
        return reservations.stream().map(this::convertToReservationDTO).collect(Collectors.toList());
    }
    
}

