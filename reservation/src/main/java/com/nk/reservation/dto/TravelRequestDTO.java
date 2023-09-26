package com.nk.reservation.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

//class of     int requestId,
    // int raisedByEmployeeId,
    // int toBeApprovedByHRId,
    // LocalDate requestRaisedOn,
    // LocalDate fromDate,
    // LocalDate toDate,
    // String purposeOfTravel,
    // int locationId,
    // String requestStatus,
    // LocalDate requestApprovedOn]\

@Data
@AllArgsConstructor
public class TravelRequestDTO {
    private int requestId;
    private int raisedByEmployeeId;
    private int toBeApprovedByHRId;
    private LocalDate requestRaisedOn;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String purposeOfTravel;
    private int locationId;
    private String requestStatus;
    private LocalDate requestApprovedOn;
}
    
