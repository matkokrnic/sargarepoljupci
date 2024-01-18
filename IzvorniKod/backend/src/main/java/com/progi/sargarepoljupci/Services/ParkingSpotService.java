package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.Request.ReservableUpdateRequest;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    @Autowired
    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, ReservationRepository reservationRepository, ReservationService reservationService) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }


//    public boolean updateParkingSpotAvailability(String parkingSpotId, boolean isFree) {
//        var parkingSpot = parkingSpotRepository.findById(parkingSpotId).orElse(null);
//        if(parkingSpot!=null){
//            parkingSpot.setFree(isFree);
//            return true;
//
//        }
//        return false;
//    }
    public boolean getParkingSpotAvailability(String parkingSpotId) {
        return reservationService.canParkingSpotBeReserved(parkingSpotId, LocalDateTime.now(), LocalDateTime.now());

    }

    public List<Pair<Double, Double>> getCoordinatesOfFreeParkingSpots() {
        return parkingSpotRepository.findCoordinatesOfFreeParkingSpots();
    }



    public void updateReservableStatus(String parkingSpotId, Boolean reservable) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new RequestDeniedException("Parking spot not found with ID: " + parkingSpotId));

        parkingSpot.setReservable(reservable);
        parkingSpotRepository.save(parkingSpot);
    }

    public List<String> updateReservableStatusForMultipleSpots(List<ReservableUpdateRequest> updateDTOList) {
        List<String> notFoundSpotIds = new ArrayList<>();
        for (ReservableUpdateRequest updateDTO : updateDTOList) {
            parkingSpotRepository.findById(updateDTO.getParkingSpotId())
                    .ifPresentOrElse(parkingSpot -> {
                        parkingSpot.setReservable(updateDTO.getReservable());
                        parkingSpotRepository.save(parkingSpot);
                    }, () -> notFoundSpotIds.add(updateDTO.getParkingSpotId()));

        }
        if (!notFoundSpotIds.isEmpty()) {
            throw new RequestDeniedException("Parking spots not found: " + notFoundSpotIds);
        }
        return notFoundSpotIds;
    }












}
