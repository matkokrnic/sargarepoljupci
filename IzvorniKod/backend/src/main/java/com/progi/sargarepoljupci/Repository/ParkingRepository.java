package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Parking;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
    @Override
    @Nonnull
    List<Parking> findAll();


}
