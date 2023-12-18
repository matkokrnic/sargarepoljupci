package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.ParkingAuto;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingAutoRepository extends JpaRepository<ParkingAuto, Long> {
    @Override
    @Nonnull
    List<ParkingAuto> findAll();
}
