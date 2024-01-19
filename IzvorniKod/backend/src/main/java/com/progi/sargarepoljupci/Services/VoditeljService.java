package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.Voditelj;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoditeljService {

    private final VoditeljRepository voditeljRepository;

    @Autowired
    public VoditeljService(VoditeljRepository voditeljRepository) {
        this.voditeljRepository = voditeljRepository;
    }

    @Nonnull
    public Optional<Voditelj> findById(@Nonnull Long id) {
        return voditeljRepository.findById(id);
    }

    public Voditelj save(@Nonnull Voditelj korisnik) {

        return voditeljRepository.save(korisnik);
    }
}
