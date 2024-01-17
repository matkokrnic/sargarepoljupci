package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationRepository reservationRepository;
    @Autowired
    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, ReservationRepository reservationRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationRepository = reservationRepository;
    }


    public boolean updateParkingSpotAvailability(String parkingSpotId, boolean isFree) {
        var parkingSpot = parkingSpotRepository.findById(parkingSpotId).orElse(null);
        if(parkingSpot!=null){
            parkingSpot.setFree(isFree);
            return true;

        }
        return false;
    }
    public boolean getParkingSpotAvailability(String parkingSpotId) {
        var parkingSpot = parkingSpotRepository.findById(parkingSpotId).orElse(null);
        if(parkingSpot!=null){
            if(parkingSpot.getFree()==null){
                return false;
            }else
                return parkingSpot.getFree();

        }
        return false;
    }

    public List<Pair<Double, Double>> getCoordinatesOfFreeParkingSpots() {
        return parkingSpotRepository.findCoordinatesOfFreeParkingSpots();
    }

    public void updateAccessibleStatus(String parkingSpotId, Boolean accessible) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found with ID: " + parkingSpotId));

        parkingSpot.setAccessible(accessible);
        parkingSpotRepository.save(parkingSpot);
    }










}
