package com.mybus.busManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class SearchRequestDTO {
    
    @NotBlank(message = "Source is required")
    private String source;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Journey date is required")
    private LocalDate journeyDate;
    
    private LocalTime journeyTime; // Optional
    
    // Default constructor
    public SearchRequestDTO() {
    }
    
    // Constructor with parameters
    public SearchRequestDTO(String source, String destination, LocalDate journeyDate, LocalTime journeyTime) {
        this.source = source;
        this.destination = destination;
        this.journeyDate = journeyDate;
        this.journeyTime = journeyTime;
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
}

