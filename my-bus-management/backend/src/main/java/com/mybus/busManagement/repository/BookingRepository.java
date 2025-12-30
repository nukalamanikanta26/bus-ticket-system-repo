package com.mybus.busManagement.repository;

import com.mybus.busManagement.entity.Booking;
import com.mybus.busManagement.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find all bookings by passenger email
    List<Booking> findByPassengerEmail(String passengerEmail);
    
    // Find booking by ID and status
    Optional<Booking> findByIdAndBookingStatus(Long id, BookingStatus status);
    
    // Find all confirmed bookings
    List<Booking> findByBookingStatus(BookingStatus status);
    
    // Find all bookings for a specific bus
    List<Booking> findByBusId(Long busId);
    
    // Find bookings by bus ID and status
    List<Booking> findByBusIdAndBookingStatus(Long busId, BookingStatus status);
    
    // Find bookings by user ID
    List<Booking> findByUserId(Long userId);
}

