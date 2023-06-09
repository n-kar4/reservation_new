package com.nk.reservation.entity;

import java.sql.Date;
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

    //forign key
    
    private int reservationDoneByEmployeeId;
    private int travelRequestId;
    private Date createdOn;
    private String reservationDoneWithEntity;
    private Date reservationDate;
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
}
