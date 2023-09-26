package com.nk.reservation.services;

import com.nk.reservation.dto.ReservationDTO;
import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;
import com.nk.reservation.services.mapper.ReservationDTOMappper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

class ReservationServicesTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServices reservationServices;

    Reservation reservation;

    private ReservationDTOMappper reservationDTOMappper= new ReservationDTOMappper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reservation = new Reservation();
        reservation.setId(1);
        reservation.setTravelRequestId(1);
        reservation.setReservationDoneByEmployeeId(1);
        reservation.setCreatedOn(LocalDate.now());
        reservation.setReservationDoneWithEntity("Airline");
        reservation.setReservationDate(LocalDate.of(2021, 9, 1));
        reservation.setAmount(1);
        reservation.setConfirmationID("123456");
        reservation.setRemarks("No remarks");
        reservation.setReservationTypeId(new ReservationTypes(1, "Flight"));
        reservation.setReservationDocs(Collections.emptyList());
    }



    @Test
    void testGetReservationByTravelRequestId() throws Exception {
        // Mock the repository's behavior
        int travelRequestId = 30;
        Reservation reservation = new Reservation();
        reservation.setTravelRequestId(travelRequestId);
        List<Reservation> reservationList = Collections.singletonList(reservation);
        when(reservationRepository.findByTravelRequestId(travelRequestId)).thenReturn(reservationList);

        // Call the service method for success
        List<Reservation> result1 = reservationServices.getReservationsByTravelRequestId(travelRequestId);
        assertEquals(reservationList, result1);

        // Call the service method for failure
        Random random = new Random(); // Math.random() returns double, but int needed
        List<Reservation> result2 = reservationServices
                .getReservationsByTravelRequestId(travelRequestId + (random.nextInt(100) + 1));
        assertNotEquals(reservationList, result2);
    }

    @Test
    void testGetReservationById() throws Exception {
        // Mock the repository's behavior
        int id = 1;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        // Call the service method for success
        Reservation result_valid = reservationServices.getReservationById(id);
        assertEquals(reservation, result_valid);

        // Call the service method for failure
        Random random = new Random(); // Math.random() returns double, but int needed
        Reservation result_inv = reservationServices.getReservationById(id + (random.nextInt() + 1));
        assertNull(result_inv);
    }

    // @Test
    // void testGetReservationById() {
    // // Mock the repository's behavior
    // int id = 1;
    // Optional<Reservation> optionalReservation = Optional.of(new Reservation());
    // when(reservationRepository.findById(id)).thenReturn(optionalReservation);

    // // Call the service method for success
    // Reservation result_valid = reservationServices.getReservationById(id);
    // assertEquals(optionalReservation, result_valid);

    // // Call the service method for failure
    // Random random = new Random(); //Math.random() returns double, but int needed
    // Reservation result_inv =
    // reservationServices.getReservationById(id+(random.nextInt()+1));
    // assertNotEquals(optionalReservation, result_inv);
    // }

    @Test
    void testAddNewReservation_ValidReservationDate() {
        // Arrange
        Reservation validReservation_1 = new Reservation();
        validReservation_1.setReservationDate(LocalDate.now());
        validReservation_1.setReservationTypeId(new ReservationTypes(1, ""));

        // Act
        reservationServices.addNewReservation(validReservation_1);

        // Assert
        verify(reservationRepository, times(1)).save(validReservation_1);

        // Arrange
        Reservation validReservation_2 = new Reservation();
        validReservation_2.setReservationDate(LocalDate.now().plusDays(1));
        validReservation_2.setReservationTypeId(new ReservationTypes(5, ""));

        // Act
        reservationServices.addNewReservation(validReservation_2);

        // Assert
        verify(reservationRepository, times(1)).save(validReservation_2);
    }

    @Test
    void testAddNewReservation_ExceedingAmount() {

        Reservation reservation = new Reservation();
        reservation.setAmount(999999999);

        verify(reservationRepository, times(0)).save(reservation);

        reservation.setAmount(-100);
        verify(reservationRepository, times(0)).save(reservation);
    }

    @Test
    void testAddNewReservation_ExceedingCount() {
        // Arrange
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(new Reservation(100, 100, 100, LocalDate.now(), "Airline", LocalDate.now(), 100, "123456", "No remarks", new ReservationTypes(1, ""), Collections.emptyList()));
        existingReservations.add(new Reservation(100, 100, 100, LocalDate.now(), "Airline", LocalDate.now(), 100, "123456", "No remarks", new ReservationTypes(4, ""), Collections.emptyList()));
        existingReservations.add(new Reservation(100, 100, 100, LocalDate.now(), "Airline", LocalDate.now(), 100, "123456", "No remarks", new ReservationTypes(5, ""), Collections.emptyList()));

        Reservation exceedingCountReservation = new Reservation(100, 100, 100, LocalDate.now(), "Airline", LocalDate.now(), 100, "123456", "No remarks", new ReservationTypes(1, ""), Collections.emptyList());

        // Verify that the save method was not called
        verify(reservationRepository, times(0)).save(exceedingCountReservation);
    }

    @Test
    void testAddNewReservation_ValidTypes() {
        // Arrange
        Reservation reservation_1 = new Reservation();
        reservation_1.setReservationTypeId(new ReservationTypes(1, ""));
        reservationRepository.save(reservation_1);

        Reservation exceedingCountReservation = new Reservation();
        exceedingCountReservation.setReservationTypeId(new ReservationTypes(3, ""));

        // Verify that the save method was not called
        verify(reservationRepository, times(0)).save(exceedingCountReservation);

        exceedingCountReservation.setReservationTypeId(new ReservationTypes(5, ""));
    }

    @Test
    public void testDownloadReservationsDoc1() throws Exception {
        // Create a sample Reservation object
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setTravelRequestId(123);
        reservation.setReservationDoneByEmployeeId(456);
        reservation.setCreatedOn(LocalDate.now()); // Set the createdOn field to the current date
        reservation.setReservationDoneWithEntity("Airline");
        reservation.setReservationDate(LocalDate.of(2023, 6, 17)); // Set a specific reservation date
        reservation.setAmount(1000);
        reservation.setConfirmationID("123456");
        reservation.setRemarks("No remarks");
        reservation.setReservationTypeId(new ReservationTypes(1, "Flight"));
        reservation.setReservationDocs(Collections.emptyList()); // Set an empty list for reservationDocs

        // Mock the reservationRepository's findById method
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        // Call the downloadReservationsDoc method
        ByteArrayInputStream resultStream = reservationServices.downloadReservationsDoc(1);

        // Convert the PDF content to a string
        String pdfContent = convertPdfToString(resultStream).trim();

        // TODO: Compare the PDF content with the expected string using assertions
        String expectedContent = getExpectedDocumentContent(reservationDTOMappper.convertToReservationDTO(reservation));

        System.out.println(expectedContent);
        System.out.println(pdfContent);
        String[] expectedLines = expectedContent.split("\n");
        String[] actualLines = pdfContent.split("\n");

        // Compare each line of expected and actual content
        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i].trim(), actualLines[i].trim());
        }
    }

    public String convertPdfToString(ByteArrayInputStream inputStream) {
        try (PDDocument document = Loader.loadPDF(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            // Handle any exceptions here
            e.printStackTrace();
            return null;
        }
    }

    private String getExpectedDocumentContent(ReservationDTO reservationDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation Id: ").append(reservationDTO.id()).append("\n");
        sb.append("Travel Request Id: ").append(reservationDTO.travelRequestId()).append("\n");
        sb.append("Reservation Date: ").append(reservationDTO.reservationDate()).append("\n");
        sb.append("Amount: ").append(reservationDTO.amount()).append("\n");
        sb.append("Confirmation ID: ").append(reservationDTO.confirmationID()).append("\n");
        sb.append("Remarks: ").append(reservationDTO.remarks()).append("\n");
        sb.append("Reservation Type: ").append(reservationDTO.reservationTypeId()).append("\n");
    
        return sb.toString();
    }
    

    @Test
    public void testAddNewReservation_ExceedBudgetLimit_ThrowsException() {
        // Create a travel request with a budget of 1000
        int travelRequestId = 2;
        int budget = 1000;
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(createExistingReservation(travelRequestId, 1, "Flight"));
        existingReservations.add(createExistingReservation(travelRequestId, 2, "Hotel"));
        when(reservationRepository.findByTravelRequestId(travelRequestId)).thenReturn(existingReservations);

        // Create a new reservation that exceeds the budget limit
        Reservation r1 = reservation;
        r1.setAmount((int) (budget*0.7) + 1); // Exceed the budget by 1

        // Call the addNewReservation method and expect an exception
        assertThrows(IllegalArgumentException.class, () -> reservationServices.addNewReservation(reservation));
    }

    @Test
    public void testAddNewReservation_ValidThreeReservations() {
        // Create a travel request and add three existing reservations of different types
        int travelRequestId = 3;
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(createExistingReservation(travelRequestId, 1, "Flight"));
        existingReservations.add(createExistingReservation(travelRequestId, 4, "Hotel"));
        existingReservations.add(createExistingReservation(travelRequestId, 5, "Cab"));
        when(reservationRepository.findByTravelRequestId(travelRequestId)).thenReturn(existingReservations);

        // Create a new reservation of a different type
        Reservation r1 = reservation;
        reservation.setReservationTypeId(new ReservationTypes(6, "Another Type"));

        // Call the addNewReservation method and expect that it returns string "Success"
        assertTrue(reservationServices.addNewReservation(r1));
    }

    // TODO: Add more test cases to cover other scenarios and conditions


    // Helper method for creating an existing reservation
    private Reservation createExistingReservation(int travelRequestId, int reservationTypeId, String reservationType) {
        Reservation reservation = new Reservation();
        reservation.setId(travelRequestId);
        reservation.setTravelRequestId(travelRequestId);
        reservation.setReservationTypeId(new ReservationTypes(reservationTypeId, reservationType));
        reservation.setAmount(200);
        return reservation;
    }


}
