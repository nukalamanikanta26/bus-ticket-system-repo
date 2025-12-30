package com.mybus.busManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Passenger name is required")
    @Column(name = "passenger_name", nullable = false)
    private String passengerName;
    
    @NotBlank(message = "Passenger email is required")
    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;
    
    @NotBlank(message = "Passenger phone is required")
    @Column(name = "passenger_phone", nullable = false)
    private String passengerPhone;
    
    @NotNull(message = "Number of seats is required")
    @Positive(message = "Number of seats must be positive")
    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;
    
    @Column(name = "total_fare", nullable = false)
    private Double totalFare;
    
    @Column(name = "booking_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;
    
    // Default constructor
    public Booking() {
        this.bookingStatus = BookingStatus.CONFIRMED;
        this.bookingDate = LocalDateTime.now();
    }
    
    // Constructor with parameters
    public Booking(Bus bus, User user, String passengerName, String passengerEmail, 
                   String passengerPhone, Integer numberOfSeats) {
        this.bus = bus;
        this.user = user;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.numberOfSeats = numberOfSeats;
        this.totalFare = bus.getFarePerSeat() * numberOfSeats;
        this.bookingStatus = BookingStatus.CONFIRMED;
        this.bookingDate = LocalDateTime.now();
    }
    
    // Enum for booking status
    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Bus getBus() {
        return bus;
    }
    
    public void setBus(Bus bus) {
        this.bus = bus;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }
    
    public void setBookingStatus(BookingStatus bookingStatus) {
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

