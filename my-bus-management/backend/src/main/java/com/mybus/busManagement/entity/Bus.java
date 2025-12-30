package com.mybus.busManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "buses")
public class Bus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Source is required")
    @Column(nullable = false)
    private String source;
    
    @NotBlank(message = "Destination is required")
    @Column(nullable = false)
    private String destination;
    
    @NotNull(message = "Journey date is required")
    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;
    
    @NotNull(message = "Journey time is required")
    @Column(name = "journey_time", nullable = false)
    private LocalTime journeyTime;
    
    @NotNull(message = "Total seats is required")
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
    
    @Column(name = "fare_per_seat", nullable = false)
    private Double farePerSeat;
    
    // Default constructor
    public Bus() {
    }
    
    // Constructor with parameters
    public Bus(String source, String destination, LocalDate journeyDate, 
               LocalTime journeyTime, Integer totalSeats, Double farePerSeat) {
        this.source = source;
        this.destination = destination;
        this.journeyDate = journeyDate;
        this.journeyTime = journeyTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
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

