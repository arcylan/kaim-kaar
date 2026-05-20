package com.kaim.kaar.controller;

import com.kaim.kaar.DTOs.BookingResponseDTO;
import com.kaim.kaar.entity.Booking;
import com.kaim.kaar.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> bookService(@RequestBody Booking booking){
        bookingService.bookService(booking);
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body("Room Booked Succesfully");
    }
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getProvidersBooking(){
       return  ResponseEntity.ok(bookingService.getProviderBooking());
    }
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptBooking(@PathVariable Long id){
        bookingService.acceptBooking(id);
        return  ResponseEntity.ok("Booking Accepted");
    }
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id){
        bookingService.rejectBooking(id);
        return  ResponseEntity.ok("Booking Rejected");
    }
}
