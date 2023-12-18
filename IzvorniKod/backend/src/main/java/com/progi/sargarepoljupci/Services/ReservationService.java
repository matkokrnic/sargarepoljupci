package com.progi.sargarepoljupci.Services;


import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    public boolean isTimeSlotAvailable(Timestamp start, Timestamp end) {
        List<Reservation> overlappingReservations = findOverlappingReservations(start, end);
        return overlappingReservations.isEmpty();
    }

    /*
    public boolean isParkingSpotAvailableForReservation(String parkingSpotId, Timestamp timeStart, Timestamp timeEnd) {
        List<Reservation> conflictingReservations = reservationRepository.findByParkingSpotIdAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
                parkingSpotId, timeStart, timeEnd);

        return conflictingReservations.isEmpty();
    }

     */

    public List<Reservation> findReservationsForParkingSpots(List<String> parkingSpotIds) {
        return reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
    }

    public List<ParkingSpot> findReservedParkingSpotsForTimeSlot(Timestamp startTime, Timestamp endTime) {
        List<Reservation> reservations = findOverlappingReservations(startTime, endTime);

        return reservations.stream()
                .map(Reservation::getParkingSpot)
                .distinct()
                .toList();
    }

    public List<ParkingSpot> findAvailableParkingSpotsForTimeSlot(Timestamp startTime, Timestamp endTime) {

        List<ParkingSpot> reservedParkingSpots = findReservedParkingSpotsForTimeSlot(startTime, endTime);
        // prvo trazimo sva dostupna, onda micemo ova koja nisu slobodna u tom terminu
        List<ParkingSpot> availableParkingSpots = parkingSpotRepository.findParkingSpotsByAccessibleIsTrue();

        availableParkingSpots.removeAll(reservedParkingSpots);
        return availableParkingSpots;
    }

    public List<Reservation> findOverlappingReservations(
            Timestamp startTime, Timestamp endTime) {
        return reservationRepository.findByReservationStartIsLessThanAndReservationEndGreaterThan(
                endTime, startTime);
    }








}
