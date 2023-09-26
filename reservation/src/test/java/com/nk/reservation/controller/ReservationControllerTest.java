package com.nk.reservation.controller;

import com.nk.reservation.dto.ReservationDTO;
import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.exceptions.ResourceNotFoundException;
import com.nk.reservation.services.IReservationServices;
import com.nk.reservation.services.IReservationTypesServices;
import com.nk.reservation.services.mapper.ReservationDTOMappper;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private IReservationServices reservationServices;

    @Mock
    private IReservationTypesServices reservationTypesServices;

    @InjectMocks
    private ReservationController reservationController;

    private ReservationDTOMappper reservationDTOMapper = new ReservationDTOMappper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReservationTypes() throws ResourceNotFoundException {
        List<ReservationTypes> reservationTypesList = new ArrayList<>();
        reservationTypesList.add(new ReservationTypes(1, "Flight"));
        reservationTypesList.add(new ReservationTypes(2, "Hotel"));

        when(reservationTypesServices.getReservationTypes()).thenReturn(reservationTypesList);

        List<ReservationTypes> result = reservationController.getReservationTypes();

        assertEquals(reservationTypesList, result);
    }

    @Test
    public void testAddNewReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setTravelRequestId(1);
        reservation.setReservationDoneByEmployeeId(1);
        reservation.setCreatedOn(LocalDate.now());
        reservation.setReservationDoneWithEntity("Airline");
        reservation.setReservationDate(LocalDate.of(2021, 9, 1));
        reservation.setAmount(1000);
        reservation.setConfirmationID("123456");
        reservation.setRemarks("No remarks");
        reservation.setReservationTypeId(new ReservationTypes(1, "Flight"));
        reservation.setReservationDocs(new ArrayList<>());

        reservationController.addNewReservation(reservation);

        verify(reservationServices, times(1)).addNewReservation(reservation);
    }

    @Test
    public void testGetReservationById() throws Exception {
        int reservationId = 1;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setTravelRequestId(1);
        reservation.setReservationDate(LocalDate.of(2021, 9, 1));
        reservation.setAmount(1000);
        reservation.setConfirmationID("123456");
        reservation.setRemarks("No remarks");
        reservation.setReservationTypeId(new ReservationTypes(1, "Flight"));
        reservation.setReservationDocs(new ArrayList<>());

        when(reservationServices.getReservationById(reservationId)).thenReturn(reservation);

        ReservationDTO expectedReservationDTO = reservationDTOMapper.convertToReservationDTO(reservation);
        ReservationDTO result = reservationController.getReservationById(reservationId);

        assertEquals(expectedReservationDTO, result);
    }

    @Test
    public void testGetReservationByTravelRequestId() throws Exception {
        int travelRequestId = 1;
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservation1.setId(1);
        reservation1.setTravelRequestId(travelRequestId);
        reservation1.setReservationDate(LocalDate.of(2021, 9, 1));
        reservation1.setAmount(1000);
        reservation1.setConfirmationID("123456");
        reservation1.setRemarks("No remarks");
        reservation1.setReservationTypeId(new ReservationTypes(1, "Flight"));
        reservation1.setReservationDocs(new ArrayList<>());
        reservations.add(reservation1);

        when(reservationServices.getReservationsByTravelRequestId(travelRequestId)).thenReturn(reservations);

        List<ReservationDTO> expectedReservationDTOList = reservationDTOMapper.convertToReservationDTOList(reservations);
        List<ReservationDTO> result = reservationController.getReservationByTravelRequestId(travelRequestId);

        assertEquals(expectedReservationDTOList, result);
    }



//@Test
//public void testDownloadReservationDoc() throws ResourceNotFoundException, IOException {
//    int reservationId = 1;
//    Reservation reservation = new Reservation();
//        reservation.setId(reservationId);
//        reservation.setTravelRequestId(1);
//        reservation.setReservationDate(LocalDate.of(2021, 9, 1));
//        reservation.setAmount(1000);
//        reservation.setConfirmationID("123456");
//        reservation.setRemarks("No remarks");
//        reservation.setReservationTypeId(new ReservationTypes(1, "Flight"));
//        reservation.setReservationDocs(new ArrayList<>());
//
//    // Mock the behavior of the reservationServices
//    when(reservationServices.getReservationById(reservationId)).thenReturn(reservation);
//
//    // Call the controller method
//    ResponseEntity<InputStreamResource> responseEntity = reservationController.downloadReservationDoc(reservationId);
//
//    // Check the response
//    assertEquals(MediaType.APPLICATION_PDF, responseEntity.getHeaders().getContentType());
//    assertEquals("inline;filename=lcwd.pdf", responseEntity.getHeaders().get("Content-Disposition").get(0));
//
//    // Get the PDF content as a string
//    String pdfContent = convertPdfToString((ByteArrayInputStream) responseEntity.getBody().getInputStream());
//
//    // Compare the PDF content with the expected string
//    String expectedContent = getExpectedDocumentContent(reservation);
//    assertEquals(expectedContent.trim(), pdfContent.trim());
//
//    // Verify that the reservationServices method was called
//    verify(reservationServices, times(1)).getReservationById(reservationId);
//}
//
//public String convertPdfToString(ByteArrayInputStream inputStream) {
//    try (PDDocument document = Loader.loadPDF(inputStream)) {
//        PDFTextStripper stripper = new PDFTextStripper();
//        return stripper.getText(document);
//    } catch (IOException e) {
//        // Handle any exceptions here
//        e.printStackTrace();
//        return null;
//    }
//}
//
//private String getExpectedDocumentContent(Reservation reservation) {
//    StringBuilder sb = new StringBuilder();
//    sb.append("id: ").append(reservation.getId()).append("\n");
//    sb.append("travelRequestId: ").append(reservation.getTravelRequestId()).append("\n");
//    sb.append("reservationDoneByEmployeeId: ").append(reservation.getReservationDoneByEmployeeId()).append("\n");
//    sb.append("createdOn: ").append(reservation.getCreatedOn()).append("\n");
//    sb.append("reservationDoneWithEntity: ").append(reservation.getReservationDoneWithEntity()).append("\n");
//    sb.append("reservationDate: ").append(reservation.getReservationDate()).append("\n");
//    sb.append("amount: ").append(reservation.getAmount()).append("\n");
//    sb.append("confirmationID: ").append(reservation.getConfirmationID()).append("\n");
//    sb.append("remarks: ").append(reservation.getRemarks()).append("\n");
//    sb.append(reservation.getReservationTypeId()).append("\n");
//    sb.append("reservationDocs: ").append(reservation.getReservationDocs());
//
//    return sb.toString();
//}

    // Add more test cases for other scenarios if required
}
