package com.mybus.busManagement.repository;

import com.mybus.busManagement.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    // Find buses by source, destination, and date
    List<Bus> findBySourceAndDestinationAndJourneyDate(
        String source, String destination, LocalDate journeyDate);
    
    // Find buses by source, destination, date, and time
    List<Bus> findBySourceAndDestinationAndJourneyDateAndJourneyTime(
        String source, String destination, LocalDate journeyDate, LocalTime journeyTime);
    
    // Find buses with available seats
    @Query("SELECT b FROM Bus b WHERE b.source = :source AND b.destination = :destination " +
           "AND b.journeyDate = :journeyDate AND b.availableSeats > 0")
    List<Bus> findAvailableBuses(@Param("source") String source, 
                                  @Param("destination") String destination,
                                  @Param("journeyDate") LocalDate journeyDate);
}

