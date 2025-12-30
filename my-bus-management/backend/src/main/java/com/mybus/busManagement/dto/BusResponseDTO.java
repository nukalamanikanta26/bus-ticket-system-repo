package com.mybus.busManagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BusResponseDTO {
    
    private Long id;
    private String source;
    private String destination;
    private LocalDate journeyDate;
    private LocalTime journeyTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double farePerSeat;
    
    // Default constructor
    public BusResponseDTO() {
    }
    
    // Constructor with parameters
    public BusResponseDTO(Long id, String source, String destination, LocalDate journeyDate, 
                         LocalTime journeyTime, Integer totalSeats, Integer availableSeats, 
                         Double farePerSeat) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.journeyDate = journeyDate;
        this.journeyTime = journeyTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.farePerSeat = farePerSeat;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Integer getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public Double getFarePerSeat() {
        return farePerSeat;
    }
    
    public void setFarePerSeat(Double farePerSeat) {
        this.farePerSeat = farePerSeat;
    }
}

