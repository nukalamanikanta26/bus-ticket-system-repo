package com.mybus.busManagement.controller;

import com.mybus.busManagement.dto.BookingRequestDTO;
import com.mybus.busManagement.dto.BookingResponseDTO;
import com.mybus.busManagement.entity.User;
import com.mybus.busManagement.exception.BusinessException;
import com.mybus.busManagement.service.AuthService;
import com.mybus.busManagement.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private AuthService authService;
    
    // Helper method to get current user from request
    private User getCurrentUser(HttpServletRequest request) {
        // Try to get username from request attribute first (set by JWT filter)
        String email = (String) request.getAttribute("username");
        
        // If not in request attribute, try to get from SecurityContext
        if (email == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof String) {
                email = (String) authentication.getPrincipal();
            }
        }
        
        if (email == null) {
            throw new BusinessException("User not authenticated. Please login again.");
        }
        return authService.getUserByEmail(email);
    }
    
    // Create a new booking
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO bookingRequestDTO,
            HttpServletRequest request) {
        User user = getCurrentUser(request);
        BookingResponseDTO bookingResponse = bookingService.createBooking(bookingRequestDTO, user);
        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
    }
    
    // Get all bookings (admin only) or user's own bookings
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(HttpServletRequest request) {
        User user = getCurrentUser(request);
        List<BookingResponseDTO> bookings;
        if (user.isAdmin()) {
            bookings = bookingService.getAllBookings();
        } else {
            bookings = bookingService.getBookingsByUser(user);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    
    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }
    
    // Get bookings by passenger email
    @GetMapping("/email/{email}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByEmail(@PathVariable String email) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByEmail(email);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    
    // Cancel a booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            HttpServletRequest request) {
        User user = getCurrentUser(request);
        BookingResponseDTO bookingResponse = bookingService.cancelBooking(id, user);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }
    
    // Get all confirmed bookings
    @GetMapping("/confirmed")
    public ResponseEntity<List<BookingResponseDTO>> getConfirmedBookings() {
        List<BookingResponseDTO> bookings = bookingService.getConfirmedBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    
    // Get all cancelled bookings
    @GetMapping("/cancelled")
    public ResponseEntity<List<BookingResponseDTO>> getCancelledBookings() {
        List<BookingResponseDTO> bookings = bookingService.getCancelledBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}

