package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.ParkingAuto;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, String> {


    //
    @Override
    @Nonnull
    Optional<ParkingSpot> findById(@Nonnull String aLong);

    List<ParkingSpot> findParkingSpotsByReservableIsNotNull();
    List<ParkingSpot> findParkingSpotsByReservableIsTrue();
    List<ParkingSpot> findParkingSpotsByParkingIsNotNull();

    List<ParkingSpot> findParkingSpotsByFreeIsTrueAndFreeIsNotNull();

    @Query("SELECT NEW org.springframework.data.util.Pair(aaaa.longitude, aaaa.latitude) " +
            "FROM ParkingSpot aaaa WHERE aaaa.free = true")
    List<Pair<Double, Double>> findCoordinatesOfFreeParkingSpots();

    ParkingSpot findByLongitudeAndLatitude(double longitude, double latitude);

    List<ParkingSpot> findByParking(ParkingAuto parkingAuto);


}
