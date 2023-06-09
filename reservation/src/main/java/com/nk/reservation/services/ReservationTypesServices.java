package com.nk.reservation.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationTypesRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
// import jakarta.persistence.Query;

@Service
public class ReservationTypesServices implements IReservationTypesServices {

    @Autowired
    private ReservationTypesRepository reservationTypesRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReservationTypes> getReservationTypes() {
        return reservationTypesRepo.findAll();

        // Query query = entityManager.createQuery("SELECT reservationTypeName FROM ReservationType");
        // List<String> reservationTypeNames = query.getResultList();
        // return reservationTypeNames;
    }

    @Override
    public void addIntoReservationType(ReservationTypes reservationTypes) {
        
        reservationTypesRepo.save(reservationTypes);
    }
    

    
}
