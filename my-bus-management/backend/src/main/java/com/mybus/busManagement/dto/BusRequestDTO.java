package com.mybus.busManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public class BusRequestDTO {
    
    @NotBlank(message = "Source is required")
    private String source;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Journey date is required")
    private LocalDate journeyDate;
    
    @NotNull(message = "Journey time is required")
    private LocalTime journeyTime;
    
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;
    
    @NotNull(message = "Fare per seat is required")
    @Positive(message = "Fare per seat must be positive")
    private Double farePerSeat;
    
    // Default constructor
    public BusRequestDTO() {
    }
    
    // Constructor with parameters
    public BusRequestDTO(String source, String destination, LocalDate journeyDate, 
                        LocalTime journeyTime, Integer totalSeats, Double farePerSeat) {
        this.source = source;
        this.destination = destination;
        this.journeyDate = journeyDate;
        this.journeyTime = journeyTime;
        this.totalSeats = totalSeats;
        this.farePerSeat = farePerSeat;
    }
    
    // Getters and Setters
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public LocalDate getJourneyDate() {
        return journeyDate;
    }
    
    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }
    
    public LocalTime getJourneyTime() {
        return journeyTime;
    }
    
    public void setJourneyTime(LocalTime journeyTime) {
        this.journeyTime = journeyTime;
    }
    
    public Integer getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public Double getFarePerSeat() {
        return farePerSeat;
    }
    
    public void setFarePerSeat(Double farePerSeat) {
        this.farePerSeat = farePerSeat;
    }
}

