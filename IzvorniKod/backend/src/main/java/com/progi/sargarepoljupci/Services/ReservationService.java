package com.progi.sargarepoljupci.Services;


import com.progi.sargarepoljupci.DTO.Request.TimeSlot;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final KorisnikService korisnikService;
    private final WalletService walletService;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ParkingSpotRepository parkingSpotRepository, KorisnikService korisnikService, WalletService walletService) {
        this.reservationRepository = reservationRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.korisnikService = korisnikService;
        this.walletService = walletService;
    }
/*
    public boolean isTimeSlotAvailable(LocalDateTime start, LocalDateTime end) {
        List<Reservation> overlappingReservations = findOverlappingReservations(start, end);
        return overlappingReservations.isEmpty();
    }

 */

/*
    public boolean isParkingSpotAvailableForReservation(String parkingSpotId, LocalDateTime timeStart, LocalDateTime timeEnd) {
        List<Reservation> conflictingReservations = reservationRepository.findByParkingSpotIdAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
                parkingSpotId, timeStart, timeEnd);

        return conflictingReservations.isEmpty();
    }
 */

/*
    public List<Reservation> findReservationsForParkingSpots(List<String> parkingSpotIds) {
        return reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
    }

 */

    /*
    public List<ParkingSpot> findReservableParkingSpotsForTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {

        List<ParkingSpot> reservedParkingSpots = findReservedParkingSpotsForTimeSlot(startTime, endTime);


        // prvo trazimo sva koja se mogu rezervirati, onda micemo ova koja nisu slobodna u tom terminu
        List<ParkingSpot> availableParkingSpots = parkingSpotRepository.findParkingSpotsByReservableIsTrue();

        availableParkingSpots.removeAll(reservedParkingSpots);
        return availableParkingSpots;
    }

     */
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
        List<ParkingSpot> availableParkingSpots = parkingSpotRepository.findParkingSpotsByParkingIsNotNull();

        availableParkingSpots.removeAll(reservedParkingSpots);
        return availableParkingSpots;
    }



    public List<Reservation> findOverlappingReservations(
            LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findByReservationStartIsLessThanAndReservationEndGreaterThan(
                endTime, startTime);
    }

    public List<ParkingSpot> findReservableParkingSpotsForTimeSlots(List<TimeSlot> timeSlots) {
        List<ParkingSpot> reservedParkingSpots = new ArrayList<>();

        for (TimeSlot timeSlot : timeSlots) {
            List<Reservation> reservations = findOverlappingReservations(timeSlot.getStartTime(), timeSlot.getEndTime());
            reservedParkingSpots.addAll(reservations.stream()
                    .map(Reservation::getParkingSpot)
                    .distinct()
                    .toList());

        }
        List<ParkingSpot> reservableParkingSpots = parkingSpotRepository.findParkingSpotsByReservableIsTrue();

        reservableParkingSpots.removeAll(reservedParkingSpots);

        return reservableParkingSpots;

    }

    public boolean canParkingSpotBeReserved(String parkingSpotId, LocalDateTime start, LocalDateTime end) {
        List<Reservation> overlappingReservations = reservationRepository.findByParkingSpotIdInAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
                List.of(parkingSpotId),
                end,
                start
        );

        return overlappingReservations.isEmpty();
    }


    // If all timeslots are reservable it will return a Pair of null, and the totalTime;
    // If some timeslots are not reservable it will return a Pair that includes a list of unaccepted TimeSlots and duration=0
    public List<TimeSlot> makeMultipleReservations(Long userId, String parkingSpotId, List<TimeSlot> timeSlots) {

        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));
        long totalDurationInMin = 0L;
        Optional<Korisnik> user = korisnikService.findById(userId);
        if(user.isEmpty()){
            throw new RequestDeniedException("User with that ID doesn't exist");
        }
        List<TimeSlot> unavailableTimeSlots = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            //Check if the parking spot is reservable for the current time slot
            // ovdje trebam round downat startingTime na 30 min
            timeSlot.setStartTime(timeSlot.getStartTime());
            boolean isReservable = canParkingSpotBeReserved(parkingSpotId, timeSlot.getStartTime(), timeSlot.getEndTime());


            if (isReservable) {
                totalDurationInMin += calculateDurationInMinutes(timeSlot.getStartTime(), timeSlot.getEndTime());
                Reservation reservation = new Reservation();
                reservation.setKorisnik(user.get());
                reservation.setParkingSpot(parkingSpot);
                reservation.setReservationStart(timeSlot.getStartTime());
                reservation.setReservationEnd(timeSlot.getEndTime());
                reservation.setDuration(calculateDurationInMinutes(timeSlot.getStartTime(), timeSlot.getEndTime()));
                // add the reservation then save it later if everything is ok
                reservations.add(reservation);
            } else {
               // throw new RequestDeniedException("Not reservable because of Reservation:\n Start:timeSlot.getStartTime().toString()\nEnd:timeSlot.getEndTime().toString()");
                unavailableTimeSlots.add(timeSlot);
            }


        }
        // if every timeslot was available we just return null and the total duration
        if(unavailableTimeSlots.isEmpty()){

            var costPerHour = parkingSpot.getParking().getCostPerHour();
            double totalPrice = costPerHour*((double) totalDurationInMin /60.);
            walletService.withdrawFunds(userId, totalPrice);
            reservationRepository.saveAll(reservations);
            return null;
        }
        return unavailableTimeSlots;
    }

    private int calculateDurationInMinutes(LocalDateTime start, LocalDateTime end) {
        return (int) java.time.Duration.between(start, end).toMinutes();
    }

    public LocalDateTime roundToClosest30Minutes(LocalDateTime dateTime) {
        LocalDateTime roundedDown = dateTime.withSecond(0).withNano(0);
        return roundedDown.minusMinutes(roundedDown.getMinute() % 30);
    }

    public List<Reservation> findByParkingSpotIdIn(Collection<String> parkingSpotIds) {
        return reservationRepository.findByParkingSpotIdIn(parkingSpotIds);
    }


















}
