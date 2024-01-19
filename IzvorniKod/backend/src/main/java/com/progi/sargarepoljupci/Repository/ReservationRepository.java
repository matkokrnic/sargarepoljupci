package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /*
    @Query("SELECT r FROM Reservation r " +
            "WHERE (r.reservationStart < :end) AND (r.reservationEnd > :start)")
    List<Reservation> findOverlappingReservations(
            LocalDateTime start,
            LocalDateTime end
    );

     */


    List<Reservation> findByReservationStartIsLessThanAndReservationEndGreaterThan(LocalDateTime reservationEnd, LocalDateTime reservationStart);
    List<Reservation> findByParkingSpotIdInAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
            Collection<String> parkingSpot_id, LocalDateTime reservationEnd, LocalDateTime reservationStart);

    List<Reservation> findByParkingSpotIdIn(Collection<String> parkingSpotId);


    //List<Reservation> findByParkingSpotIdInAndReservationEndAfterAndReservationStartBefore(
    //        Collection<String> parkingSpot_id, LocalDateTime reservationEnd, LocalDateTime reservationStart);
    //List<Reservation> findReservationsByParkingSpotId(String id);



    //List<Reservation> findByParkingSpotIdAndReservationStartBetweenAndReservationEndBetween(
    //        String parkingSpotID,
    //        LocalDateTime start1, LocalDateTime end1,
    //        LocalDateTime start2, LocalDateTime end2
    //);





}


