package com.nk.reservation.services;

import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.nk.reservation.dto.ReservationDTO;
import com.nk.reservation.dto.TravelRequestDTO;
import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.exceptions.DocumentSizeLimitExceededException;
import com.nk.reservation.repository.ReservationRepository;
import com.nk.reservation.services.mapper.ReservationDTOMappper;

@Service
public class ReservationServices implements IReservationServices {

    Logger logger = LoggerFactory.getLogger(ReservationServices.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestTemplate restTemplate;

    private ReservationDTOMappper reservationDTOMappper = new ReservationDTOMappper();

    ReservationServices(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean addNewReservation(Reservation reservation) {
        // boolean isValidReservationAmount = checkValidReservationAmount(reservation);
        // boolean isValidReservationTypes = checkValidReservationTypes(reservation);
        // boolean isValidReservationDate = checkValidReservationDate(reservation);

        // if (isValidReservationAmount && isValidReservationTypes && isValidReservationDate) {
        //     reservationRepository.save(reservation);
        //     logger.info("Reservation added successfully");
        //     return true;
        // } else {
        //     logger.info("Reservation not added");
        //     return false;
        // }
        ArrayList<TravelRequestDTO> travelRequestList = restTemplate.getForObject("http://localhost:3000/travelRequests/", ArrayList.class);
        logger.info("{} " + travelRequestList);
        return true;
    }


    @Override
    public List<Reservation> getReservationsByTravelRequestId(int travelRequestId) throws Exception{
        return reservationRepository.findByTravelRequestId(travelRequestId);
    }


    @Override
    public Reservation getReservationById(int id) throws Exception{
        return reservationRepository.findById(id).orElse(null);
    }


    @Override
    public ByteArrayInputStream downloadReservationsDoc(int id) throws Exception{

        Reservation res = getReservationById(id);
        // convert it to reservationdto
        ReservationDTO reservation = reservationDTOMappper.convertToReservationDTO(res);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();

        // Create a font for the chunks
        Font font = FontFactory.getFont(FontFactory.COURIER, 12);

        // Create a paragraph for each field value
        Paragraph idParagraph = new Paragraph("Reservation Id: " + reservation.id(), font);
        Paragraph travelRequestIdParagraph = new Paragraph("Travel Request Id: " + reservation.travelRequestId(), font);
        Paragraph reservationDateParagraph = new Paragraph("Reservation Date: " + reservation.reservationDate(), font);
        Paragraph amountParagraph = new Paragraph("Amount: " + reservation.amount(), font);
        Paragraph confirmationIDParagraph = new Paragraph("Confirmation ID: " + reservation.confirmationID(), font);
        Paragraph remarksParagraph = new Paragraph("Remarks: " + reservation.remarks(), font);
        Paragraph reservationTypeIdParagraph = new Paragraph("Reservation Type: " + reservation.reservationTypeId(),font);

        // Add each paragraph to the document
        document.add(idParagraph);
        document.add(travelRequestIdParagraph);
        document.add(reservationDateParagraph);
        document.add(amountParagraph);
        document.add(confirmationIDParagraph);
        document.add(remarksParagraph);
        document.add(reservationTypeIdParagraph);

        document.close();

        byte[] pdfData = out.toByteArray();
        int pdfSize = pdfData.length;

        // Check if the PDF size exceeds the limit (1MB)
        int maxSize = 1024*1024; // 1MB in bytes
        if (pdfSize > maxSize) {
            throw new DocumentSizeLimitExceededException("PDF size exceeds the limit of 1MB");
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Not Required
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    //Function to check if the reservation DATE is valid
    public boolean checkValidReservationDate(Reservation reservation) {

        
        //get travel start date from travel planner table
        //http://localhost:3000/travelRequests
        
        // ArrayList travelRequestList = restTemplate.getForObject("http://localhost:3000/travelRequests/", ArrayList.class);
        // logger.info("{} " + travelRequestList);
        
        
        
        // sample date, have to fetch from travel planner table   
        LocalDate TravelStartdate = LocalDate.now().plusDays(1);
        
        //get reservation date from reservation table
        LocalDate resvDate = reservation.getReservationDate();
        ReservationTypes reservationType = reservation.getReservationTypeId();

        //check if date is past date
        if(resvDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Reservation date cannot be a past date");
        }

        // Rule a: ReservationDate for a train/bus reservation must be 1 day before the
        // fromdate mentioned in Travel Planner
        if (reservationType.getId() == 2 || reservationType.getId() == 3) {
            if (!TravelStartdate.isEqual(resvDate.plusDays(1))) {
                throw new IllegalArgumentException(
                        "Train/Bus reservation date must be 1 day before the travel plan start date");
            }
        }
        // Rule b: ReservationDate for hotel must be same as the from date in travel
        // plan
        if (reservationType.getId() == 5) {
            if (!TravelStartdate.isEqual(resvDate)) {
                throw new IllegalArgumentException("Hotel reservation date must be same as the travel plan start date");
            }
        }

        return true;
    }

    //Function to check if the reservation TYPE is valid
    public boolean checkValidReservationTypes(Reservation reservation) {

        // Rule c: There must be exactly 3 reservations per travel - one for
        // flight/bus/train, one for hotel, and one for cab travel to hotel from
        // flight/bus/train
        List<Reservation> reservations = reservationRepository.findByTravelRequestId(reservation.getTravelRequestId());
        int reservationCount = reservations.size();

        if (reservationCount >= 3) {
            throw new IllegalArgumentException("There must be exactly 3 reservations per travel request");
        } else if (reservationCount == 0) {
            return true;
        }

        reservations.add(reservation);
        int flightCount = 0;
        int hotelCount = 0;
        int cabCount = 0;
        for (Reservation rsv : reservations) {
            if (rsv.getReservationTypeId().getId() == 1 || rsv.getReservationTypeId().getId() == 2
                    || rsv.getReservationTypeId().getId() == 3) {
                flightCount++;
            }
            if (rsv.getReservationTypeId().getId() == 4) {
                hotelCount++;
            }
            if (rsv.getReservationTypeId().getId() == 5) {
                cabCount++;
            }
        }

        if (flightCount > 1 || hotelCount > 1 || cabCount > 1) {
            throw new IllegalArgumentException(
                    "There must be exactly 1 flight/bus/train reservation, 1 hotel reservation, and 1 cab reservation per travel request");
        }

        return true;
    }

    //Function to check if the reservation AMOUNT is valid
    public boolean checkValidReservationAmount(Reservation reservation) {

        if (reservation.getAmount() < 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // sample budget, have to fetch from travel planner table
        double travelBudget = 1000;
        
        // d. The amount of all 3 reservations must not exceed the 70% of the allocated budget
        List<Reservation> reservations = reservationRepository.findByTravelRequestId(reservation.getTravelRequestId());

        reservations.add(reservation);
        double totalAmount = 0;
        for (Reservation rsv : reservations) {
            totalAmount += rsv.getAmount();
        }

        if (totalAmount > 0.7 * travelBudget) {
            throw new IllegalArgumentException(
                    "The amount of all 3 reservations must not exceed the 70% of the allocated budget");
        }

        return true;
    }
}
