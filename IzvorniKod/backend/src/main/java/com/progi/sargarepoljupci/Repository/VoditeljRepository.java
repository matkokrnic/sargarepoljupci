package com.progi.sargarepoljupci.Repository;

import com.progi.sargarepoljupci.Models.Voditelj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoditeljRepository extends JpaRepository<Voditelj, Long> {
    @Override
    Optional<Voditelj> findById(Long aLong);
}
