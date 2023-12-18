package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.BicycleParking;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BicycleRepository extends JpaRepository<BicycleParking, String>
{
    @Override
    @Nonnull
    List<BicycleParking> findAll();

    // AKO IMAMO LISTU WAYPOINT (ILI NEKI TIP PODATKA KOJI JE ITERABLE, MOZEMO SAMO TO UNIJETI)
    @Override
    @Nonnull
    List<BicycleParking> findAllById(@Nonnull Iterable<String> strings);


}
