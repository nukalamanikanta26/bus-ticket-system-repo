package com.mybus.busManagement.controller;

import com.mybus.busManagement.dto.BusRequestDTO;
import com.mybus.busManagement.dto.BusResponseDTO;
import com.mybus.busManagement.dto.SearchRequestDTO;
import com.mybus.busManagement.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buses")
@CrossOrigin(origins = "*")
public class BusController {
    
    @Autowired
    private BusService busService;
    
    // Create a new bus journey
    @PostMapping
    public ResponseEntity<BusResponseDTO> createBus(@Valid @RequestBody BusRequestDTO busRequestDTO) {
        BusResponseDTO busResponse = busService.createBus(busRequestDTO);
        return new ResponseEntity<>(busResponse, HttpStatus.CREATED);
    }
    
    // Get all buses
    @GetMapping
    public ResponseEntity<List<BusResponseDTO>> getAllBuses() {
        List<BusResponseDTO> buses = busService.getAllBuses();
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Get bus by ID
    @GetMapping("/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable Long id) {
        BusResponseDTO bus = busService.getBusById(id);
        return new ResponseEntity<>(bus, HttpStatus.OK);
    }
    
    // Search buses by source, destination, date, and optionally time
    @PostMapping("/search")
    public ResponseEntity<List<BusResponseDTO>> searchBuses(@Valid @RequestBody SearchRequestDTO searchRequest) {
        List<BusResponseDTO> buses = busService.searchBuses(searchRequest);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    // Update bus details
    @PutMapping("/{id}")
    public ResponseEntity<BusResponseDTO> updateBus(@PathVariable Long id, 
                                                    @Valid @RequestBody BusRequestDTO busRequestDTO) {
        BusResponseDTO busResponse = busService.updateBus(id, busRequestDTO);
        return new ResponseEntity<>(busResponse, HttpStatus.OK);
    }
    
    // Delete bus
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return new ResponseEntity<>(Map.of("message", "Bus deleted successfully"), HttpStatus.OK);
    }
}

