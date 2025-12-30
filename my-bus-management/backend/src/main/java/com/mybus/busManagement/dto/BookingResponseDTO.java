package com.mybus.busManagement.dto;

import java.time.LocalDateTime;

public class BookingResponseDTO {

private Long id;
private Long busId;
private String source;
private String destination;
private java.time.LocalDate journeyDate;
private java.time.LocalTime journeyTime;
private String passengerName;
private String passengerEmail;
private String passengerPhone;
private Integer numberOfSeats;
private Double totalFare;
private String bookingStatus;
private LocalDateTime bookingDate;
private LocalDateTime cancellationDate;

// Default constructor
public BookingResponseDTO() {
}

// Constructor with parameters
public BookingResponseDTO(Long id, Long busId, String source, String destination,
                        java.time.LocalDate journeyDate, java.time.LocalTime journeyTime,
                        String passengerName, String passengerEmail, String passengerPhone,
                        Integer numberOfSeats, Double totalFare, String bookingStatus,
                        LocalDateTime bookingDate, LocalDateTime cancellationDate) {
this.id = id;
this.busId = busId;
this.source = source;
this.destination = destination;
this.journeyDate = journeyDate;
this.journeyTime = journeyTime;
this.passengerName = passengerName;
this.passengerEmail = passengerEmail;
this.passengerPhone = passengerPhone;
this.numberOfSeats = numberOfSeats;
this.totalFare = totalFare;
this.bookingStatus = bookingStatus;
this.bookingDate = bookingDate;
this.cancellationDate = cancellationDate;
}

// Getters and Setters
public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public Long getBusId() {
return busId;
}

public void setBusId(Long busId) {
this.busId = busId;
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

public java.time.LocalDate getJourneyDate() {
return journeyDate;
}

public void setJourneyDate(java.time.LocalDate journeyDate) {
this.journeyDate = journeyDate;
}

public java.time.LocalTime getJourneyTime() {
return journeyTime;
}

public void setJourneyTime(java.time.LocalTime journeyTime) {
this.journeyTime = journeyTime;
}

public String getPassengerName() {
return passengerName;
}

public void setPassengerName(String passengerName) {
this.passengerName = passengerName;
}

public String getPassengerEmail() {
return passengerEmail;
}

public void setPassengerEmail(String passengerEmail) {
this.passengerEmail = passengerEmail;
}

public String getPassengerPhone() {
return passengerPhone;
}

public void setPassengerPhone(String passengerPhone) {
this.passengerPhone = passengerPhone;
}

public Integer getNumberOfSeats() {
return numberOfSeats;
}

public void setNumberOfSeats(Integer numberOfSeats) {
this.numberOfSeats = numberOfSeats;
}

public Double getTotalFare() {
return totalFare;
}

public void setTotalFare(Double totalFare) {
this.totalFare = totalFare;
}

public String getBookingStatus() {
return bookingStatus;
}

public void setBookingStatus(String bookingStatus) {
this.bookingStatus = bookingStatus;
}

public LocalDateTime getBookingDate() {
return bookingDate;
}

public void setBookingDate(LocalDateTime bookingDate) {
this.bookingDate = bookingDate;
}

public LocalDateTime getCancellationDate() {
return cancellationDate;
}

public void setCancellationDate(LocalDateTime cancellationDate) {
this.cancellationDate = cancellationDate;
}
}

