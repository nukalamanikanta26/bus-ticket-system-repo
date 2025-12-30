package com.mybus.busManagement.service;

import com.mybus.busManagement.dto.BookingRequestDTO;
import com.mybus.busManagement.dto.BookingResponseDTO;
import com.mybus.busManagement.entity.Booking;
import com.mybus.busManagement.entity.Booking.BookingStatus;
import com.mybus.busManagement.entity.Bus;
import com.mybus.busManagement.entity.User;
import com.mybus.busManagement.exception.BusinessException;
import com.mybus.busManagement.exception.ResourceNotFoundException;
import com.mybus.busManagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BusService busService;
    
    @Autowired
    private AuthService authService;
    
    // Create a new booking
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO, User user) {
        // Get the bus
        Bus bus = busService.getBusEntityById(bookingRequestDTO.getBusId());
        
        // Check if bus has enough available seats
        if (bus.getAvailableSeats() < bookingRequestDTO.getNumberOfSeats()) {
            throw new BusinessException("Insufficient seats available. Available seats: " + bus.getAvailableSeats());
        }
        
        // Create booking with user
        Booking booking = new Booking(
            bus,
            user,
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            bookingRequestDTO.getNumberOfSeats()
        );
        
        // Update available seats
        bus.setAvailableSeats(bus.getAvailableSeats() - bookingRequestDTO.getNumberOfSeats());
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        return convertToResponseDTO(savedBooking);
    }
    
    // Get all bookings
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Get booking by ID
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return convertToResponseDTO(booking);
    }
    
    // Get bookings by user
    public List<BookingResponseDTO> getBookingsByUser(User user) {
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        return bookings.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Get bookings by passenger email (for backward compatibility)
    public List<BookingResponseDTO> getBookingsByEmail(String email) {
        List<Booking> bookings = bookingRepository.findByPassengerEmail(email);
        return bookings.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Cancel a booking
    public BookingResponseDTO cancelBooking(Long id, User user) {
        Booking booking = bookingRepository.findByIdAndBookingStatus(id, BookingStatus.CONFIRMED)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Booking not found with id: " + id + " or already cancelled"));
        
        // Check if user owns this booking or is admin
        if (!user.isAdmin() && !booking.getUser().getId().equals(user.getId())) {
            throw new BusinessException("You are not authorized to cancel this booking");
        }
        
        // Update booking status
        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancellationDate(LocalDateTime.now());
        
        // Update available seats in bus
        Bus bus = booking.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + booking.getNumberOfSeats());
        
        // Save booking
        Booking cancelledBooking = bookingRepository.save(booking);
        
        return convertToResponseDTO(cancelledBooking);
    }
    
    // Get all confirmed bookings
    public List<BookingResponseDTO> getConfirmedBookings() {
        List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.CONFIRMED);
        return bookings.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Get all cancelled bookings
    public List<BookingResponseDTO> getCancelledBookings() {
        List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.CANCELLED);
        return bookings.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Helper method to convert Booking entity to BookingResponseDTO
    private BookingResponseDTO convertToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
            booking.getId(),
            booking.getBus().getId(),
            booking.getBus().getSource(),
            booking.getBus().getDestination(),
            booking.getBus().getJourneyDate(),
            booking.getBus().getJourneyTime(),
            booking.getPassengerName(),
            booking.getPassengerEmail(),
            booking.getPassengerPhone(),
            booking.getNumberOfSeats(),
            booking.getTotalFare(),
            booking.getBookingStatus().toString(),
            booking.getBookingDate(),
            booking.getCancellationDate()
        );
    }
}

