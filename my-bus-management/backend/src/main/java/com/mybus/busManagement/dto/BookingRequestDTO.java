package com.mybus.busManagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequestDTO {
    
    @NotNull(message = "Bus ID is required")
    private Long busId;
    
    @NotNull(message = "Number of seats is required")
    @Positive(message = "Number of seats must be positive")
    private Integer numberOfSeats;
    
    // Default constructor
    public BookingRequestDTO() {
    }
    
    // Constructor with parameters
    public BookingRequestDTO(Long busId, Integer numberOfSeats) {
        this.busId = busId;
        this.numberOfSeats = numberOfSeats;
    }
    
    // Getters and Setters
    public Long getBusId() {
        return busId;
    }
    
    public void setBusId(Long busId) {
        this.busId = busId;
    }
    
    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }
    
    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}

