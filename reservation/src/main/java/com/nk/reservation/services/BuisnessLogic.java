package com.nk.reservation.services;

import java.time.LocalDate;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;

public class BuisnessLogic {
    
    private boolean checkValidReservation(Reservation reservation){

        String msg="";

        //sample date, have to fetch from travel planner table
        LocalDate formdate = LocalDate.of(2024, 6, 15); 
        ReservationTypes reservationType = reservation.getReservationTypeId();

        // Rule a: ReservationDate for a train/bus reservation must be 1 day before the fromdate mentioned in Travel Planner
        if (reservationType.getId() == 1 || reservationType.getId() == 2) {
            // LocalDate reservationDate = travelPlan.getFromDate().minusDays(1);
            if (!formdate.isEqual(reservation.getReservationDate())) {
                // return false;
                msg+="\ndate is not valid as per rule a, recheck";
            }
        }

        // Rule b: ReservationDate for hotel must be same as the from date in travel plan
        if (reservationType.getId() == 3) {
            if (!formdate.isEqual(reservation.getReservationDate())) {
                // return false;
                msg+="\ndate is not valid as per rule b, recheck";
            }
        }

        // Rule c: There must be exactly 3 reservations per travel - one for flight/bus/train, one for hotel, and one for cab travel to hotel from flight/bus/train
        // int reservationCount = getReservationCount(travelPlan);
        int reservationCount = 3;
        if (reservationCount >= 3) {
            // return false;
            msg+="\nThere must be 3 reservations per travel";
        }

        // Rule d: The amount of all 3 reservations must not exceed 70% of the allocated budget
        // double totalReservationAmount = reservation.getAmount() + getReservationAmount(travelPlan);
        // double maxAllowedAmount = allocatedBudget * 0.7;
        // if (totalReservationAmount > maxAllowedAmount) {
        //     return false;
        // }

        if(msg!=""){
            throw new IllegalArgumentException(msg);
        }

        return true;
    }
}
