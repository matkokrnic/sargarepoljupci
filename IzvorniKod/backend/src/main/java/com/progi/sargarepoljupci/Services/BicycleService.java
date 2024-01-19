package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.BicycleParking;
import com.progi.sargarepoljupci.Models.Parking;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BicycleService {

    private final BicycleRepository bicycleParkingRepository;

    @Autowired
    public BicycleService(BicycleRepository bicycleParkingRepository) {
        this.bicycleParkingRepository = bicycleParkingRepository;
    }

    @Nonnull
    public List<BicycleParking> findAll() {
        return bicycleParkingRepository.findAll();
    }

    @Nonnull
    public List<BicycleParking> findAllById(@Nonnull Iterable<String> ids) {
        return bicycleParkingRepository.findAllById(ids);
    }

    public BicycleParking findByLongitudeAndLatitude(double longitude, double latitude) {
        // Implement the logic to find a BicycleParking by longitude and latitude
        // You might need to define a custom query in the repository
        return bicycleParkingRepository.findByLongitudeAndLatitude(longitude, latitude);
    }

    public List<BicycleParking> findByParkingLot(Parking parkingLot) {
        return bicycleParkingRepository.findByParkingLot(parkingLot);
    }

    public List<BicycleParking> findByParkingLotIsNull() {
        return bicycleParkingRepository.findByParkingLotIsNull();
    }

    public List<BicycleParking> findByParkingLotIsNotNull() {
        return bicycleParkingRepository.findByParkingLotIsNotNull();
    }
}