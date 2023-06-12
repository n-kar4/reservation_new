package com.nk.reservation.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int travelRequestId;

    private int reservationDoneByEmployeeId;
    private LocalDate createdOn;
    private String reservationDoneWithEntity;
    private LocalDate reservationDate;
    private int amount;
    private String confirmationID;
    private String remarks;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reservationTypeId")
    private ReservationTypes reservationTypeId;

    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinColumn(name = "reservationDocs")
    // private ReservationDocs reservationDocs;

    @OneToMany(mappedBy = "reservation",fetch=FetchType.EAGER)
    private List<ReservationDocs> reservationDocs;


    //new sample reswrvation object
    //    Reservation reservation = new Reservation();

    
}
