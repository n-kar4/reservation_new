package com.nk.reservation.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nk.reservation.dto.ReservationDTO;
import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.exceptions.DocumentSizeLimitExceededException;
import com.nk.reservation.exceptions.ResourceNotFoundException;
import com.nk.reservation.services.IReservationServices;
import com.nk.reservation.services.IReservationTypesServices;
import com.nk.reservation.services.mapper.ReservationDTOMappper;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReservationController {

    Logger logger=LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private IReservationServices reservationServices;
    @Autowired
    private IReservationTypesServices reservationTypesServices;
    private ReservationDTOMappper reservationDTOMappper=new ReservationDTOMappper();

    @GetMapping("/types")
    public List<ReservationTypes> getReservationTypes() throws ResourceNotFoundException{
        logger.error("ReservationTypes fetched successfully");
        return reservationTypesServices.getReservationTypes();
    }

    @PostMapping("/add")
    public boolean addNewReservation(@RequestBody Reservation reservation) throws Exception {
        // boolean res = reservationServices.addNewReservation(reservation);
        // return ResponseEntity.ok(res);
        logger.info("Reservation added successfully");
        return reservationServices.addNewReservation(reservation);
    }

    // @PostMapping("/add")
    // public String addReservationaddNewReservation(@RequestBody Reservation reservation) throws ResourceNotFoundException{
    //     String res = reservationServices.addNewReservation(reservation);
    //     logger.info("Reservation added successfully");
    //     return res;
    // }

    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable int id) throws Exception{

        logger.info("Reservation fetched successfully by id");
        Reservation reservation = reservationServices.getReservationById(id);

        return reservationDTOMappper.convertToReservationDTO(reservation);
        
    }

    @GetMapping("/track/{travelRequestId}")
    public List<ReservationDTO> getReservationByTravelRequestId(@PathVariable int travelRequestId) throws Exception{

        logger.info("Reservation fetched successfully by travelRequestId");
        List<Reservation> reservations = reservationServices.getReservationsByTravelRequestId(travelRequestId);

        //convert all the reservation in reservations to reservationDTO
        return reservationDTOMappper.convertToReservationDTOList(reservations);
    }

    @GetMapping("/{reservationId}/download")
    public ResponseEntity<InputStreamResource> downloadReservationDoc(@PathVariable("reservationId") int reservationId) throws Exception{
        try {
            ByteArrayInputStream pdf = reservationServices.downloadReservationsDoc(reservationId);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "inline; filename=lcwd.pdf");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdf));
        } catch (DocumentSizeLimitExceededException ex) {
            // Handle the custom exception here and return an appropriate response
            throw ex;
        } catch (Exception ex) {
            // Handle generic exceptions here and return an appropriate response
            throw ex;
        }
    }


    //NOT REQUIRED
    @CrossOrigin(origins = "*", allowedHeaders = "*")	
    @GetMapping("/")
    public List<ReservationDTO> getAllReservations() {
        logger.info("Reservations fetched successfully");
        List<Reservation> reservations = reservationServices.getAllReservations();
        return reservationDTOMappper.convertToReservationDTOList(reservations);
    }
}
