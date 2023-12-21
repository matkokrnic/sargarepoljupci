package com.progi.sargarepoljupci.Services;


import com.progi.sargarepoljupci.DTO.Request.TimeSlotRequest;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ParkingSpotRepository parkingSpotRepository) {
        this.reservationRepository = reservationRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public boolean isTimeSlotAvailable(LocalDateTime start, LocalDateTime end) {
        List<Reservation> overlappingReservations = findOverlappingReservations(start, end);
        return overlappingReservations.isEmpty();
    }

/*
    public boolean isParkingSpotAvailableForReservation(String parkingSpotId, LocalDateTime timeStart, LocalDateTime timeEnd) {
        List<Reservation> conflictingReservations = reservationRepository.findByParkingSpotIdAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
                parkingSpotId, timeStart, timeEnd);

        return conflictingReservations.isEmpty();
    }
 */


    public List<Reservation> findReservationsForParkingSpots(List<String> parkingSpotIds) {
        return reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
    }

    public List<ParkingSpot> findReservedParkingSpotsForTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> reservations = findOverlappingReservations(startTime, endTime);

        return reservations.stream()
                .map(Reservation::getParkingSpot)
                .distinct()
                .toList();
    }

    public List<ParkingSpot> findAvailableParkingSpotsForTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {

        List<ParkingSpot> reservedParkingSpots = findReservedParkingSpotsForTimeSlot(startTime, endTime);


        // prvo trazimo sva dostupna, onda micemo ova koja nisu slobodna u tom terminu
        List<ParkingSpot> availableParkingSpots = parkingSpotRepository.findParkingSpotsByAccessibleIsTrue();

        availableParkingSpots.removeAll(reservedParkingSpots);
        return availableParkingSpots;
    }

    public List<Reservation> findOverlappingReservations(
            LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findByReservationStartIsLessThanAndReservationEndGreaterThan(
                endTime, startTime);
    }








}
