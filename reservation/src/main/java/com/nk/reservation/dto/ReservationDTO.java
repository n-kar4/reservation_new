package com.nk.reservation.dto;

import java.time.LocalDate;

public record ReservationDTO(
    int id,
    int travelRequestId,
    LocalDate reservationDate,
    int amount,
    String confirmationID,
    String remarks,
    String reservationTypeId
) {
    
}