package com.mybus.busManagement.service;

import com.mybus.busManagement.dto.BusRequestDTO;
import com.mybus.busManagement.dto.BusResponseDTO;
import com.mybus.busManagement.dto.SearchRequestDTO;
import com.mybus.busManagement.entity.Booking;
import com.mybus.busManagement.entity.Booking.BookingStatus;
import com.mybus.busManagement.entity.Bus;
import com.mybus.busManagement.exception.BusinessException;
import com.mybus.busManagement.exception.ResourceNotFoundException;
import com.mybus.busManagement.repository.BookingRepository;
import com.mybus.busManagement.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BusService {
    
    @Autowired
    private BusRepository busRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    // Create a new bus journey
    public BusResponseDTO createBus(BusRequestDTO busRequestDTO) {
        Bus bus = new Bus(
            busRequestDTO.getSource(),
            busRequestDTO.getDestination(),
            busRequestDTO.getJourneyDate(),
            busRequestDTO.getJourneyTime(),
            busRequestDTO.getTotalSeats(),
            busRequestDTO.getFarePerSeat()
        );
        
        Bus savedBus = busRepository.save(bus);
        return convertToResponseDTO(savedBus);
    }
    
    // Get all buses
    public List<BusResponseDTO> getAllBuses() {
        return busRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Get bus by ID
    public BusResponseDTO getBusById(Long id) {
        Bus bus = busRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        return convertToResponseDTO(bus);
    }
    
    // Search buses by source, destination, date, and optionally time
    public List<BusResponseDTO> searchBuses(SearchRequestDTO searchRequest) {
        List<Bus> buses;
        
        if (searchRequest.getJourneyTime() != null) {
            buses = busRepository.findBySourceAndDestinationAndJourneyDateAndJourneyTime(
                searchRequest.getSource(),
                searchRequest.getDestination(),
                searchRequest.getJourneyDate(),
                searchRequest.getJourneyTime()
            );
        } else {
            buses = busRepository.findAvailableBuses(
                searchRequest.getSource(),
                searchRequest.getDestination(),
                searchRequest.getJourneyDate()
            );
        }
        
        return buses.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    // Update bus details
    public BusResponseDTO updateBus(Long id, BusRequestDTO busRequestDTO) {
        Bus bus = busRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        
        bus.setSource(busRequestDTO.getSource());
        bus.setDestination(busRequestDTO.getDestination());
        bus.setJourneyDate(busRequestDTO.getJourneyDate());
        bus.setJourneyTime(busRequestDTO.getJourneyTime());
        bus.setTotalSeats(busRequestDTO.getTotalSeats());
        bus.setFarePerSeat(busRequestDTO.getFarePerSeat());
        
        // Recalculate available seats if total seats changed
        int seatsDifference = busRequestDTO.getTotalSeats() - bus.getTotalSeats();
        bus.setAvailableSeats(bus.getAvailableSeats() + seatsDifference);
        
        Bus updatedBus = busRepository.save(bus);
        return convertToResponseDTO(updatedBus);
    }
    
    // Delete bus
    public void deleteBus(Long id) {
        Bus bus = busRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        
        // Check if there are any CONFIRMED bookings for this bus
        List<Booking> confirmedBookings = bookingRepository.findByBusIdAndBookingStatus(id, BookingStatus.CONFIRMED);
        if (!confirmedBookings.isEmpty()) {
            throw new BusinessException("Cannot delete bus. There are " + confirmedBookings.size() + " confirmed booking(s) associated with this bus. Please cancel the bookings first.");
        }
        
        // Delete all cancelled bookings for this bus to avoid foreign key constraint issues
        List<Booking> cancelledBookings = bookingRepository.findByBusIdAndBookingStatus(id, BookingStatus.CANCELLED);
        if (!cancelledBookings.isEmpty()) {
            bookingRepository.deleteAll(cancelledBookings);
        }
        
        // Now delete the bus
        busRepository.delete(bus);
    }
    
    // Helper method to convert Bus entity to BusResponseDTO
    private BusResponseDTO convertToResponseDTO(Bus bus) {
        return new BusResponseDTO(
            bus.getId(),
            bus.getSource(),
            bus.getDestination(),
            bus.getJourneyDate(),
            bus.getJourneyTime(),
            bus.getTotalSeats(),
            bus.getAvailableSeats(),
            bus.getFarePerSeat()
        );
    }
    
    // Method to get Bus entity by ID (used by BookingService)
    public Bus getBusEntityById(Long id) {
        return busRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
    }
}

