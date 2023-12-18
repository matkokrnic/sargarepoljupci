package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.Reservation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /*
    @Query("SELECT r FROM Reservation r " +
            "WHERE (r.reservationStart < :end) AND (r.reservationEnd > :start)")
    List<Reservation> findOverlappingReservations(
            Timestamp start,
            Timestamp end
    );

     */

    //

    List<Reservation> findByReservationStartIsLessThanAndReservationEndGreaterThan(Timestamp reservationEnd, Timestamp reservationStart);
    List<Reservation> findByParkingSpotIdInAndReservationEndGreaterThanEqualAndReservationStartLessThanEqual(
            Collection<String> parkingSpot_id, Timestamp reservationEnd, Timestamp reservationStart);
    List<Reservation> findByParkingSpotIdInAndReservationEndAfterAndReservationStartBefore(
            Collection<String> parkingSpot_id, Timestamp reservationEnd, Timestamp reservationStart);
    List<Reservation> findReservationsByParkingSpotId(String id);

    List<Reservation> findByParkingSpotIdIn(Collection<String> parkingSpotId);



}


