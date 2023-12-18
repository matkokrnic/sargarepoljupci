package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Korisnik;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Models.uloga;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, String> {


    //
    @Override
    @Nonnull
    public Optional<ParkingSpot> findById(@Nonnull String aLong);

    List<ParkingSpot> findParkingSpotsByAccessibleIsNotNull();
    List<ParkingSpot> findParkingSpotsByAccessibleIsTrue();

    List<ParkingSpot> findParkingSpotsByFreeIsTrueAndFreeIsNotNull();

    @Query("SELECT NEW org.springframework.data.util.Pair(aaaa.longitude, aaaa.latitude) " +
            "FROM ParkingSpot aaaa WHERE aaaa.free = true")
    List<Pair<Double, Double>> findCoordinatesOfFreeParkingSpots();




}
